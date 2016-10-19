package cs3500.music.tests;

import cs3500.music.model.*;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the MIDI view, HW06.
 *
 * Assume that the model works as intended (it was tested more than thoroughly in HW05).
 */
public class ConsoleViewTests {

    private Piece blank, abc, amaj;
    private Note a3, a4, b4, c4, cs4, e4, d3, c5;
    private ConsoleView printer;
    private Appendable output;

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

        this.output = new StringBuilder();

        this.printer = new ConsoleView(output);
    }

    @Test
    public void smallTests() throws IOException {
        this.reset();

        this.printer.render(this.blank);
        assertEquals(this.output.toString(), " \n"); // one space

        this.reset();
        this.blank.addNote(new CoolNote(PitchClass.C, 4, 4, 98, 103, 0));
        this.printer.render(this.blank);
        assertEquals(this.output.toString(),
                "    C4\n" +
                        " 98 X \n" +
                        " 99 | \n" +
                        "100 | \n" +
                        "101 | \n");



        this.reset();
        this.printer.render(this.abc);
        assertEquals(this.output.toString(),
                "  A4A#4 B4 C5\n" +
                        "0 X          \n" +
                        "1 |          \n" +
                        "2       X    \n" +
                        "3       |    \n" +
                        "4          X \n" +
                        "5          | \n");

        this.reset();
        this.abc.addNote(this.a3);
        this.printer.render(this.abc);
        assertEquals(this.output.toString(),
                "  A3A#3 B3 C4C#4 D4D#4 E4 F4F#4 G4G#4 A4A#4 B4 C5\n" +
                        "0 X                                   X          \n" +
                        "1 |                                   |          \n" +
                        "2                                           X    \n" +
                        "3                                           |    \n" +
                        "4                                              X \n" +
                        "5                                              | \n");




        this.reset();
        this.printer.render(this.amaj);
        assertEquals(this.output.toString(),
                "  A3A#3 B3 C4C#4 D4D#4 E4\n" +
                        "0 X           X        X \n" +
                        "1 |           |        | \n");

        this.reset();
        this.amaj.addNote(this.c4);
        this.printer.render(this.amaj);
        assertEquals(this.output.toString(),
                "  A3A#3 B3 C4C#4 D4D#4 E4\n" +
                        "0 X           X        X \n" +
                        "1 |           |        | \n" +
                        "2                        \n" +
                        "3                        \n" +
                        "4                        \n" +
                        "5          X             \n" +
                        "6          |             \n" +
                        "7          |             \n");

        this.reset();
        this.amaj.addNote(this.c4);
        this.amaj.addNote(new CoolNote(PitchClass.C, 4, 4, 10, 103, 0));
        this.printer.render(this.amaj);
        assertEquals(this.output.toString(),
                "   A3A#3 B3 C4C#4 D4D#4 E4\n" +
                        " 0 X           X        X \n" +
                        " 1 |           |        | \n" +
                        " 2                        \n" +
                        " 3                        \n" +
                        " 4                        \n" +
                        " 5          X             \n" +
                        " 6          |             \n" +
                        " 7          |             \n" +
                        " 8                        \n" +
                        " 9                        \n" +
                        "10          X             \n" +
                        "11          |             \n" +
                        "12          |             \n" +
                        "13          |             \n");
    }

    @Test
    public void moreSmallTests() throws IOException {
        this.reset();
        this.printer.render(this.abc);
        String putput = this.output.toString();
        this.printer.pause();
        assertEquals(putput, this.output.toString()); // killing does nothing


        this.reset();
        this.printer.render(this.abc);
        putput = this.output.toString();
        this.reset();
        this.printer.render(0, this.abc);
        assertEquals(this.output.toString(), putput); // render(piece) does same as render(int)
        String outout = this.output.toString();
        this.reset();
        this.printer.render(3, this.abc);
        assertEquals(this.output.toString(), outout); // render(int) does same thing on any input

    }

    @Test
    public void maryTests() throws IOException {
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
        this.printer.render(mary);
        assertEquals(this.output.toString(),
                "   E3 F3F#3 G3G#3 A3A#3 B3 C4C#4 D4D#4 E4 F4F#4 G4\n" +
                " 0          X                          X          \n" +
                " 1          |                          |          \n" +
                " 2          |                    X                \n" +
                " 3          |                    |                \n" +
                " 4          |              X                      \n" +
                " 5          |              |                      \n" +
                " 6          |                    X                \n" +
                " 7                               |                \n" +
                " 8          X                          X          \n" +
                " 9          |                          |          \n" +
                "10          |                          X          \n" +
                "11          |                          |          \n" +
                "12          |                          X          \n" +
                "13          |                          |          \n" +
                "14          |                          |          \n" +
                "15                                                \n" +
                "16          X                    X                \n" +
                "17          |                    |                \n" +
                "18          |                    X                \n" +
                "19          |                    |                \n" +
                "20          |                    X                \n" +
                "21          |                    |                \n" +
                "22          |                    |                \n" +
                "23          |                    |                \n" +
                "24          X                          X          \n" +
                "25          |                          |          \n" +
                "26                                              X \n" +
                "27                                              | \n" +
                "28                                              X \n" +
                "29                                              | \n" +
                "30                                              | \n" +
                "31                                              | \n" +
                "32          X                          X          \n" +
                "33          |                          |          \n" +
                "34          |                    X                \n" +
                "35          |                    |                \n" +
                "36          |              X                      \n" +
                "37          |              |                      \n" +
                "38          |                    X                \n" +
                "39          |                    |                \n" +
                "40          X                          X          \n" +
                "41          |                          |          \n" +
                "42          |                          X          \n" +
                "43          |                          |          \n" +
                "44          |                          X          \n" +
                "45          |                          |          \n" +
                "46          |                          X          \n" +
                "47          |                          |          \n" +
                "48          X                    X                \n" +
                "49          |                    |                \n" +
                "50          |                    X                \n" +
                "51          |                    |                \n" +
                "52          |                          X          \n" +
                "53          |                          |          \n" +
                "54          |                    X                \n" +
                "55          |                    |                \n" +
                "56 X                       X                      \n" +
                "57 |                       |                      \n" +
                "58 |                       |                      \n" +
                "59 |                       |                      \n" +
                "60 |                       |                      \n" +
                "61 |                       |                      \n" +
                "62 |                       |                      \n" +
                "63 |                       |                      \n");
    }
}
