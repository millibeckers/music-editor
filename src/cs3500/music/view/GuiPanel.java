package cs3500.music.view;

import java.awt.*;

import java.util.List;
import javax.swing.*;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.util.MusicUtils;

import static java.util.Objects.requireNonNull;

/**
 * Created by Sasha on 11/13/2015.
 */
final class GuiPanel extends JPanel {

    private Piece p;
    private final int BUFFER = GuiViewFrame.BUFFER;
    private final int CELL = GuiViewFrame.CELL;
    // the highest pitch to render.
    // INVARIANT: The highest pitch must be greater than 0.
    private int highest;
    // the last beat in the song.
    private int lastBeat;
    // the current beat in the song.
    private int beat;
    // the first beat in this frame.
    private int start;
    // the last beat in this frame.
    private int end;
    // the number of beats per frame.
    private int beatsPerScreen;
    // the status of the song.
    private String status;
    // a boolean flag determining if the user has scrolled.
    // Used for preventing the panel from intializing the default values.
    private boolean changedPitch = false;

    /**
     * A method strictly tied to {@code render()} that initializes
     * all of the fields in this class, which will be drawn.
     *
     * INVARIANTS:
     * The given piece cannot be null.
     * The given piece must have at least one note.
     * The start beat must be non-negative.
     * The end beat must be greater than the start beat.
     *
     * @param p the piece to be rendered
     * @param beat the beat that is currently playing
     * @param start the beat at which to start rendering
     * @param end the beat at which to stop rendering
     * @throws IllegalArgumentException if the given beat/start is negative,
     * if the start > end, or if the {@code cs3500.music.model.Piece} doesn't have any notes.
     * @throws NullPointerException if the given {@code cs3500.music.model.Piece} is null.
     */
    void initialize(Piece p, int beat, int start, int end, String status) {
        if (beat < 0 || start < 0 || end < start || p.getNotes().isEmpty()) {
            throw new IllegalArgumentException("Bad arguments!");
        }
        this.p = requireNonNull(p);

        int highestPitch = MusicUtils.midiNumber(p.getHighest().getPitchClass(),
                p.getHighest().getOctave());

        // initialize the default highestPitch
        if(!this.changedPitch) {
            this.highest = highestPitch;
        }
        this.start = start;
        this.end = end;
        this.lastBeat = p.getEnd();
        this.beat = beat;
        this.beatsPerScreen = this.end - this.start;
        this.status = status;
    }

    /**
     * Draws all of the piece in its entirety, provided that all of the fields are initialized
     * (which doesn't happen unless initialize gets called)
     * @param g The graphics object used by Swing to draw components of the panel
     */
    @Override
    public void paint(Graphics g) {
        if (this.p == null) {
            return;
        }
        // sets the font size for use in rendering the scales
        g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        this.drawNotes(g);
        this.drawVertical(g);
        this.drawBar((Graphics2D) g);
        this.drawHorizontal((Graphics2D) g);
        this.drawStatus(g);
    }

    /**
     * Draws the status of the piece.
     * @param g The graphics object used by Swing to draw components of the panel.
     */
    private void drawStatus(Graphics g) {
        g.setFont(new Font("Candara", Font.PLAIN, 24));
        g.setColor(new Color(255, 86, 99));
        g.drawString(this.status, BUFFER + 12, BUFFER + 24);
    }

    /**
     * Scrolls the composition vertically.
     */
    public boolean scrollY(int i, int height) {
        if (this.highest + i > 127 || this.highest + i < ((height - (3 * BUFFER)) / CELL)) {
            return false;
        } else {
            this.highest = this.highest + i;
            this.changedPitch = true;
            return true;
        }
    }

    /**
     * Draws the notes in the piece.
     *
     * @param g the graphics object used by Swing to draw components of the panel
     */
    private void drawNotes(Graphics g) {
        // loops through the beats and draws all notes at each beat
        for (int i = this.start; i <= this.end; i++) {
            this.drawBeat(i, g);
        }
    }

