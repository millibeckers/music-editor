package cs3500.music.model;

import java.util.List;

import cs3500.music.model.Note;

/**
 * Represents a piece of music.
 */
public interface Piece {

    /**
     * Returns the number of beats per each measure.
     * <p>
     * It is assumed that there are no meter changes in the piece. It is also assumed that the
     * bottom number of the time signature is 4.
     *
     * @return the number of beats per measure
     */
    int beatsPerMeasure();

    /**
     * Returns the tempo of the piece, given in beats per microsecond.
     *
     * @return the tempo of the piece, given in beats per microsecond.
     */
    int getTempo();

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
    void addNote(Note note);

    /**
     * Removes the given note from the piece.
     * <p>
     * Throws an IllegalArgumentException if the given note does not exist.
     *
     * @param note the note to be removed
     */
    void removeNote(Note note);

    /**
     * Gets a copy of the notes starting at the given beat.
     * <p>
     * If no notes at that beat, will return an empty list.
     *
     * @param beat the beat number the notes we want are at
     * @return a list of the notes starting at the given beat
     */
    List<Note> getNotesStartingAt(int beat);

    /**
     * Gets a copy of all notes sounding at the given beat.
     * <p>
     * If no notes are at the given beat, will return an empty list.
     *
     * @param beat the beat number the notes we want are at
     * @return a list of all notes sounding at the given beat.
     */
    List<Note> getAllNotesAt(int beat);

    /**
     * Gets a copy of all notes that are sustained over the given beat.
     *
     * A note that is sustained over a beat is a note that is sounding for that beat but is not
     * starting there.
     *
     * If no applicable notes at that beat, will return an empty list.
     *
     * @param beat the beat number of the notes we want
     * @return a list of all notes sustaned through the given beat
     */
    List<Note> getNotesSustainedAt(int beat);

    /**
     * Gets a copy of the highest note in the piece.
     * <p>
     * The highest note is the one with the highest octave number and the pitch class closest to
     * B of that octave number.
     *
     * @return a copy of the highest note in the piece
     */
    Note getHighest();

    /**
     * Gets a copy of the lowest note in the piece.
     * <p>
     * The lowest note is the one with the lowest octave number and the pitch class closest to C
     * of that octave number.
     *
     * @return a copy of the lowest note in the piece
     */
    Note getLowest();

    /**
     * Returns a copy of all the notes in the piece.
     * <p>
     * They don't have to be in any particular order.
     *
     * @return a copy of all the notes in the piece
     */
    List<Note> getNotes();

    /**
     * Returns the first beat on which a note is played.
     * <p>
     * If no notes are in the piece, will return zero.
     *
     * @return the first beat of the piece that contains a note
     */
    int getStart();

    /**
     * Returns the beat number immediately following the last note in the piece.
     * <p>
     * If no notes are in the piece, will return zero.
     *
     * @return the first beat of rest that goes on forever
     */
    int getEnd();

    /**
     * Resets the piece so that no notes are inside.
     */
    void resetPiece();

}