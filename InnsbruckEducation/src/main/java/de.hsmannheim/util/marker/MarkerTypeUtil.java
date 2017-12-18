package de.hsmannheim.util.marker;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.InnsbruckEducationApp;
import de.hsmannheim.markers.ColorMarker;
import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.UrbanDistrict;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mjando on 13.12.17.
 */
public class MarkerTypeUtil {

    private static List<MarkerType> markerTypesToShowInDistrict = new ArrayList<MarkerType>() {{
        add(MarkerType.UNIVERSITY_MARKER);
        add(MarkerType.SCHOOL_MARKER);
    }};


    public static Map<MarkerType, List<Marker>> getAllMarkersForAllMarkerType(InnsbruckEducationApp app) {
        Map<MarkerType, List<Marker>> markers = new HashMap<>();

        for (MarkerType type : MarkerType.values())
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

    public static void resetEducationMarkers(Map<MarkerType, List<Marker>> markers) {
        for (Map.Entry<MarkerType, List<Marker>> mapEntry : markers.entrySet()) {
            for (Marker marker : mapEntry.getValue()) {
                marker.setHidden(true);
            }
        }
    }

    public static void showEducationalInstitutionsInSelectedDistrict(Map<MarkerType, List<Marker>> markers, UnfoldingMap map, UrbanDistrict selectedDistrict) {
        for (MarkerType markerType : markerTypesToShowInDistrict)
            enableMarkersInSelectedDistrict(markerType, markers, map, selectedDistrict);
    }

    private static void enableMarkersInSelectedDistrict(MarkerType markerType, Map<MarkerType, List<Marker>> markers, UnfoldingMap map, UrbanDistrict selectedDistrict) {
        for (Marker marker : markers.get(markerType)) {
            float markerXPosition = MarkerScreenLocationUtil.getScreenXPositionFromMarker(map, marker);
            float markerYPosition = MarkerScreenLocationUtil.getScreenYPositionFromMarker(map, marker);
            if (selectedDistrict.getMarker().isInside(map, markerXPosition, markerYPosition)) {
                marker.setHidden(false);
            }
        }
    }
}
