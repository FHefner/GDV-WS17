package de.hsmannheim.util.unfoldingMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.markers.MarkerType;

import java.util.List;
import java.util.Map;

/**
 * Created by mjando on 13.12.17.
 */
public class UnfoldingMapUtil {
    public static UnfoldingMap addMarkersToUnfoldingMap(UnfoldingMap map, Map<MarkerType, List<Marker>> markers) {
        for (Map.Entry<MarkerType, List<Marker>> markerTypeListEntry : markers.entrySet())
            map.addMarkers(markerTypeListEntry.getValue());
        return map;
    }
}
