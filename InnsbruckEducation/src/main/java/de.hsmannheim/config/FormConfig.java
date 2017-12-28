package de.hsmannheim.config;

/**
 * Created by mjando on 12.12.17.
 */
public class FormConfig {

    public final static String WINDOW_NAME = "Innsbruck Education";
    public final static String VERSION = "0.1a";

    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;

    public final static int SIDE_PANEL_WIDTH = 420;
    public final static int SIDE_PANEL_HEIGHT = WINDOW_HEIGHT;

    public final static float MAP_X_WINDOW_OFFSET = 0;
    public final static float MAP_Y_WINDOW_OFFSET = 0;
    public final static float MAP_WIDTH = WINDOW_WIDTH - MAP_X_WINDOW_OFFSET - SIDE_PANEL_WIDTH;
    public final static float MAP_HEIGHT = WINDOW_HEIGHT - MAP_Y_WINDOW_OFFSET;

    public final static float XStartLocation =47.286526f;
    public final static float YStartLocation =11.388446f;
}
