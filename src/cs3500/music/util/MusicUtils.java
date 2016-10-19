package cs3500.music.util;

import java.util.*;

import cs3500.music.model.CoolNote;
import cs3500.music.model.Note;
import cs3500.music.model.PitchClass;

/**
 * Some static utilities for use in the Music Editor.
 */
public final class MusicUtils {

    /**
     * Converts a MIDI number of a pitch into a string
     *
     *
     * The MIDI number of a pitch is incremented by semitones, 60 being equivalent to C4, 48
     * being equivalent to C3, and so on.
     *
     * The string returned will favor naturals over accidentals and sharps over flats.
     *
     * @param pitch the MIDI number of the pitch
     * @return the string representation of the pitch
     */
    public static String midiNumberToString(int pitch) {
        int pitchClass = Math.abs(pitch % 12);
        int octave = ((pitch / 12) - 1);

        String result = "";

        switch(pitchClass) {
            case 0:
                result = "C";
                break;
            case 1:
                result = "C#";
                break;
            case 2:
                result = "D";
                break;
            case 3:
                result = "D#";
                break;
            case 4:
                result = "E";
                break;
            case 5:
                result = "F";
                break;
            case 6:
                result = "F#";
                break;
            case 7:
                result = "G";
                break;
            case 8:
                result = "G#";
                break;
            case 9:
                result = "A";
                break;
            case 10:
                result = "A#";
                break;
            case 11:
                result = "B";
                break;
            default:
                throw new IllegalArgumentException("Unreachable statement.");
        }

        return result + String.valueOf(octave);
    }


    /**
     * Returns the MIDI number of the pitch, given in terms of {@link PitchClass} and octave
     * number.
     *
     * The MIDI number of a pitch is incremented by semitones, 60 being equivalent to C4, 40
     * being equivalent to C3, and so on.
     *
     * @param p the pitch class that we want to convert
     * @param octave the octave number that we want to convert
     * @return the MIDI number of the pitch
     */
    public static int midiNumber(PitchClass p, int octave) {
        return p.value() + ((octave + 1) * 12);
    }

    /**
     * Returns the pitch class that corresponds to the given MIDI number.
     *
     * The MIDI number of a pitch is incremented by semitones, 60 being equivalent to C4, 48
     * being equivalent to C3, and so on.
     *
     * The pitch class returned will favor naturals over accidentals and sharps over flats.
     *
     * @param midiNumber the MIDI number to get the pitch class from
     * @return the pitch class of the MIDI number
     */
    public static PitchClass midiNumberToPitchClass(int midiNumber) {
        int pitchClass = Math.abs(midiNumber % 12);
        switch (pitchClass) {
            case 0:
                return PitchClass.C;
            case 1:
                return PitchClass.Cs;
            case 2:
                return PitchClass.D;
            case 3:
                return PitchClass.Ds;
            case 4:
                return PitchClass.E;
            case 5:
                return PitchClass.F;
            case 6:
                return PitchClass.Fs;
            case 7:
                return PitchClass.G;
            case 8:
                return PitchClass.Gs;
            case 9:
                return PitchClass.A;
            case 10:
                return PitchClass.As;
            case 11:
                return PitchClass.B;
            default:
                // shouldn't ever get here.
                throw new RuntimeException("Unreachable statement");
        }
    }

    /**
     * Returns the octave of the given MIDI number.
     *
     * The MIDI number of a pitch is incremented by semitones, 60 being equivalent to C4, 48
     * being equivalent to C3, and so on.
     *
     * @param midiNumber the MIDI number from which to determine the octave
     * @return the octave of the given MIDI number.
     */
    public static int midiNumberToOctave(int midiNumber) {
        return (midiNumber / 12) - 1;
    }

    /**
     * Determines if the given ArrayList contains any notes with the given pitch.
     *
     * @param notes the ArrayList to be searched
     * @param pitchClass the pitch class that we want to match
     * @param octave the octave that we want to match
     * @return if the given ArrayList contains any notes with the given pitch
     */
    public static boolean listContainsPitch(List<Note> notes,
                                            PitchClass pitchClass, int octave) {
        Note dummy = new CoolNote(pitchClass, octave, 1, 0, 0, 1);

        boolean result = false;
        for (int i = 0; i < notes.size(); i += 1) {
            result = result || notes.get(i).samePitch(dummy);
        }

        return result;

    }

}

