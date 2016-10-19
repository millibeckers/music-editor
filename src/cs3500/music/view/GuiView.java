package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.Piece;

/**
 * A GUI cs3500.music.view.View.
 */
public interface GuiView extends View {


    public int getLength();
    /**
     * Returns the size of the top margin.
     * @return the top margin size.
     */
    public int getTop();

    /**
     * Returns the size of the left margin.
     * @return the left margin size.
     */
    public int getSide();
    /**
     * Scrolls the composition vertically.
     */
    public boolean scrollY(int i);

    /**
     * Displays a default image for the piece.
     *
     * @param p the piece
     */
    public void renderDefault(Piece p);

    /**
     * Snaps the view to a position.
     *
     * @param beat the beat to snap to
     * @param p the piece
     */
    public void snap(int beat, Piece p);

    /**
     * Displays a status representing the editing mode that the user is currently in.
     * @param status the string representing the status of the piece.
     */
    public void setStatus(String status);

    /**
     * Sets the key listener for this view.
     *
     * @param keyListener the keyListener
     */
    public void setKeyListener(KeyListener keyListener);

    /**
     * Sets the mouse listener for this view.
     * @param mouseListener
     */
    public void setMouseListener(MouseListener mouseListener);
}

