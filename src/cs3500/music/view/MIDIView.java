package cs3500.music.view;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.Note;
import cs3500.music.model.Piece;
import cs3500.music.util.MusicUtils;

/**
 * Represents and creates an audio view (playback) using MIDI protocol.
 */
public final class MIDIView implements View {
    /**
     * The synthesizer that will act as the sound module for playback.
     */
    private final Synthesizer synth;

    /**
     * The reciever that will receive and interpret MIDI commands.
     */
    private final Receiver receiver;

    /**
     * Creates an instance of the MIDI view.
     *
     * Will throw a MidiUnavailableException if the MIDI systems are not available.
     */
    public MIDIView() {
        Synthesizer tempSynth;
        Receiver tempRec;
        try {
            tempSynth = MidiSystem.getSynthesizer();
            tempRec = tempSynth.getReceiver();
            tempSynth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            tempSynth = null;
            tempRec = null;
        }
        this.synth = tempSynth;
        this.receiver = tempRec;
    }

    /**
     * Creates an instance of the MIDI view, using the given reciever.
     *
     * This is mainly used for testing purposes.
     */
    public MIDIView(Receiver r) {
        Synthesizer tempSynth;
        try {
            tempSynth = MidiSystem.getSynthesizer();
            tempSynth.open();
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
            tempSynth = null;
        }
        this.synth = tempSynth;
        this.receiver = r;
    }

    /**
     * Displays the state of the editor at a particular beat.
     *
     * Sends all on and off MIDI messages that would be necessary for the correct playback at
     * the given beat.
     *
     * @param beat the beat at which to draw every note
     * @param piece the piece to render
     * @return a text stream equivalent of the output, for testing purposes.
     * @throws IOException if writing the output throws
     */
    @Override
    public void render(int beat, Piece piece) throws InvalidMidiDataException {
        List<Note> starts = piece.getNotesStartingAt(beat);
        List<Note> ends = this.ends(beat, piece.getAllNotesAt(beat - 1)); // wont throw anything

        for (Note n : ends) {
            this.receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, n.getInstrument(),
                    MusicUtils.midiNumber(n.getPitchClass(), n.getOctave()), 0), beat);
        }
        for (Note n : starts) {
            this.receiver.send(new ShortMessage(ShortMessage.NOTE_ON, n.getInstrument(),
                            MusicUtils.midiNumber(n.getPitchClass(), n.getOctave()),
                            n.getVelocity()),
                    beat);
        }

        return;
    }

    /**
     * Renders the state of the editor.
     *
     * This must render the piece in its entirety. This is not particularly useful when trying
     * to sync between different Views that are running simultaneously.
     *
     * @param piece the piece to render
     * @throws IOException if rendering throws the exception
     */
    @Override
    public void render(Piece piece) throws InvalidMidiDataException {
        int wait = piece.getTempo() / 1000;

        for (int i = 0; i <= piece.getEnd(); i += 1) {
            this.render(i, piece);
            try {
                Thread.sleep(wait);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return;
    }

    /**
     * Stops drawing the editor.
     *
     * Sends off messages to the MIDI source, stopping all sound regardless of the correct lengths
     * of the notes that are currently playing.
     */
    @Override
    public void pause() {
        try {
            // controller 123, all notes off.
            this.receiver.send(new ShortMessage(176, 123, 0), -1);
        }
        catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return;
    }


    /**
     * Initializes the view.
     *
     * Nothing is neccesary for this view, so does nothing.
     */
    public void initialize() {
        return;
    }

    /**
     * Returns a List of all notes that are to be turned off at the given beat
     */
    private List<Note> ends(int beat, List<Note> playing) {
        List<Note> ends = new ArrayList<Note>();
        for (Note n : playing) {
            if (n.getAttack() + n.getDuration() == beat) {
                ends.add(n);
            }
        }
        return ends;
    }


}

