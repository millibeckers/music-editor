package cs3500.music.tests;

import cs3500.music.model.*;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.MIDIView;
import cs3500.music.view.View;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Tests for the MIDI view, HW06.
 *
 * Assume that the model works as intended (it was tested more than thoroughly in HW05).
 */
public final class MIDIViewTests {

    private Piece blank, abc, amaj;
    private Note a3, a4, b4, c4, cs4, e4, d3, c5;
    private MIDIView player, printer;
    private Receiver rec;

    private void reset() {
        this.a3 = new CoolNote(PitchClass.A, 3, 2, 0, 103, 0);
        this.a4 = new CoolNote(PitchClass.A, 4, 2, 0, 103, 0);
        this.b4 = new CoolNote(PitchClass.B, 4, 2, 2, 103, 0);
        this.c5 = new CoolNote(PitchClass.C, 5, 2, 4, 103, 0);
        this.cs4 = new CoolNote(PitchClass.Cs, 4, 2, 0, 103, 0);
        this.e4 = new CoolNote(PitchClass.E, 4, 2, 0, 103, 0);
        this.d3 = new CoolNote(PitchClass.D, 3, 1, 0, 103, 0);
        this.c4 = new CoolNote(PitchClass.C, 4, 3, 5, 103, 0);

        this.blank = new PieceImpl(3, 200000);
        this.abc = new PieceImpl(4, 200000, this.a4, this.b4, this.c5);
        this.amaj = new PieceImpl(2, 200000, this.a3, this.cs4, this.e4);

        this.rec = new Interceptor();

        this.player = new MIDIView();
        this.printer = new MIDIView(this.rec);
    }

    @Test
    public void shortTests() throws InvalidMidiDataException, InterruptedException {
        this.reset();

        for (int i = 0; i < 7; i += 1) {
            this.printer.render(i, this.abc);
            Thread.sleep(200);
        }
        assertEquals(this.rec.toString(), "Beat: 0. NOTE ON.  note 69, velocity 103\n" +
                "Beat: 2. NOTE OFF. note 69\n" +
                "Beat: 2. NOTE ON.  note 71, velocity 103\n" +
                "Beat: 4. NOTE OFF. note 71\n" +
                "Beat: 4. NOTE ON.  note 72, velocity 103\n" +
                "Beat: 6. NOTE OFF. note 72\n");

        this.reset();

        for (int i = 0; i < 3; i += 1) {
            this.printer.render(i, this.abc);
            Thread.sleep(200);
        }
        Thread.sleep(200);
        this.printer.pause();

        assertEquals(this.rec.toString(),
                "Beat: 0. NOTE ON.  note 69, velocity 103\n" +
                "Beat: 2. NOTE OFF. note 69\n" +
                "Beat: 2. NOTE ON.  note 71, velocity 103\n" +
                "ALL NOTES OFF \n");


        this.reset();
        this.printer.render(this.amaj);
        assertEquals(this.rec.toString(),
                "Beat: 0. NOTE ON.  note 57, velocity 103\n" +
                        "Beat: 0. NOTE ON.  note 61, velocity 103\n" +
                        "Beat: 0. NOTE ON.  note 64, velocity 103\n" +
                        "Beat: 2. NOTE OFF. note 57\n" +
                        "Beat: 2. NOTE OFF. note 61\n" +
                        "Beat: 2. NOTE OFF. note 64\n");
    }

