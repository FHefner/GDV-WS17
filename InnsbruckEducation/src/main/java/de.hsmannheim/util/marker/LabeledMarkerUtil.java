package de.hsmannheim.util.marker;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.markers.LabeledMarker;

public class LabeledMarkerUtil {

    public static boolean isMarkerInsideLabeledMarker(UnfoldingMap map, Marker marker, LabeledMarker labeledMarker) {
        float markerXPosition = MarkerScreenLocationUtil.getScreenXPositionFromMarker(map, marker);
        float markerYPosition = MarkerScreenLocationUtil.getScreenYPositionFromMarker(map, marker);
        boolean insideXRange = (markerXPosition > labeledMarker.getBoxXStartPosition()) &&
                (markerXPosition < labeledMarker.getBoxXStartPosition() + labeledMarker.getBoxWidth());
        boolean insideYRange = (markerYPosition > labeledMarker.getBoxYStartPosition()) &&
                (markerYPosition < labeledMarker.getBoxYStartPosition() + labeledMarker.getBoxHeight());
        return (insideXRange && insideYRange);
    }
}
