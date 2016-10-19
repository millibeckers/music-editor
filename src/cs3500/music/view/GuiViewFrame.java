package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.Piece;
import cs3500.music.util.MusicUtils;
import cs3500.music.view.GuiPanel;
import cs3500.music.view.GuiView;

import static java.util.Objects.requireNonNull;

public final class GuiViewFrame extends javax.swing.JFrame implements GuiView {

    // public constants that determine the cell size and margin of the piece
    // INVARIANTS: The margin must be even and a multiple of CELL_SIZE.
    // Both the CELL_SIZE and MARGIN must be greater than zero.
    public final static int CELL = 15;
    public final static int BUFFER = CELL * 2;
    // the panel that everything is drawn in
    private GuiPanel displayPanel;
    // the length and width of the frame
    private int length;
    private int width;
    private String status;

    /**
     * Creates a default cs3500.music.view.GuiView with nothing rendered.
     * The displayPanel is set to a blank panel.
     * The length and width are set to be 1000 x 1000.
     *
     * INVARIANTS:
     * the length and width must be greater than zero.
     */
    public GuiViewFrame() {
        this.displayPanel = new GuiPanel();
        this.length = 1000;
        this.width = 1000;
        this.status = "";
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.getContentPane().add(this.displayPanel);
    }

    /**
     * Renders the piece in its entirety, beginning at the first beat in the song.
     * @throws NullPointerException if the given input is null
     * This method is deprecated with the addition of the controller.
     */
    @Deprecated
    public void render(Piece p) {
        p = requireNonNull(p);
        this.render(p.getStart(), p);
    }

    /**
     * Renders the piece from the given beat on (to the end of the piece),
     * initializes the frame to fit the song, and sets the visibility to true.
     * @param beat the beat to start rendering at
     * @throws NullPointerException if the given input is null
     * @throws IllegalArgumentException if the beat is negative
     */
    public void render(int beat, Piece p) {
        if(beat < 0) {
            throw new IllegalArgumentException("The beat cannot be negative");
        }
        p = requireNonNull(p);

        // initializes the length and the width of the frame.
        this.length = Math.min(p.getEnd() * CELL + (BUFFER * 2), this.length);
        this.width = Math.min((MusicUtils.midiNumber(p.getHighest().getPitchClass(),
                p.getHighest().getOctave()) - MusicUtils.midiNumber(p.getLowest().getPitchClass(),
                p.getLowest().getOctave())) * this.CELL + BUFFER * 3, this.width);

        // number of beats per screen
        int beatCells = (this.length - BUFFER) / CELL;
        int frameZone = beat / beatCells;
        this.displayPanel.initialize(p, beat, frameZone * beatCells, (frameZone + 1) *
                beatCells, this.status);
        this.pack();
        this.repaint();
    }

    /**
     * Turns the frame on.
     */
    public void initialize() {
        this.pack();
        this.setVisible(true);
    }

    /**
     * Stops rendering animation for the piece, pause..
     */
    public void pause() {
        return;
    }

    /**
     * Gets the length of the window.
     * @return The length of the window.
     */
    @Override
    public int getLength() {
        return this.length;
    }

    /**
     * Returns the size of the top margin.
     * @return the top margin size.
     */
    @Override
    public int getTop() {
        return this.getInsets().top;
    }

    /**
     * Returns the size of the left margin.
     *
     * @return the left margin size.
     */
    @Override
    public int getSide() {
        return this.getInsets().left;
    }

    /**
     * Scrolls the composition vertically.
     * @param i the number by which to scroll by.
     * @throws IllegalArgumentException if |i| != 1.
     */
    @Override
    public boolean scrollY(int i) {
        if(Math.abs(i) != 1) {
            throw new IllegalArgumentException("The y-scroller must be either -1 or 1.");
        }
        return this.displayPanel.scrollY(i, this.width);
    }

    /**
     * Displays a default image for the piece.
     *
     * Renders the piece for beat zero.
     *
     * @param p the piece
     * @throws NullPointerException if the piece is null.
     */
    public void renderDefault(Piece p) {
        p = requireNonNull(p);
        this.render(0, p);
    }

    /**
     * Snaps the view to a certain position.
     *
     * @param beat the beat to which to snap
     * @param p the piece
     */
    @Override
    public void snap(int beat, Piece p) {
        this.render(beat, p);
    }

    /**
     * Displays a status representing the editing mode that the user is currently in.
     *
     * @param status the string representing the status of the piece.
     */
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the key listener for the GUI view.
     *
     * @param keyListener the key listener
     */
    @Override
    public void setKeyListener(KeyListener keyListener) {
        this.addKeyListener(keyListener);
    }

    /**
     * Sets the mouse listener for the GUI view.
     * @param mouseListener the mouse listener
     */
    @Override
    public void setMouseListener(MouseListener mouseListener) { this.addMouseListener(mouseListener);}
    /**
     * Sets the size of the frame to be proportional to the size of the music piece
     * @return the {@code Dimension} of the frame
     */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(this.length, this.width);
    }

}




