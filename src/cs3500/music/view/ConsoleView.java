package cs3500.music.view;

import java.io.IOException;
import java.util.List;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.util.MusicUtils;

/**
 * A string output for a {@Link cs3500.music.model.Piece} that can be used for testing within the scope of HW5.
 */
public final class ConsoleView implements View {

    /** Represents the output stream */
    private Appendable output;

    /** The lowest note in the piece to be rendered. */
    private int lowest;

    /** The range of pieces to be rendered. */
    private int range;

    /**
     * Constructs an instance of the cs3500.music.view.ConsoleView class.
     *
     * @param output the output stream
     */
    public ConsoleView(Appendable output) {
        this.output = output;
    }

    /**
     * Updates the lowest note and the range based on the piece that is given. Must be called at
     * the beginning of every method that these variables are used.
     *
     * @param piece the piece to which the values must be updated
     */
    private void update(Piece piece) {
        Note lowNote = piece.getLowest();
        Note hiNote = piece.getHighest();
        if (lowNote == null || hiNote == null) {
            this.lowest = 0;
            this.range = 0;
        }
        else {
            this.lowest = MusicUtils.midiNumber(lowNote.getPitchClass(), lowNote.getOctave());
            this.range = MusicUtils.midiNumber(hiNote.getPitchClass(), hiNote.getOctave()) -
                    this.lowest + 1;
        }
    }

    /**
     * Renders the state of the editor.
     *
     * This must render the piece in its entirety. This is not particularly useful when trying
     * to sync between different Views that are running simultaneously.
     *
     * This method is deprecated with the addition of the controller.
     * @param piece the piece to render
     * @throws IOException if rendering throws the exception
     */
    @Deprecated
    @Override
    public void render(Piece piece) throws IOException {
        this.build(piece);
        System.out.println(this.output.toString());

        return;
    }

    /**
     * Builds up the output stream.
     *
     * @param piece the piece to render
     * @throws IOException if there is an IOException somewhere else
     */
    private void build(Piece piece) throws IOException {
        this.setHeader(piece);

        this.update(piece);

        for (int i = piece.getStart(); i < piece.getEnd(); i += 1) {
            int beat = piece.getEnd();
            String sbeat = String.valueOf(beat);
            for (int j = 0; j < sbeat.length() - String.valueOf(i).length(); j += 1) {
                this.output.append(" "); // one space
            }
            this.output.append(String.valueOf(i));


            List sustains = piece.getNotesSustainedAt(i);
            List attacks = piece.getNotesStartingAt(i);
            for (int j = 0; j < this.range; j += 1) {
                if (MusicUtils.listContainsPitch(sustains,
                        MusicUtils.midiNumberToPitchClass(this.lowest + j),
                        MusicUtils.midiNumberToOctave(this.lowest + j))) {
                    this.output.append(" | ");
                }
                else if (MusicUtils.listContainsPitch(attacks,
                        MusicUtils.midiNumberToPitchClass(this.lowest + j),
                        MusicUtils.midiNumberToOctave(this.lowest + j))) {
                    this.output.append(" X ");
                }
                else {
                    this.output.append("   "); // three spaces
                }
                for(int k = 2; k < String.valueOf((this.lowest + j) / 12).length(); k += 1) {
                    this.output.append(" "); // one space
                }
            }
            this.output.append("\n");
        }
        return;
    }

    /**
     * Creates the header for the output.
     *
     * The header is the part of the output that displays all the note names available for this
     * piece.
     *
     * @param piece the piece for which to set the header
     * @throws IOException if there is an IOException somewhere else
     */
    private void setHeader(Piece piece) throws IOException {
        this.update(piece);

        StringBuilder header = new StringBuilder();
        int pad = String.valueOf(piece.getEnd()).length();

        for (int i = 0; i < pad; i += 1) {
            header.append(" "); // one space
        }

        for (int i = this.lowest; i < this.lowest + this.range; i += 1) {
            header.append(String.format("%3s", MusicUtils.midiNumberToString(i)));
        }
        header.append("\n");
        this.output.append(header.toString());
        return;
    }

    /**
     * Renders the state of the editor for a particular beat.
     *
     * This does not have to render the piece in its entirety, although it may depending on the
     * nature of the implementation. This is especially useful when attempting to sync different
     * implementations of cs3500.music.view.View that are running simultaneously.
     *
     * In this case, the beat has no impact on the render.
     *
     * @param beat the beat at which to render the output
     * @param piece the piece to be rendered
     * @throws IOException if writing the output throws
     */
    @Override
    public void render(int beat, Piece piece) throws IOException {
        this.render(piece);
    }

    /**
     * Stops rendering the editor.
     *
     * There's no way to un-print text, so this does nothing.
     */
    @Override
    public void pause() {
        return;
    }

    /**
     * Initializes the view.
     *
     * There is no additional initialization needed for this view, so does nothing.
     */
    @Override
    public void initialize() {
        return;
    }
}

