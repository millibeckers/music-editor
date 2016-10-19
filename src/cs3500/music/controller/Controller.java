
package cs3500.music.controller;

import java.util.function.Consumer;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.Note;

/**
 * A controller for the music editor.
 */
public interface Controller {

    /**
     * Starts the program.
     */
    public void activate() throws InvalidMidiDataException;

    /**
     * Adds a lambda expression to the controller dealing with key input
     */
    public void addKeyResponse(int key, Runnable runnable);

    /**
     * Adds a lambda expression to the controller dealing with mouse input
     */
    public void addMouseResponse(int key, Consumer consumer);

    /**
     * Gets the key handler for the controller.
     * @return
     */
    public KeyboardHandler getKeyHandler();

    /**
     * Gets the mouse handler for the controller.
     * @return
     */
    public MouseHandler getMouseHandler();

    /**
     * Sets the mouse handler for the controller.
     */
    public void setMouseHandler(MouseHandler m);

    /**
     * Sets the key handler for the controller.
     */
    public void setKeyHandler(KeyboardHandler k);

    /**
     * Gets the current beat.
     * @return the current beat
     */
    public int getCurrentBeat();

    /**
     * Returns whether the piece is playing.
     * @return whether the piece is playing.
     */
    public boolean isPlaying();

    /**
     * Returns the highest pitch.
     */
    public int getHighest();

    /** Returns the selected note.*/
    public Note getSelectedNote();

}

