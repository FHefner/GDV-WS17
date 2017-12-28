package de.hsmannheim.util.marker;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.hsmannheim.InnsbruckEducationApp;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.models.education.AbstractEducationalInstitution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimplePointMarkerUtil {


    public static Map<Integer, Integer> markerDiameterMap = new HashMap<Integer, Integer>() {{
        put(0, 0);
        put(1, 10);
        put(2, 15);
        put(3, 20);
        put(4, 25);
        put(5, 30);
        put(6, 35);
        put(7, 50);
    }};


    public static List<Marker> generateMarkerWithDiameter(List<UrbanDistrict> allDistrictsList, InnsbruckEducationApp app) {
        List<Marker> simplePointMarkers = new LinkedList<>();
        for (UrbanDistrict district : allDistrictsList) {
            simplePointMarkers.add(generateMarker(getDiameter(district.getSchools()), district, app, 0));
            simplePointMarkers.add(generateMarker(getDiameter(district.getUniversities()), district, app, 20));
        }
        return simplePointMarkers;
    }

    private static SimplePointMarker generateMarker(Integer diameter, UrbanDistrict district, InnsbruckEducationApp app, Integer offset) {
        Location centerOfPolygon = GeoUtils.getEuclideanCentroid(district.getLocations());
        centerOfPolygon.x = centerOfPolygon.x - (centerOfPolygon.x / 100 * offset);
        SimplePointMarker marker = new SimplePointMarker(centerOfPolygon);
        marker.setStrokeColor(90);
        marker.setStrokeWeight(2);
        marker.setDiameter(diameter);
        marker.setColor(app.color(115, 134, 52));
        marker.setHidden(false);
        System.out.format("Breite: %f, Höhe: %f\n", marker.getLocation().getLat(), marker.getLocation().getLon());
        return marker;
    }

    protected static Integer getDiameter(List<AbstractEducationalInstitution> educationalInstitutions) {
        if (markerDiameterMap.get(educationalInstitutions.size()) != null)
            return markerDiameterMap.get(educationalInstitutions.size());
        else
            return 50;
    }
}
