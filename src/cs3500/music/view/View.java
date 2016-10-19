package cs3500.music.view;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

import cs3500.music.model.Piece;

/**
 * An interface for representing the view of the model
 */
public interface View {

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
    public void render(int beat, Piece piece) throws IOException, InvalidMidiDataException;

    /**
     * Renders the state of the editor.
     *
     * This must render the piece in its entirety. This is not particularly useful when trying
     * to sync between different Views that are running simultaneously.
     *
     * @param piece the piece to be rendered
     * @throws IOException if rendering throws the exception
     */
    public void render(Piece piece) throws IOException, InvalidMidiDataException;

    /**
     * Stops rendering the editor.
     */
    public void pause();

    /** Initializes the view. */
    public void initialize();


}


