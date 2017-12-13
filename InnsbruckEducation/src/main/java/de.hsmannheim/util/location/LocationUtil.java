package de.hsmannheim.util.location;

import de.fhpotsdam.unfolding.geo.Location;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mjando on 13.12.17.
 */
public class LocationUtil {

    protected static Collection<? extends Location> getLocationsFromJSONArray(JSONArray districtJSON, int index) {
        JSONObject districtObject = districtJSON.getJSONObject(index);
        JSONArray coordinates = districtObject.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);

        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            JSONArray coordinate = coordinates.getJSONArray(i);
            float lat = coordinate.getFloat(1);
            float lon = coordinate.getFloat(0);
            locations.add(new Location(lat, lon));
        }
        return locations;
    }

    public static List<Location> extractLocations(PApplet applet, UrbanDistrict district) {
        JSONArray districtJSON = applet.loadJSONArray(PathConfig.GEO_STADTTEILE_JSON_PATH);
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < districtJSON.size(); i++) {
            if (regionNumberIsEqualToNR(districtJSON, district,i))
                locations.addAll(getLocationsFromJSONArray(districtJSON, i));
        }
        return locations;
    }

    protected static boolean regionNumberIsEqualToNR(JSONArray districtJSON, UrbanDistrict district, int index) {
        return districtJSON.getJSONObject(index).getJSONObject("properties").getInt("NR") == district.getRegionNumber();
    }

}
