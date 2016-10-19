package cs3500.music.tests;

import cs3500.music.model.CoolNote;
import cs3500.music.model.Piece;
import cs3500.music.model.PieceImpl;
import cs3500.music.model.PitchClass;
import cs3500.music.view.GuiViewFrame;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

/**
 * Tests for the GUI view. Note that comprehensive testing of the GUI view, these tests are all
 * just visual spot-check.
 */
public class GuiViewTest {
    @Test
    public void testGuiView() throws IOException, InvalidMidiDataException {
        Piece p = new PieceImpl(4, 60, new CoolNote(PitchClass.B, 5, 2, 5, 6, 1),
                new CoolNote(PitchClass.B, 6, 4, 8, 6, 1));
        GuiViewFrame view = new GuiViewFrame();

        for (int i = 0; i < p.getEnd(); i += 1) {
            //view.repaint();
            view.initialize();
            view.render(i, p);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
