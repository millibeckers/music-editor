package cs3500.music.model;

import java.util.Objects;

import cs3500.music.util.MusicUtils;

/**
 * Represents a note in the piece
 */
public final class CoolNote extends Note {
    /**
     * Represents the pitch of the note.
     *
     * C0 is equivalent to 0, and each semitone above it is an increment in the pitch number. In
     * order to find out what pitch you are referring to in scientific pitch notation, dividing
     * the number by 12 will yield the octave number. this.pitch % 12 will yield the number of
     * semitones the note is above C of that same octave.
     *
     * cs3500.music.model.Note that this is different from the MIDI convention, which specifies
     * that C4 is equivalent to 60, instead of this which would have C4 equivalent to 48.
     *
     * Can theoretically be any value but note that human hearing range only goes from
     * approximately E0 to E10.
     */
    int pitch;


    /**
     * Constructs an instance of a {@code cs3500.music.model.Note}.
     *
     * Throws an IllegalArgumentException if the duration is less than or equal to zero, if
     * the attack is less than zero, if velocity is not between 0 and 127, or if instrument is
     * not between 0 and 127.
     *
     * @param pitch the integer represenation of the pitch
     * @param duration the duration of the note (in beats)
     * @param attack when the note begins (in beats)
     * @param velocity the volume at which to play the note
     * @param instrument the instrument with which to play the note.
     * @throws IllegalArgumentException if invariants outlined above would be violated.
     */
    private CoolNote(int pitch, int duration, int attack, int velocity, int instrument) {
        super(duration, attack, velocity, instrument);

        this.pitch = pitch;
    }

    /**
     * Constructs an instance of a {@code cs3500.music.model.Note}.
     *
     * Throws an IllegalArgumentException if the duration is less than or equal to zero, if
     * the attack is less than zero, if pitchClass is null, velocity is not between 0 and 127,
     * or instrument is not between 0 and 127
     *
     * @param pitchClass the enum representation of the note
     * @param octave the octave of the note
     * @param duration the duration of the note (in beats)
     * @param attack when the note begins (in beats)
     * @param velocity the volume of the note
     * @param instrument the instrument to play the note
     * @throws IllegalArgumentException if any invariants outlined above would be broken.
     */
    public CoolNote(PitchClass pitchClass, int octave, int duration, int attack, int velocity,
                    int instrument) {
        this((12 * octave) + Objects.requireNonNull(pitchClass).value(), duration, attack,
                velocity, instrument);
    }

    /** Get the pitch of this note */
    @Override
    int getPitch() {
        return this.pitch;
    }

    /**
     * Get the pitch class of this note.
     *
     * The pitch class will be represented with a preference for naturals over accidentals and
     * sharps over flats.
     *
     * @return the pitch class of this note
     */
    @Override
    public PitchClass getPitchClass() {
        return MusicUtils.midiNumberToPitchClass(this.pitch + 12);
    }

    /** Gets the octave of this note */
    @Override
    public int getOctave() {
        return (this.pitch - this.getPitchClass().value()) / 12;
    }

    /**
     * Returns a copy of this note with all of the same values.
     *
     * @return a copy of this note.
     */
    public Note copy() {
        return new CoolNote(this.pitch, this.duration, this.attack, this.velocity,
                this.instrument);
    }

    /** Returns the hashcode of the cs3500.music.model.Note. */
    @Override
    public int hashCode() {
        return this.pitch;
    }


    /** Returns a string representation of this note. */
    @Override
    public String toString() {
        return MusicUtils.midiNumberToString(this.pitch + 12);
    }
}