    /**
     * Draws all notes at a particular beat
     * @param beat the current beat used to retrieve the notes
     * @param g the graphics object used by Swing to draw panel components
     * @throws IllegalArgumentException if {@param i} is negative or if
     * {@param position} is negative
     */
    private void drawBeat(int beat, Graphics g) {
        if (beat < 0) {
            throw new IllegalArgumentException("Improper arguments!");
        }
        this.drawNotesAtBeat(p.getAllNotesAt(beat), g, beat);
    }

    /**
     * Draws all of the notes at this beat (including both sustained and starting)
     * @param list the list of notes to draw
     * @param g the graphics object used by Swing to draw components of the panel
     * @param beat the beat at which the given notes play
     * @throws IllegalArgumentException if {@code start} or {@code i} are less than zero.
     */
    private void drawNotesAtBeat(List<Note> list, Graphics g, int beat) {
        if(beat < 0) {
            throw new IllegalArgumentException("Improper arguments!");
        }
        for(Note n : list) {
            if(p.getNotesStartingAt(beat).contains(n)) {
                this.drawNote(new Color(15, 61, 61), n, beat, g);
            }
            else {
                this.drawNote(new Color(51, 204, 204), n, beat, g);
            }
        }
    }

    /**
     * Draws a note in the editor.
     * @param c the Color of the note
     * @param n the note to draw
     * @param position the x-position at which to draw the note
     * @param g the Swing graphic to mutate
     */
    private void drawNote(Color c, Note n, int position, Graphics g) {
        g.setColor(c);

        int y = (this.highest - MusicUtils.midiNumber(n.getPitchClass(),
                n.getOctave())) *
                this.CELL + this.BUFFER;
        if (y < this.BUFFER) {
            // don't draw the note.
        }
        else {
            g.fillRect((position - this.start) * this.CELL + this.BUFFER, y,
                    this.CELL, this.CELL);
        }
    }

    /**
     * Draws the red vertical bar
     * @param g the graphics object used by Swing to draw the components of the panel
     */
    private void drawBar(Graphics2D g) {
        int set = this.beat - this.start;
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(3));

        g.drawLine(set * CELL + BUFFER, BUFFER,
                set * CELL + BUFFER,
                CELL * (this.highest + 1) + BUFFER);
    }

    /**
     * Draws all the horizontal lines dividing pitches and the pitch scale
     * @param g the graphics object used by Swing to draw the components of the panel
     */
    private void drawHorizontal(Graphics2D g) {
        g.setColor(Color.BLACK);

        g.setStroke(new BasicStroke(1));
        g.drawLine(this.BUFFER, this.BUFFER,
                Math.max(this.end, this.lastBeat) * this.CELL + this.BUFFER,
                this.BUFFER);

        for (int i = this.highest ; i >= 0; i--) {
            // this pitch is a the end of an octave
            if (i % 12 == 0) {
                g.setStroke(new BasicStroke(3));
            } else {
                g.setStroke(new BasicStroke(1));
            }
            // draws the horizontal line
            g.drawLine(this.BUFFER, this.CELL * (this.highest - i + 1) + this.BUFFER,
                    Math.max(this.end, this.lastBeat) * this.CELL + this.BUFFER,
                    this.CELL * (this.highest - i + 1) + BUFFER);
            // draws the label corresponding to the line
            g.drawString(MusicUtils.midiNumberToString(i), BUFFER / 2, BUFFER + CELL / 2 +
                    (Math.abs(i - this.highest) * CELL));
        }
    }

    /**
     * Draws all the vertical bars separating measures as well as the measure labels above them
     * @param g the graphics object used by Swing to draw components of the panel
     */
    private void drawVertical(Graphics g) {
        g.setColor(Color.BLACK);
        for(int i = this.start; i < this.end; i++) {
            if((i % 4) == 0) {
                g.drawLine(BUFFER + (i - this.start) * CELL, BUFFER,
                        BUFFER + (i - this.start) * CELL, CELL * (this.highest + 1) + BUFFER);
                g.drawString(Integer.toString(i), BUFFER + (i - this.start) * CELL, BUFFER / 2);
            }
        }
        g.drawLine(BUFFER + this.beatsPerScreen * CELL, BUFFER, BUFFER + this.beatsPerScreen *
                        CELL,
                CELL * (this.highest + 1) + BUFFER);
    }

}


