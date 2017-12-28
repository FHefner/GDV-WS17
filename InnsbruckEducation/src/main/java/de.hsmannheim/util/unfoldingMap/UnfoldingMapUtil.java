package de.hsmannheim.util.unfoldingMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.markers.MarkerType;

import java.util.List;
import java.util.Map;

/**
 * Created by mjando on 13.12.17.
 */
public class UnfoldingMapUtil {

    private static Location startingLocation = new Location(FormConfig.XStartLocation, FormConfig.YStartLocation);


    public static UnfoldingMap addMarkersToUnfoldingMap(UnfoldingMap map, Map<MarkerType, List<Marker>> markers) {
        map.getMarkerManager(0).clearMarkers();
        map.addMarkers(markers.get(MarkerType.DISTRICT_MARKER));
        map.addMarkers(markers.get(MarkerType.SCHOOL_MARKER));
        map.addMarkers(markers.get(MarkerType.UNIVERSITY_MARKER));
        return map;
    }


    public static void setPropertiesToMap(UnfoldingMap map) {
        map.zoomAndPanTo(12, startingLocation);
        map.setZoomRange(10, 20);
        map.setPanningRestriction(startingLocation, 10);
    }
}
