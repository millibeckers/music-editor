package cs3500.music.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

import cs3500.music.controller.Controller;
import cs3500.music.model.CoolNote;
import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.util.MusicUtils;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewFrame;

import static java.util.Objects.requireNonNull;

/**
 * An implementation of the cs3500.music.controller.Controller, written for HW07.
 */
public class ControllerImpl implements Controller {

    /** The highest pitch is the song. */
    private int highestPitch;

    /** The piece to be played. */
    private final Piece piece;

    /** The view to render the piece. */
    private final GuiView view;

    /** The key handler. */
    private KeyboardHandler keyHandler;

    /** The mouse handler. */
    private MouseHandler mouseHandler;

    /** The timer. */
    private final Timer timer;

    /**
     * The beat that is currently playing.
     *
     * INVARIANT: 0 <= currentBeat < this.piece.getEnd()
     */
    private int currentBeat;

    /** Flag for if the piece is currently playing.
     * INVARIANTS: If the piece is playing, the rest of the flags must be false.
     * SelectedNote must be null. SelectedBeat and SelectedPitch must be -1.*/
    private boolean isPlaying;

    /**
     * The note that is selected.
     *
     * If no note is selected, this will be null.
     * INVARIANT: Cannot be nonNull if selectedSpace == true or if the piece is playing.
     */
    private Note selectedNote;

    /**
     * A boolean flag indicating whether a space for a note has been selected.
     * INVARIANTS: This can only be true if isPlaying == false.
     *             This can only be true if selectedNote == null.
     */
    private boolean selectedSpace;

    /**
     * Boolean flag indicating whether the note is being edited for volume.
     * INVARIANT: This field must be false if there is no selected note.
     */
    private boolean editingVolume;

    /**
     * The number to set the instrument or volume to.
     * Set to "" by default.
     * INVARIANT: This StringBuilder will never have a value greater than "" unless
     * editingVolume == true.
     */
    private StringBuilder number;

    /**
     * Integers specifying the beat and pitch of the empty space selected.
     * Initialized to -1 if no space has been selected.
     * INVARIANT: These fields will only ever have values above -1 if selectedSpace == true.
     */
    private int selectedBeat;
    private int selectedPitch;

    /**
     * Creates an instance of this cs3500.music.controller.Controller.
     *
     * @param piece the piece to be implemented
     * @param view the view to render the piece.
     * @throws NullPointerException if any input is null
     */
    public ControllerImpl(Piece piece, GuiView view) {
        this.piece = requireNonNull(piece);
        this.view = requireNonNull(view);
        this.keyHandler = new KeyboardHandler();
        this.keyHandlerSetup();
        this.mouseHandler = new MouseHandler();
        this.mouseHandlerSetup();
        this.view.setKeyListener(this.keyHandler);
        this.view.setMouseListener(this.mouseHandler);
        this.timer = new Timer(0, new TimerListener());
        this.timer.setDelay(this.piece.getTempo() / 1000);
        this.currentBeat = 0;
        this.isPlaying = false;
        this.selectedNote = null;
        this.selectedSpace = false;
        this.selectedBeat = -1;
        this.selectedPitch = -1;
        this.editingVolume = false;
        this.number = new StringBuilder();
        this.highestPitch = MusicUtils.midiNumber(this.piece.getHighest().getPitchClass(),
                this.piece.getHighest().getOctave());
    }

    public KeyboardHandler getKeyHandler() {
        return this.keyHandler;
    }
    public MouseHandler getMouseHandler() {
        return this.mouseHandler;
    }

    /**
     * Sets the mouse handler for the controller.
     */
    @Override
    public void setMouseHandler(MouseHandler m) {
        this.mouseHandler = m;
        this.view.setMouseListener(m);
    }

    /**
     * Sets the key handler for the controller.
     */
    @Override
    public void setKeyHandler(KeyboardHandler k) {
        this.keyHandler = k;
        this.view.setKeyListener(k);
    }

    /**
     * Gets the current beat.
     */
    @Override
    public int getCurrentBeat() {
        return this.currentBeat;
    }

    /**
     * Returns whether the piece is playing.
     *
     * @return whether the piece is playing.
     */
    @Override
    public boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * Returns the highest pitch.
     */
    @Override
    public int getHighest() {
        return this.highestPitch;
    }

    /**
     * Returns the selected note.
     */
    @Override
    public Note getSelectedNote() {
        return this.selectedNote;
    }