    @Test
    public void testMaryPlayback() throws InvalidMidiDataException {
        this.reset();
        Piece mary;
        try {
            mary = MusicReader.parseFile(new FileReader(
                    new File("mary.txt")), PieceImpl.builder());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            mary = null;
        }

        //this.player.render(mary);
        this.printer.render(mary);
        System.out.println(this.rec.toString());
        assertEquals(this.rec.toString(),
                "Beat: 0. NOTE ON.  note 64, velocity 72\n" +
                "Beat: 0. NOTE ON.  note 55, velocity 70\n" +
                "Beat: 2. NOTE OFF. note 64\n" +
                "Beat: 2. NOTE ON.  note 62, velocity 72\n" +
                "Beat: 4. NOTE OFF. note 62\n" +
                "Beat: 4. NOTE ON.  note 60, velocity 71\n" +
                "Beat: 6. NOTE OFF. note 60\n" +
                "Beat: 6. NOTE ON.  note 62, velocity 79\n" +
                "Beat: 7. NOTE OFF. note 55\n" +
                "Beat: 8. NOTE OFF. note 62\n" +
                "Beat: 8. NOTE ON.  note 55, velocity 79\n" +
                "Beat: 8. NOTE ON.  note 64, velocity 85\n" +
                "Beat: 10. NOTE OFF. note 64\n" +
                "Beat: 10. NOTE ON.  note 64, velocity 78\n" +
                "Beat: 12. NOTE OFF. note 64\n" +
                "Beat: 12. NOTE ON.  note 64, velocity 74\n" +
                "Beat: 15. NOTE OFF. note 55\n" +
                "Beat: 15. NOTE OFF. note 64\n" +
                "Beat: 16. NOTE ON.  note 55, velocity 77\n" +
                "Beat: 16. NOTE ON.  note 62, velocity 75\n" +
                "Beat: 18. NOTE OFF. note 62\n" +
                "Beat: 18. NOTE ON.  note 62, velocity 77\n" +
                "Beat: 20. NOTE OFF. note 62\n" +
                "Beat: 20. NOTE ON.  note 62, velocity 75\n" +
                "Beat: 24. NOTE OFF. note 55\n" +
                "Beat: 24. NOTE OFF. note 62\n" +
                "Beat: 24. NOTE ON.  note 55, velocity 79\n" +
                "Beat: 24. NOTE ON.  note 64, velocity 82\n" +
                "Beat: 26. NOTE OFF. note 55\n" +
                "Beat: 26. NOTE OFF. note 64\n" +
                "Beat: 26. NOTE ON.  note 67, velocity 84\n" +
                "Beat: 28. NOTE OFF. note 67\n" +
                "Beat: 28. NOTE ON.  note 67, velocity 75\n" +
                "Beat: 32. NOTE OFF. note 67\n" +
                "Beat: 32. NOTE ON.  note 55, velocity 78\n" +
                "Beat: 32. NOTE ON.  note 64, velocity 73\n" +
                "Beat: 34. NOTE OFF. note 64\n" +
                "Beat: 34. NOTE ON.  note 62, velocity 69\n" +
                "Beat: 36. NOTE OFF. note 62\n" +
                "Beat: 36. NOTE ON.  note 60, velocity 71\n" +
                "Beat: 38. NOTE OFF. note 60\n" +
                "Beat: 38. NOTE ON.  note 62, velocity 80\n" +
                "Beat: 40. NOTE OFF. note 55\n" +
                "Beat: 40. NOTE OFF. note 62\n" +
                "Beat: 40. NOTE ON.  note 55, velocity 79\n" +
                "Beat: 40. NOTE ON.  note 64, velocity 84\n" +
                "Beat: 42. NOTE OFF. note 64\n" +
                "Beat: 42. NOTE ON.  note 64, velocity 76\n" +
                "Beat: 44. NOTE OFF. note 64\n" +
                "Beat: 44. NOTE ON.  note 64, velocity 74\n" +
                "Beat: 46. NOTE OFF. note 64\n" +
                "Beat: 46. NOTE ON.  note 64, velocity 77\n" +
                "Beat: 48. NOTE OFF. note 55\n" +
                "Beat: 48. NOTE OFF. note 64\n" +
                "Beat: 48. NOTE ON.  note 55, velocity 78\n" +
                "Beat: 48. NOTE ON.  note 62, velocity 75\n" +
                "Beat: 50. NOTE OFF. note 62\n" +
                "Beat: 50. NOTE ON.  note 62, velocity 74\n" +
                "Beat: 52. NOTE OFF. note 62\n" +
                "Beat: 52. NOTE ON.  note 64, velocity 81\n" +
                "Beat: 54. NOTE OFF. note 64\n" +
                "Beat: 54. NOTE ON.  note 62, velocity 70\n" +
                "Beat: 56. NOTE OFF. note 55\n" +
                "Beat: 56. NOTE OFF. note 62\n" +
                "Beat: 56. NOTE ON.  note 52, velocity 72\n" +
                "Beat: 56. NOTE ON.  note 60, velocity 73\n" +
                "Beat: 64. NOTE OFF. note 52\n" +
                "Beat: 64. NOTE OFF. note 60\n");
    }

    @Test
    public void testMystery1() throws InvalidMidiDataException {
        this.reset();
        Piece mystery1;
        try {
            mystery1 = MusicReader.parseFile(new FileReader(
                    new File("mystery-1.txt")), PieceImpl.builder());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            mystery1 = null;
        }

        //this.player.render(mystery1);
        // It's the Mario theme song!!!

        this.printer.render(mystery1);
        //System.out.println(this.rec);
        // I had an assertion for this but the result string was too long for java to handle lol

    }

    @Test
    public void testMystery2() throws InvalidMidiDataException {
        this.reset();
        Piece mystery2;
        try {
            mystery2 = MusicReader.parseFile(new FileReader(
                    new File("mystery-2.txt")), PieceImpl.builder());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            mystery2 = null;
        }

        // Tetris!

        //this.player.render(mystery2);
        this.printer.render(mystery2);
        //System.out.println(this.rec);

    }
}
