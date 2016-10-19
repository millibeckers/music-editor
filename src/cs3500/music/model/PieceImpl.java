package cs3500.music.model;

import java.util.*;

import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicUtils;

/**
 * Represents a concrete implementation of a piece.
 */
public final class PieceImpl implements Piece {

    /**
     * Represents the collection of notes in the piece.
     *
     * The key in the hash map is the beat number, and the value is a list of all notes sounding
     * at that beat.
     *
     * INVARIANT: No two notes with the same pitch (same pitch class and same octave number) are
     *            sounding at the same time.
     *
     */
    private final Map<Integer, ArrayList<Note>> notes;


    /**
     * Number of beats per measure.
     *
     * For the sake of this exercise, we are assuming that meter never changes throughout the
     * piece. We are also assuming that the bottom number in the time signature is 4.
     *
     * INVARIANT: this.beatsPerMeasure > 1
     */
    private final int beatsPerMeasure;

    /**
     * The tempo of the piece, measured in microseconds per beat.
     *
     * INVARIANT: this.mspb > 1
     */
    private final int tempo;


    /**
     * Constructs an instance of the cs3500.music.model.PieceImpl class.
     *
     * Throws an IllegalArgumentException if there are two or more notes of the same pitch (same
     * pitch class and same octave) sounding at the same time, or if the beats per measure or
     * tempo is less than one.
     *
     * @param bpm the beats per measure of the piece
     * @param tempo the number of microseconds per beat
     * @param notes the notes to be added initially.
     */
    public PieceImpl(int bpm, int tempo, Note... notes) {
        this.notes = new HashMap<Integer, ArrayList<Note>>();

        for (Note n : notes) {
            this.addNote(n);
        }

        this.beatsPerMeasure = bpm;
        this.tempo = tempo;
    }



    /**
     * Returns the number of beats per each measure.
     *
     * It is assumed that there are no meter changes in the piece. It is also assumed that the
     * bottom number of the time signature is 4.
     *
     * @return the number of beats per measure
     */
    @Override
    public int beatsPerMeasure() {
        return this.beatsPerMeasure;
    }

    /**
     * Returns the tempo of the piece, given in beats per microsecond.
     *
     * @return the tempo of the piece, given in beats per microsecond.
     */
    @Override
    public int getTempo() {
        return this.tempo;
    }

    /**
     * Adds the given note to the piece.
     * <p>
     * Throws an IllegalArgumentException if there is already a note at that pitch being played
     * by that instrument playing at any time during the given note's duration or if the note to
     * be added has a duration of than 1 or a starting beat of less than zero.
     *
     * @param note the note to be added
     * @throws IllegalArgumentException if a note already exists at that pitch with that instrument
     *                                  at any time the given note is meant to sound, if the
     *                                  note to be added's duration is less than 1 or starting
     *                                  beat is less than 0
     */
    @Override
    public void addNote(Note note) {
        //check if the note already exists
        for (int i = note.getAttack(); i < note.getAttack() + note.getDuration(); i += 1) {
            if(this.notes.containsKey(i)) {
                for (Note n : this.notes.get(i)) {
                    if (n.samePitch(note) && n.getInstrument() == note.getInstrument()) {
                        throw new IllegalArgumentException("cs3500.music.model.Note overlap: notes already exist");
                    }
                }
            }
        }

        // now lets actually add it
        for (int i = note.getAttack(); i < note.getAttack() + note.getDuration(); i += 1) {
            if (!this.notes.containsKey(i)) {
                this.notes.put(i, new ArrayList<Note>());
            }
            this.notes.get(i).add(note);
        }
        return;
    }

