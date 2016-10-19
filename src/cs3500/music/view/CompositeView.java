package cs3500.music.view;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

import cs3500.music.model.Piece;

/**
 * A view that combines a GUI view and a MIDI view, keeping them in sync for playback.
 */
public final class CompositeView implements GuiView {
    /**
     * The MIDI view to render
     */
    private final MIDIView midiView;
    /**
     * The GUI view to render
     */
    private final GuiViewFrame guiView;

    /**
     * Creates an instance of the cs3500.music.view.CompositeView.
     *
     * Throws an NullPointerException if either input view is null
     *
     * @param midiView the MIDI view to render
     * @param guiView the GUI view to render
     * @throws NullPointerException if either view is null
     */
    public CompositeView(MIDIView midiView, GuiViewFrame guiView) {
        this.midiView = Objects.requireNonNull(midiView);
        this.guiView = Objects.requireNonNull(guiView);
    }


    /**
     * Renders the state of the editor for a particular beat.
     *
     * This does not have to render the piece in its entirety, although it may depending on the
     * nature of the implementation. This is especially useful when attempting to sync different
     * implementations of cs3500.music.view.View that are running simultaneously.
     *
     * @param beat the beat at which to render the output
     * @param piece the piece to be rendered
     * @throws IOException if writing the output throws
     */
    @Override
    public void render(int beat, Piece piece) throws IOException, InvalidMidiDataException {
        this.guiView.render(beat, piece);
        this.midiView.render(beat, piece);

    }


    /**
     * Renders the state of the editor.
     *
     * This must render the piece in its entirety. This is not particularly useful when trying
     * to sync between different Views that are running simultaneously.
     * This method is deprecated with the addition of the controller.
     * @param piece the piece to be rendered
     * @throws IOException if rendering throws the exception
     */
    @Deprecated
    @Override
    public void render(Piece piece) throws IOException, InvalidMidiDataException {
        int wait = piece.getTempo() / 1000;

        for (int i = 0; i <= piece.getEnd(); i += 1) {
            this.guiView.initialize();
            this.guiView.render(i, piece);
            this.midiView.render(i, piece);
            try {
                Thread.sleep(wait);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Reset the play-head to the beginning when piece is over.
        this.guiView.initialize();
        this.guiView.render(0, piece);

        return;
    }

    /**
     * Stops rendering the editor.
     */
    @Override
    public void pause() {
        this.midiView.pause();
        return;
    }

    /** Initializes the view. */
    @Override
    public void initialize() {
        this.guiView.initialize();
        return;
    }

    @Override
    public int getLength() {
        return this.guiView.getLength();
    }

    /**
     * Returns the size of the top margin.
     * @return the top margin size.
     */
    @Override
    public int getTop() {
        return this.guiView.getTop();
    }

    /**
     * Returns the size of the left margin.
     *
     * @return the left margin size.
     */
    @Override
    public int getSide() {
        return this.guiView.getSide();
    }

    /**
     * Scrolls the composition vertically.
     * @param i An integer representing which way to scroll the view.
     */
    @Override
    public boolean scrollY(int i) {
        return this.guiView.scrollY(i);
    }

    /**
     * Displays a default image for the piece.
     *
     * Displays the GUI view at beat zero, does nothing with the MIDI view.
     *
     * @param p the piece
     */
    @Override
    public void renderDefault(Piece p) {
        this.guiView.render(0, p);
    }

    /**
     * Snaps the view to a certain position.
     *
     * MIDI view does not play.
     *
     * @param beat the beat to which to snap
     * @param p the piece
     */
    @Override
    public void snap(int beat, Piece p) {
        this.guiView.snap(beat, p);
    }

    /**
     * Displays a status representing the editing mode that the user is currently in.
     *
     * @param status the string representing the status of the piece.
     */
    @Override
    public void setStatus(String status) {
        this.guiView.setStatus(status);
    }

    /**
     * Sets the key listener for the GUI view.
     *
     * @param keyListener the key listener
     */
    @Override
    public void setKeyListener(KeyListener keyListener) {
        this.guiView.setKeyListener(keyListener);
    }

    /**
     * Sets the mouse listener for the GUI view.
     * @param mouseListener the mouse listener
     */
    @Override
    public void setMouseListener(MouseListener mouseListener) { this.guiView.setMouseListener(mouseListener);
    }

}

