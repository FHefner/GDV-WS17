package de.hsmannheim.util.marker;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class MarkerScreenLocationUtil {

    public static float getScreenXPositionFromMarker(UnfoldingMap map, Marker marker) {
        ScreenPosition position = locationToScreenPosition(map, marker.getLocation());
        return position.x;
    }

    public static float getScreenYPositionFromMarker(UnfoldingMap map, Marker marker) {
        ScreenPosition position = locationToScreenPosition(map, marker.getLocation());
        return position.y;
    }

    private static ScreenPosition locationToScreenPosition (UnfoldingMap map, Location location) {
        return map.getScreenPosition(location);
    }
}
