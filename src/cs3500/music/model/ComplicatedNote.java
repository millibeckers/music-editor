package cs3500.music.model;

/**
 * Represents a note.
 */
public class ComplicatedNote {
    /**
     * Represents the pitch class of this note.
     */
    PitchClass pitchClass;
    /**
     * Represents the octave of this note.
     * A4 is A440
     */
    int octave;
    /**
     * Represents the duration of this note (in beats).
     * INVARIANT: this.duration > 0;
     */
    int duration;
    /**
     * Represents the starting beat of this note.
     * INVARIANT: this.attack >= 0;
     */
    int attack;

    /**
     * Constructs an instance of a {@code Note}.
     *
     * Throws an IllegalArgumentException if the duration is less than or equal to zero, or if
     * the attack is less than zero.
     *
     * @param pitchClass the pitch class of the note
     * @param octave the octave of the note
     * @param duration the duration of the note (in beats)
     * @param attack when the note begins (in beats)
     * @throws IllegalArgumentException if duration <= 0 or attack < 0
     */
    public ComplicatedNote(PitchClass pitchClass, int octave, int duration, int attack) {
        if (duration <= 0 || attack < 0) {
            throw new IllegalArgumentException("Invalid note timing.");
        }

        this.pitchClass = pitchClass;
        this.octave = octave;
        this.duration = duration;
        this.attack = attack;
    }

    /** Get the pitch class of this note. */
    public PitchClass getPitchClass() {
        return this.pitchClass;
    }

    /** Get the octave of this note. */
    public int getOctave() {
        return this.octave;
    }

    /** Get the duration of this note (in beats). */
    public int getDuration() {
        return this.duration;
    }

    /** Get the starting position of this note (in beats). */
    public int getAttack() {
        return this.attack;
    }

    /** Determines if the given note has the same pitch as this note.
     *
     * Same pitch is when there is the same pitch class and the same octave number.
     * Enharmonics with the same octave number (e.g. G#4 and Ab4) are considered the same pitch.
     *
     * B#, which is an enharmonic of C, would have an octave number one less than the C, for
     * example B#3 is equivalent to C4, and therefore they would have the same pitch. Likewise,
     * Cb4 is equivalent to B3. For more information regarding this case, see
     * <a href> https://en.wikipedia
     * .org/wiki/Scientific_pitch_notation#C-flat_and_B-sharp_issues</a>
     *
     * @param n the given note
     * @return whether the given note has the same pitch as this note
     */
    public boolean samePitch(ComplicatedNote n) {
        // Handle the Cb/B# cases
        if (this.pitchClass == PitchClass.Cb && n.getPitchClass() == PitchClass.B) {
            return this.octave - 1 == n.getOctave();
        }
        else if (this.pitchClass == PitchClass.B && n.getPitchClass() == PitchClass.Cb) {
            return this.octave == n.getOctave() - 1;
        }
        else if (this.pitchClass == PitchClass.Bs && n.getPitchClass() == PitchClass.C) {
            return this.octave == n.getOctave() - 1;
        }
        else if (this.pitchClass == PitchClass.C && n.getPitchClass() == PitchClass.Bs) {
            return this.octave - 1 == n.getOctave();
        }

        // Everything else
        else {
            return this.octave == n.getOctave()
                    && this.pitchClass.value() == n.getPitchClass().value();
        }
    }

    /**
     * Determines whether this note is higher than the given note.
     *
     * A note is higher if it has a higher octave number, or when they have the same octave
     * number it's closer to C of the next octave.
     *
     * B#4 is an octave higher than C4. Likewise, Cb4 is an octave lower than B4.
     *
     * @param n the given note
     * @return whether this note is higher than the given note
     */
    public boolean isHigherThan(ComplicatedNote n) {
        // Handle the Cb/B# cases
        if (this.pitchClass == PitchClass.Cb && n.getPitchClass() == PitchClass.B) {
            return this.octave - 1 > n.getOctave();
        }
        else if (this.pitchClass == PitchClass.B && n.getPitchClass() == PitchClass.Cb) {
            return this.octave > n.getOctave() - 1;
        }
        else if (this.pitchClass == PitchClass.Bs && n.getPitchClass() == PitchClass.C) {
            return this.octave > n.getOctave() - 1;
        }
        else if (this.pitchClass == PitchClass.C && n.getPitchClass() == PitchClass.Bs) {
            return this.octave - 1 > n.getOctave();
        }
        // everything else
        if (this.octave > n.getOctave()) {
            return true;
        }
        else if (this.octave < n.getOctave()) {
            return false;
        }
        else {
            return this.pitchClass.value() > n.getPitchClass().value();
        }
    }

    /**
     * Returns a copy of this note with all of the same values.
     * @return a copy of this note.
     */
    public ComplicatedNote copy() {
        return new ComplicatedNote(this.pitchClass, this.octave, this.duration, this.attack);
    }

    /** Returns the hashcode of the Note. */
    @Override
    public int hashCode() {
        return this.pitchClass.value() * this.octave;
    }

    /**
     * Determines if the given object is equal to this note.
     *
     * For two notes to be equal, they must have the same pitch (as determined by the
     * samePitch() method), the same duration, and the same starting time.
     *
     * @param obj the object to be compared for equality with this note
     * @return {@code true} if the given object is equal to this note
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComplicatedNote)) {
            return false;
        }
        ComplicatedNote n = (ComplicatedNote) obj;
        return this.samePitch(n) &&
                this.duration == n.getDuration() &&
                this.attack == n.getAttack();

    }

    /** Returns a string representation of this note. */
    @Override
    public String toString() {
        return this.pitchClass.toString() + this.octave;
    }
}
