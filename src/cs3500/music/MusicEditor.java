package cs3500.music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.Controller;
import cs3500.music.controller.ControllerImpl;
import cs3500.music.util.MusicReader;
import cs3500.music.model.Piece;
import cs3500.music.model.PieceImpl;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewFactory;


public class MusicEditor {
    public static void main(String[] args) throws IOException, InvalidMidiDataException {
        GuiView view = ViewFactory.guiViewFactory(args[1]); // the type of view.
        Piece piece;
        try {
            piece = MusicReader.parseFile(new FileReader(
                    new File(args[0])), PieceImpl.builder());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            piece = null;
        }

        Controller controller = new ControllerImpl(piece, view);
        controller.activate();
    }
}