    /**
     * Adds a lambda expression to the controller dealing with key input
     */
    @Override
    public void addKeyResponse(int key, Runnable run) {
        this.keyHandler.addKeyResponse(key, run);
    }

    /**
     * Adds a lambda expression to the controller dealing with mouse input
     */
    @Override
    public void addMouseResponse(int key, Consumer consumer) {
        this.mouseHandler.addMouseResponse(key, consumer);
    }

    /**
     * Sets up the keyHandler.
     *
     * Actions that can be taken with keyboard input:
     * <ul>
     *     <li>Play -> Spacebar, if piece is not playing.</li>
     *     <li>Pause -> Spacebar, if the piece is playing.</li>
     *     <li>Jump to start -> [ , if the piece is not playing.</li>
     *     <li>Jump to end -> [ , if the piece is not playing.</li>
     *     <li>Scroll left -> LEFT arrow, if the piece is not playing.</li>
     *     <li>Scroll right -> RIGHT arrow, if the piece is not playing.</li>
     *     <li>Delete selected note -> backspace (delete on mac), if piece is not playing.</li>
     *     <li>Delete volume input -> backspace, if piece is not playing and
     *     the volume is being edited.</li>
     *     <li>Enter Volume-Editing Mode -> V, if there is a selected cs3500.music.model.Note.</li>
     *     <li>Exit Volume-Editing Mode/Confirm Volume -> V, is currently in V-E Mode</li>
     *     <li>Input Volume -> 0,1,2,3,4,5,6,7,8,9 if currently in V-E Mode</li>
     * </ul>
     */
    private void keyHandlerSetup() {

        // For play/pause.
        this.addKeyResponse(KeyEvent.VK_SPACE,
                () -> {
                    this.isPlaying = !this.isPlaying;
                    this.selectedNote = null;
                    this.selectedSpace = false;
                    if (!this.isPlaying) {
                        this.view.setStatus("Paused...");
                    } else {
                        this.view.setStatus("Playing!");
                    }
                    view.snap(this.currentBeat, this.piece);

                }
        );

        // For jumping to the start of the composition.
        this.addKeyResponse(KeyEvent.VK_OPEN_BRACKET, () -> {
            if (!this.isPlaying) {
                this.currentBeat = 0;
                this.view.snap(this.currentBeat, this.piece);
            }
        });

        // For jumping to the end of the composition.
        this.addKeyResponse(KeyEvent.VK_CLOSE_BRACKET, () -> {
            if (!this.isPlaying) {
                this.currentBeat = this.piece.getEnd();
                this.view.snap(this.currentBeat, this.piece);
            }
        });

        // For scrolling right.
        this.addKeyResponse(KeyEvent.VK_RIGHT, () -> {
            if (!this.isPlaying) {
                if (this.currentBeat < this.piece.getEnd()) {
                    this.currentBeat += 1;
                }
                try {
                    this.view.snap(this.currentBeat, this.piece);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // For scrolling left.
        this.addKeyResponse(KeyEvent.VK_LEFT, () -> {
            if (!this.isPlaying) {
                if (this.currentBeat > 0) {
                    this.currentBeat -= 1;
                }
                try {
                    this.view.snap(this.currentBeat, this.piece);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // For scrolling down.
        this.addKeyResponse(KeyEvent.VK_DOWN, () -> {
            if (!this.isPlaying) {
                if (this.view.scrollY(-1)) {
                    this.highestPitch -= 1;
                    this.view.snap(this.currentBeat, this.piece);
                }
            }
        });

        // For scrolling down.
        this.addKeyResponse(KeyEvent.VK_UP, () -> {
            if (!this.isPlaying) {
                if (this.view.scrollY(1)) {
                    this.highestPitch += 1;
                    this.view.snap(this.currentBeat, this.piece);
                }
            } else {
                //do nothing
            }
        });

        // For deleting a note or deleting an entry to the volume input.
        this.addKeyResponse(KeyEvent.VK_BACK_SPACE, () -> {
            if (!this.isPlaying && this.selectedNote != null &&
                    !this.selectedSpace && !this.editingVolume) {
                try {
                    this.piece.removeNote(this.selectedNote);
                } catch (IllegalArgumentException exn) {
                    // do nothing
                }
                this.view.setStatus("Paused...");
                this.view.snap(this.currentBeat, this.piece);
                this.selectedNote = null;
            } else if (!this.isPlaying && this.selectedNote != null &&
                    !this.selectedSpace && this.editingVolume) {
                this.number.deleteCharAt(this.number.length() - 1);
                this.view.setStatus(this.number.toString());
                this.view.snap(this.currentBeat, this.piece);
            }
        });

        // For changing the volume of a selected cs3500.music.model.Note.
        this.addKeyResponse(KeyEvent.VK_V, () -> {
            if (!this.isPlaying && this.selectedNote != null && !this.selectedSpace) {
                if (this.editingVolume && (this.number.length() > 0)) {
                    int volume = Integer.valueOf(this.number.toString());
                    if (volume > 127) {
                        throw new IllegalArgumentException("The volume is too high!");
                    }
                    try {
                        this.piece.removeNote(this.selectedNote);
                        Note n = new CoolNote(this.selectedNote.getPitchClass(),
                                this.selectedNote.getOctave(), this.selectedNote.getDuration(),
                                this.selectedNote.getAttack(), volume,
                                this.selectedNote.getInstrument());
                        this.piece.addNote(n);
                    } catch (IllegalArgumentException exn) {
                        //do nothing
                    }
                    this.view.setStatus("Paused...");
                    this.view.snap(this.currentBeat, this.piece);
                    this.selectedNote = null;
                    this.editingVolume = false;
                    this.number = new StringBuilder();
                } else if (this.editingVolume && (this.number.length() == 0)) {
                    this.view.setStatus("Paused...");
                    this.view.snap(this.currentBeat, this.piece);
                    this.selectedNote = null;
                    this.editingVolume = false;
                } else {
                    this.editingVolume = true;
                    this.view.setStatus("Editing volume...");
                    this.view.snap(this.currentBeat, this.piece);
                }
            }
        });

        /**
         * The following are methods that initialize lambdas for adding to the number input string.
         */
        this.addKeyResponse(KeyEvent.VK_0, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("0");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
        this.addKeyResponse(KeyEvent.VK_1, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("1");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
        this.addKeyResponse(KeyEvent.VK_2, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("2");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
        this.addKeyResponse(KeyEvent.VK_3, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("3");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
        this.addKeyResponse(KeyEvent.VK_4, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("4");

                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
        this.addKeyResponse(KeyEvent.VK_5, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("5");

                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );

        this.addKeyResponse(KeyEvent.VK_6, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("6");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );

        this.addKeyResponse(KeyEvent.VK_7, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("7");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );

        this.addKeyResponse(KeyEvent.VK_8, () -> {
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("8");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );

        this.addKeyResponse(KeyEvent.VK_9, () -> {
                    System.out.println(KeyEvent.VK_9);
                    if ((this.editingVolume) && this.selectedNote != null) {
                        this.number.append("9");
                        this.view.setStatus(this.number.toString());
                        this.view.snap(this.currentBeat, this.piece);
                    }
                }
        );
    }


    /**
     * Sets up the mouse handler.
     *
     * Actions that can be taken out with mouse input:
     * <ul>
     *     <li>Click to select a note, if piece is not playing.</li>
     *     <li>Click to move a selected note to that position, if the piece is not playing.</li>
     *     <li>Click an empty space to select a pitch for a new note. Click again to set the
     *     duration and the starting/ending beat of the note. The note will start at the leftmost
     *     click and end at the rightmost click, using the pitch of the first click.</li>
     * </ul>
     */
    private void mouseHandlerSetup() {
        Consumer<MouseEvent> c;
        this.addMouseResponse(1, c = (e) -> {
            int x = e.getX();
            int y = e.getY();
            Note n = this.getNoteAt(this.getBeatFromX(x), this.getPitchFromY(y));

            // for selecting notes
            if (n != null && !this.selectedSpace && !this.isPlaying && this.selectedNote == null) {
                this.selectedNote = n;
                this.view.setStatus("Selected note..." +
                        n.getPitchClass().toString() +
                        n.getOctave() + " at beat " + n.getAttack());
                this.view.snap(this.currentBeat, this.piece);
            }
            // for moving a selected note
            else if (this.selectedNote != null && !this.selectedSpace && !this.isPlaying) {
                int midiNumber = this.getPitchFromY(y);
                Note newNote = new CoolNote(MusicUtils.midiNumberToPitchClass(midiNumber),
                        MusicUtils.midiNumberToOctave(midiNumber), this.selectedNote.getDuration(),
                        this.getBeatFromX(x), this.selectedNote.getVelocity(),
                        this.selectedNote.getInstrument());
                try {
                    this.piece.removeNote(this.selectedNote);
                    this.piece.addNote(newNote);
                } catch (IllegalArgumentException ex) {
                    this.piece.addNote(this.selectedNote);
                }
                this.selectedNote = null;
                this.view.setStatus("Paused...");
                this.view.snap(this.currentBeat, this.piece);
            }
            // for adding notes when in AddNoteMode
            else if (this.selectedSpace && this.selectedNote == null && n == null && !this.isPlaying) {
                int duration = Math.abs(this.selectedBeat - this.getBeatFromX(x)) + 1;
                int start;

                // finds the starting beat for the new note; depends on where we clicked first and now
                if (this.getBeatFromX(x) < this.selectedBeat) {
                    start = this.getBeatFromX(x);
                } else if (this.getBeatFromX(x) > this.selectedBeat) {
                    start = Math.abs(this.getBeatFromX(x) - duration + 1);
                } else {
                    start = this.getBeatFromX(x);
                }
                // constructs a new note to add
                Note newNote = new CoolNote(MusicUtils.midiNumberToPitchClass(this.selectedPitch),
                        MusicUtils.midiNumberToOctave(this.selectedPitch), duration, start, 103, 1);
                try {
                    this.piece.addNote(newNote);
                } catch (IllegalArgumentException ex) {

                }
                this.selectedSpace = false;
                this.selectedBeat = -1;
                this.selectedPitch = -1;
                this.view.snap(this.currentBeat, this.piece);
                this.view.setStatus("Paused...");
                this.view.snap(this.currentBeat, this.piece);

            }
            // for setting addNoteMode
            else if (n == null && !this.selectedSpace && !this.isPlaying) {
                this.view.setStatus("Adding a note at beat " + this.getBeatFromX(x) + " of the pitch " +
                        MusicUtils.midiNumberToPitchClass(this.getPitchFromY(y)) +
                        MusicUtils.midiNumberToOctave(this.getPitchFromY(y)));
                this.selectedSpace = true;
                this.isPlaying = false;
                this.selectedBeat = this.getBeatFromX(x);
                this.selectedPitch = this.getPitchFromY(y);
                this.view.snap(this.currentBeat, this.piece);
            }
            // There has been a mis-click; reset everything
            else {
                this.view.setStatus("Paused...");
                this.selectedSpace = false;
                this.selectedNote = null;
                this.selectedBeat = -1;
                this.selectedPitch = -1;
                this.view.snap(this.currentBeat, this.piece);
            }
        });
    }

    /**
     * A private helper for retrieving the corresponding beat given the x-position of the mouse.
     * @param x the x-position of the mouse, in pixels.
     * @return the beat representation of the position.
     */
    private int getBeatFromX(int x) {
        int beatsPerFrame = (this.view.getLength() - GuiViewFrame.BUFFER) / GuiViewFrame.CELL;
        int frame = this.currentBeat / beatsPerFrame;
        return ((x - this.view.getSide()) / GuiViewFrame.CELL) + (frame * beatsPerFrame) - 2;
    }

    /**
     * A private helper for retrieving the corresponding pitch given the y-position of the mouse.
     * @param y the y-position of the mouse, in pixels.
     * @return the pitch representation of the position.
     */
    private int getPitchFromY(int y) {
        int pitchCell = this.highestPitch - (y - (GuiViewFrame.BUFFER + this.view.getTop())) /
                GuiViewFrame.CELL;
        return pitchCell;
    }

    /**
     * Retrieves the note at the given beat and pitch, if one exists.
     * @param beat the starting beat of the note.
     * @param pitch the pitch of the note.
     * @return the cs3500.music.model.Note at the given beat and pitch within the piece.
     * @throws IllegalArgumentException if the beat or pitch are less than zero.
     */
    private Note getNoteAt(int beat, int pitch) {
        if (beat < 0 || pitch < 0) {
            throw new IllegalArgumentException("Beat and/or pitch are less than zero.");
        }
        Note noteAt = null;
        List<Note> notes = this.piece.getAllNotesAt(beat);
        for (Note n : notes) {
            if (MusicUtils.midiNumber(n.getPitchClass(), n.getOctave()) == pitch) {
                noteAt = n;
            }
        }
        return noteAt;
    }

    /**
     * Starts the program.
     */
    @Override
    public void activate() {
        this.timer.start();
        this.view.renderDefault(this.piece);
        this.view.initialize();
    }

    /**
     * A small class to act as an action listener for the timer.
     */
    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (isPlaying) {
                try {
                    view.render(currentBeat, piece);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (currentBeat >= piece.getEnd()) {
                    isPlaying = false;
                    currentBeat = 0;
                    view.snap(0, piece);
                }
                else {
                    currentBeat += 1;
                }
            }
            return;
        }
    }
}

