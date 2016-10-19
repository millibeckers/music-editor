package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Listens for keyboard input from the user.
 */
public final class KeyboardHandler implements KeyListener {
    /**
     * Stores all of the key/response combinations.
     *
     * The Integer value is the key, as specified by the virtual key constants in the KeyEvent
     * class. The Runnable value is the lambda function that will be carried out for that key
     * event.
     */
    Map<Integer, Runnable> keyResponse = new HashMap<Integer, Runnable>();

    public KeyboardHandler(Map<Integer, Runnable> keyResponse) {
        this.keyResponse = keyResponse;
    }
    public KeyboardHandler() {
        this.keyResponse = new HashMap<Integer, Runnable>();
    }

    /**
     * Invoked when a key has been pressed.
     *
     * cs3500.music.model.Note that key pressed is different from key typed in that key pressed refers to the
     * literal keys on the keyboard, as opposed to the character that is produced.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (keyResponse.containsKey(e.getKeyCode())) {
            keyResponse.get(e.getKeyCode()).run();
            return;
        }
        // Key is not supported.
        return;
    }

    /**
     * Invoked when a key has been released.
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed.
        return;
    }

    /**
     * Invoked when a key has been typed.
     *
     * cs3500.music.model.Note that key typed is different from key released in that key typed refers to the actual
     * unicode character that is produced (for example, the combination shift + a would produce
     * a capital A as opposed to shift and then lowercase a).
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed.
        return;
    }

    /**
     * Adds a key/response pair to the set of supported key actions.
     *
     * @param keyCode the code of the key
     * @param response the response to the key
     * @throws IllegalArgumentException if there is already an entry for that key code.
     */
    public void addKeyResponse(int keyCode, Runnable response) {
        if (this.keyResponse.containsKey(keyCode)) {
            throw new IllegalArgumentException("Key response is already defined.");
        }
        this.keyResponse.put(keyCode, response);
    }
}

