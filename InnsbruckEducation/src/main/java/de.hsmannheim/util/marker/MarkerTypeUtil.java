package de.hsmannheim.util.marker;

import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.InnsbruckEducationApp;
import de.hsmannheim.markers.ColorMarker;
import de.hsmannheim.markers.MarkerType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mjando on 13.12.17.
 */
public class MarkerTypeUtil {


    public static Map<MarkerType, List<Marker>> getAllMarkersForAllMarkerType(InnsbruckEducationApp app) {
        Map<MarkerType, List<Marker>> markers = new HashMap<>();

        for(MarkerType type : MarkerType.values())
            markers.put(type, getMarkerForMarkerType(app, type));

        return markers;
    }

    protected static List<Marker> getMarkerForMarkerType(InnsbruckEducationApp app, MarkerType type) {
        List<Marker> markerList = new ArrayList<>();
        Field objects = MarkerType.markerTypeToStrategy.get(type.name());
        try {
            for (ColorMarker entry : (List<ColorMarker>) objects.get(app)) {
                markerList.add(entry.getMarker());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return markerList;
    }
}