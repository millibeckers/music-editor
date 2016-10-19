package cs3500.music.model;

/**
 * Represents the available pitch classes.
 *
 * Note: in music theory, a pitch class is a nonspecific pitch; it is just the letter name. Not
 * to be confused with 'class' in a java context.
 *
 */
public enum PitchClass {
    /**
     * Represents the different pitch classes. 's' Refers to sharp and 'b' refers to flat.
     *
     * The integer values are how many half-steps each note is above C of the same octave.
     * Enharmonic notes have the same value, so C# = Db = 1.
     *
     * Note that E# is equivalent to F, Fb is equivalent to E, Cb is equivalent to B, and B# is
     * equivalent to C. This is weird, but so is music. Accept it.
     *
     * The string values for the flat notes are represented with the codes which will render as the real flat symbol.
     *
     */
    C(0, "C"), Cs(1, "C#"), Db(1, "D\u266D"), D(2, "D"), Ds(3, "D#"), Eb(3, "E\u266D"),
    E(4, "E"), Es(5, "E#"), Fb(4, "F\u266D"), F(5, "F"), Fs(6, "F#"), Gb(6, "G\u266D"),
    G(7, "G"), Gs(8, "G#"), Ab(8, "A\u266D"), A(9, "A"), As(10, "A#"), Bb(10, "B\u266D"),
    B(11, "B"), Bs(12, "B#"), Cb(11, "C\u266D");

    private final int value;
    private final String name;

    PitchClass(int v, String n) {
        this.value = v;
        this.name = n;
    }

    public int value() {
        return this.value;
    }

    public String toString() {
        return this.name;
    }
}
