package cs3500.music.model;

/**
 * Represents a note.
 */
public abstract class Note {

    /**
     * Represents the duration of this note (in beats).
     * INVARIANT: this.duration > 0.
     */
    int duration;
    /**
     * Represents the starting beat of this note.
     * INVARIANT: this.attack >= 0.
     */
    int attack;
    /**
     * Represents the volume of the note.
     * INVARIANT: 0 <= this.velocity < 128.
     */
    int velocity;
    /**
     * Represents the instrument to play this note, to be determined by general MIDI.
     * INVARIANT: 0 <= this.instrument < 128.
     */
    int instrument;



    /**
     * Constructs an instance of a {@code cs3500.music.model.Note}.
     *
     * Throws an IllegalArgumentException if the duration is less than or equal to zero, if
     * the attack is less than zero, if the velocity is not between zero and 127, or if the
     * instrument is not between zero and 127.
     *
     * Default modifier on purpose
     *
     * @param duration the duration of the note (in beats)
     * @param attack when the note begins (in beats)
     * @param velocity the volume of the note
     * @param instrument the instrument to play this note.
     * @throws IllegalArgumentException if any arguments would break the invariants.
     */
    Note(int duration, int attack, int velocity, int instrument) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
        if (attack < 0) {
            throw new IllegalArgumentException("Start must be positive.");
        }
        if (velocity < 0 || velocity > 127) {
            throw new IllegalArgumentException("velocity must be between 0 and 127.");
        }
        if (instrument < 0 || instrument > 127) {
            throw new IllegalArgumentException("instrument must be between 0 and 127.");
        }
        this.duration = duration;
        this.attack = attack;
        this.velocity = velocity;
        this.instrument = instrument;

    }

    /**
     * Gets an integer representation of this cs3500.music.model.Note.
     *
     * Integer values correspond to MIDI numbers, such that C4 corresponds to 60.
     *
     * @return the integer representation of the pitch.
     */
    abstract int getPitch();

    /** Gets the pitch class of this note. */
    public abstract PitchClass getPitchClass();

    /** Gets the octave of this note */
    public abstract int getOctave();

    /** Get the duration of this note (in beats). */
    public int getDuration() {
        return this.duration;
    }

    /** Get the starting position of this note (in beats). */
    public int getAttack() {
        return this.attack;
    }

    /** Get the velocity of this note. */
    public int getVelocity() {
        return this.velocity;
    }

    /** Get the instrument of this note. */
    public int getInstrument() {
        return this.instrument;
    }

    /** Determines if the given note has the same pitch as this note.
     *
     * @param n the given note
     * @return whether the given note has the same pitch as this note
     */
    public boolean samePitch(Note n) {
        return n.getPitch() == this.getPitch();
    }

    /**
     * Determines whether this note is higher than the given note.
     *
     * @param n the given note
     * @return whether this note is higher than the given note
     */
    public boolean isHigherThan(Note n) {
        return this.getPitch() > n.getPitch();
    }

    /**
     * Returns a copy of this note with all of the same values.
     *
     * @return a copy of this note.
     */
    public abstract Note copy();


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
        if (!(obj instanceof Note)) {
            return false;
        }
        Note n = (Note) obj;
        return this.samePitch(n) &&
                this.duration == n.getDuration() &&
                this.attack == n.getAttack() &&
                this.velocity == n.getVelocity() &&
                this.instrument == n.getInstrument();

    }

    /** Returns the hashcode of the cs3500.music.model.Note. */
    @Override
    public abstract int hashCode();

}

