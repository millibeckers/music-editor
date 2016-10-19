package cs3500.music.view;

import cs3500.music.view.CompositeView;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewFrame;
import cs3500.music.view.MIDIView;
import cs3500.music.view.View;

/**
 * Created by emilyjbeckers on 11/21/15.
 */
public class ViewFactory {
    /**
     * Creates a concrete view.
     *
     * Input must be one of:
     * <ul>
     *     <li>"console"</li>
     *     <li>"midi"</li>
     *     <li>"graphics"</li>
     *     <li>"composite"</li>
     * </ul>
     *
     * @param type the type of view to be constructed.
     * @return an instance of the appropriate concrete view
     * @throws IllegalArgumentException if the input is invalid
     */
    public static View viewFactory(String type) {
        switch(type) {
            case "console":
                return new ConsoleView(new StringBuilder());
            case "midi":
                return new MIDIView();
            case "graphics":
                return new GuiViewFrame();
            case "composite":
                return new CompositeView(new MIDIView(), new GuiViewFrame());
            default:
                throw new IllegalArgumentException("Not a valid view type: must be one of " +
                        "\"console\", \"midi\", \"graphics\", \"composite\".");
        }
    }

    /**
     * Creates a concrete GUI view.
     *
     * Input must be one of:
     * <ul>
     *     <li>"graphics"</li>
     *     <li>"composite"</li>
     * </ul>
     *
     * @param type the type of view to be constructed.
     * @return an instance of the appropriate concrete view
     * @throws IllegalArgumentException if the input is invalid
     */
    public static GuiView guiViewFactory(String type) {
        switch(type) {
            case "graphics":
                return new GuiViewFrame();
            case "composite":
                return new CompositeView(new MIDIView(), new GuiViewFrame());
            default:
                throw new IllegalArgumentException("Not a valid view type: must be one of "
                        + "\"graphics\", \"composite\".");
        }
    }

}