    /**
     * Removes the given note from the piece.
     * <p>
     * Throws an IllegalArgumentException if the given note does not exist.
     *
     * @param note the note to be removed
     * @throws IllegalArgumentException
     */
    @Override
    public void removeNote(Note note) {
        boolean removed = false;
        for (int i = note.getAttack(); i < note.getAttack() + note.getDuration(); i += 1) {
            if(this.notes.containsKey(i) && this.notes.get(i).contains(note)) {

                removed = this.notes.get(i).remove(note);

                if (this.notes.get(i).isEmpty()) { // make sure there aren't any entries in notes
                    this.notes.remove(i);          // that don't have anything in the ArrayList.
                }
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("cs3500.music.model.Note was not found");
        }
        return;
    }

    /**
     * Gets a copy of the notes starting at the given beat.
     * <p>
     * If no notes at that beat, will return an empty ArrayList.
     *
     * @param beat the beat number the notes we want are at
     * @return a list of the notes starting at the given beat
     */
    @Override
    public List<Note> getNotesStartingAt(int beat) {
        List<Note> temp = new ArrayList<Note>();
        if (!this.notes.containsKey(beat)) {
            return temp;
        }
        for(Note n : this.notes.get(beat)) {
            if (n.getAttack() == beat) {
                temp.add(n.copy());
            }
        }
        return temp;
    }

    /**
     * Gets a copy of all notes sounding at the given beat.
     * <p>
     * If no notes are at the given beat, will return an empty ArrayList.
     *
     * @param beat the beat number the notes we want are at
     * @return a list of all notes sounding at the given beat.
     */
    @Override
    public List<Note> getAllNotesAt(int beat) {
        List<Note> temp = new ArrayList<Note>();
        if (!this.notes.containsKey(beat)) {
            return temp;
        }
        for(Note n : this.notes.get(beat)) {
            temp.add(n.copy());
        }
        return temp;
    }

    /**
     * Gets a copy of all notes that are sustained over the given beat.
     *
     * A note that is sustained over a beat is a note that is sounding for that beat but is not
     * starting there.
     *
     * If no applicable notes at that beat, will return an empty ArrayList.
     *
     * @param beat the beat number of the notes we want
     * @return a list of all notes sustaned through the given beat
     */
    @Override
    public List<Note> getNotesSustainedAt(int beat) {
        List<Note> temp = new ArrayList<Note>();
        if (!this.notes.containsKey(beat)) {
            return temp;
        }
        for(Note n : this.notes.get(beat)) {
            if (n.getAttack() != beat) {
                temp.add(n.copy());
            }
        }
        return temp;
    }

    /**
     * Gets a copy of the highest note in the piece.
     * <p>
     * The highest note is the one with the highest octave number and the pitch class closest to
     * B of that octave number.
     *
     * If there are no notes in the piece, returns null.
     *
     * @return a copy of the highest note in the piece, or null if there are no notes
     */
    @Override
    public Note getHighest() {
        List<Note> temp = this.getNotes();
        if (temp.size() == 0) {
            return null;
        }

        Note highestNote = temp.get(0);
        for (int i = 0; i < temp.size(); i += 1) {
            if (temp.get(i).isHigherThan(highestNote)) {
                highestNote = temp.get(i);
            }
        }
        return highestNote;
    }

    /**
     * Gets a copy of the lowest note in the piece.
     * <p>
     * The lowest note is the one with the lowest octave number and the pitch class closest to C
     * of that octave number.
     *
     * @return a copy of the lowest note in the piece
     */
    @Override
    public Note getLowest() {
        List<Note> temp = this.getNotes();
        if (temp.size() == 0) {
            return null;
        }

        Note lowestNote = temp.get(0);
        for (int i = 0; i < temp.size(); i += 1) {
            if (!temp.get(i).isHigherThan(lowestNote)) {
                lowestNote = temp.get(i);
            }
        }
        return lowestNote;
    }

    /**
     * Returns a copy of all the notes in the piece.
     *
     * They don't have to be in any particular order.
     *
     * @return a copy of all the notes in the piece
     */
    @Override
    public List<Note> getNotes() {
        List<Note> temp = new ArrayList<Note>();
        for (Integer i : this.notes.keySet()) {
            for (Note n : this.notes.get(i)) {
                if (n.getAttack() == i) {
                    temp.add(n.copy());
                }
            }
        }
        return temp;
    }

    /**
     * Returns the first beat on which a note is played.
     * <p>
     * If no notes are in the piece, will return zero.
     *
     * @return the first beat of the piece that contains a note
     */
    @Override
    public int getStart() {
        int start = this.getEnd();

        for (Integer i : this.notes.keySet()) {
            if (i < start) {
                start = i;
            }
        }
        return start;
    }

    /**
     * Returns the beat number immediately following the last note in the piece.
     * <p>
     * If no notes are in the piece, will return zero.
     *
     * @return the first beat of rest that goes on forever
     */
    @Override
    public int getEnd() {
        int end = -1;

        for (Integer i : this.notes.keySet()) {
            if (i > end) {
                end = i;
            }
        }
        return end + 1;
    }

    /**
     * Resets the piece so that no notes are inside.
     */
    @Override
    public void resetPiece() {
        this.notes.clear();
        return;
    }


    /**
     * Returns a builder object for this cs3500.music.model.Piece.
     */
    public static CompositionBuilder<Piece> builder() {
        return new Builder();
    }


    /**
     * A builder for pieces.
     *
     * Defaults to a meter of 4/4, a tempo of 100000 beats per microsecond, and no notes.
     */
    public static final class Builder implements CompositionBuilder<Piece> {
        /** The tempo of the piece,

         /** The list of notes to be added. */
        List<Note> notes = new ArrayList<Note>();

        /** The tempo of the piece, in microseconds per beat. */
        int tempo = 100;

        /**
         * Constructs an actual composition, given the notes that have been added.
         * @return The new composition
         */
        public Piece build() {
            Piece p = new PieceImpl(4, this.tempo);
            for (Note n : notes) {
                p.addNote(n);
            }
            return p;
        }

        /**
         * Sets the tempo of the piece
         * @param tempo The speed, in microseconds per beat
         * @return This builder
         */
        public CompositionBuilder<Piece> setTempo(int tempo) {
            this.tempo = tempo;
            return this;
        }

        /**
         * Adds a new note to the piece
         * @param start The start time of the note, in beats
         * @param end The end time of the note, in beats
         * @param instrument The instrument number (to be interpreted by MIDI)
         * @param pitch The pitch (in the range [0, 127], where 60 represents C4)
         * @param volume The volume (in the range [0, 127])
         * @return
         */
        public CompositionBuilder<Piece> addNote(int start, int end, int instrument, int pitch,
                                                 int volume) {
            this.notes.add(new CoolNote(MusicUtils.midiNumberToPitchClass(pitch),
                    MusicUtils.midiNumberToOctave(pitch), end - start, start, volume,
                    instrument - 1));
            return this;
        }
    }


}

