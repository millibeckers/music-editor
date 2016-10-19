package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Listens for mouse input from the user.
 */
public final class MouseHandler implements MouseListener {
    /**
     * Stores all of the mouse/response combinations.
     *
     * The Integer value is the mouse event, as specified in the constants for the MouseEvent
     * class. The Runnable value is the lambda function that will be carried out for that key
     * event.
     */
    Map<Integer, Consumer<MouseEvent>> mouseResponse = new HashMap<Integer, Consumer<MouseEvent>>();

    public MouseHandler(Map<Integer, Consumer<MouseEvent>> mouseResponse) {
        this.mouseResponse = mouseResponse;
    }

    public MouseHandler() {
        this.mouseResponse = new HashMap<Integer, Consumer<MouseEvent>>();
    }
    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        if (mouseResponse.containsKey(e.getButton())) {
            mouseResponse.get(e.getButton()).accept(e);
        }
        return;
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
        // not needed.
        return;
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
        // not needed.
        return;
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        return;
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        return;
    }

    /**
     * Adds a key/response pair to the set of supported key actions.
     *
     * @param mouseCode the code of the key
     * @param response the response to the key
     * @throws IllegalArgumentException if there is already an entry for that key code.
     */
    public void addMouseResponse(int mouseCode, Consumer<MouseEvent> response) {
        if (this.mouseResponse.containsKey(mouseCode)) {
            throw new IllegalArgumentException("Mouse response is already defined.");
        }
        this.mouseResponse.put(mouseCode, response);
    }
}

