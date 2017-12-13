package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSpengelMapUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSprengelBasedStrategy;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class UrbanDistrict implements ColorMarker {


    protected Map<String, Integer> inhabitansBetween6And29;
    private PApplet applet;
    private int zaehlerSprengel = -1;
    private int regionNumber = -1;
    private String name = "undefined";
    private List<Location> locations;
    private ColoredPolygonMarker marker;
    private boolean isSelected;
    private Integer color;


    public UrbanDistrict(PApplet applet, int zaehlerSprengel, int amountHabitants6To9, int amountHabitants10To14, int amountHabitants15To19, int amountHabitants20To24, int amountHabitants25To29) {
        this.applet = applet;
        this.zaehlerSprengel = zaehlerSprengel;
        this.inhabitansBetween6And29 = new HashMap<>();
        this.inhabitansBetween6And29.put("amountInhabitants6To9", amountHabitants6To9);
        this.inhabitansBetween6And29.put("amountInhabitants10To14", amountHabitants10To14);
        this.inhabitansBetween6And29.put("amountInhabitants15To19", amountHabitants15To19);
        this.inhabitansBetween6And29.put("amountInhabitants20To24", amountHabitants20To24);
        this.inhabitansBetween6And29.put("amountInhabitants25To29", amountHabitants25To29);
        setMapZaehlerSprengelToRegionNumberAndName();
        extractLocations();
    }

    public Map<String, Integer> getInhabitansBetween6And29() {
        return this.inhabitansBetween6And29;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ColoredPolygonMarker getMarker() {
        return marker;
    }

    public List<Location> getLocationsFromJSONArray() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public int getRegionNumber() {
        return regionNumber;
    }

    public void setRegionNumber(int regionNumber) {
        this.regionNumber = regionNumber;
    }

    private void setMapZaehlerSprengelToRegionNumberAndName() {
        ZaehlerSpengelMapUtil.traverseOverTableAndSetResult(this, applet.loadTable(PathConfig.MAPPING_ZSPRL_CSV_PATH, "header"), new ZaehlerSprengelBasedStrategy());
    }

    private List<Location> getLocationsFromJSONArray(JSONArray coordinates) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            JSONArray coordinate = coordinates.getJSONArray(i);
            float lat = coordinate.getFloat(1);
            float lon = coordinate.getFloat(0);
            locations.add(new Location(lat, lon));
        }
        return locations;
    }

    private void extractLocations() {
        locations = new ArrayList<>();
        JSONArray districtJSON = applet.loadJSONArray(PathConfig.GEO_STADTTEILE_JSON_PATH);
        for (int i = 0; i < districtJSON.size(); i++) {
            if (regionNumberIsEqualToNR(districtJSON, i))
                locations.addAll(getLocationsFromJSONArray(getDistrictGeometry(districtJSON.getJSONObject(i))));
        }
    }

    private boolean regionNumberIsEqualToNR(JSONArray districtJSON, int index) {
        return districtJSON.getJSONObject(index).getJSONObject("properties").getInt("NR") == this.regionNumber;
    }

    private JSONArray getDistrictGeometry(JSONObject districtObject) {
        return districtObject.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
    }

    public void createPolygonMarker() {
        this.marker = new ColoredPolygonMarker(this.applet, this.locations, this.color);
    }

    public void calculateTotalInhabitants() {
        this.inhabitansBetween6And29.put("totalAmountInhabitants", calculateInhabitanSum());
    }

    protected int calculateInhabitanSum() {
        return sumAll(this.inhabitansBetween6And29.values());
    }

    protected int sumAll(Collection<Integer> values) {
        int result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result;
    }

    public int getZaehlerSprengel() {
        return zaehlerSprengel;
    }

    public void setZaehlerSprengel(int zaehlerSprengel) {
        this.zaehlerSprengel = zaehlerSprengel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(this.zaehlerSprengel);
        output.append(" ").append(this.name).append("\n");
        output.append("Einwohner 6-9: ").append(String.valueOf(inhabitansBetween6And29.get("amountInhabitants6To9"))).append("\n");
        output.append("Einwohner 10-14: ").append(String.valueOf(inhabitansBetween6And29.get("amountInhabitants10To14"))).append("\n");
        output.append("Einwohner 15-19: ").append(String.valueOf(inhabitansBetween6And29.get("amountInhabitants15To19"))).append("\n");
        output.append("Einwohner 20-24: ").append(String.valueOf(inhabitansBetween6And29.get("amountInhabitants20To24"))).append("\n");
        output.append("Einwohner 25-29: ").append(String.valueOf(inhabitansBetween6And29.get("amountInhabitants25To29"))).append("\n");
        output.append("Gesamteinwohner: ").append(String.valueOf(inhabitansBetween6And29.get("totalAmountInhabitants"))).append("\n");
        return output.toString();
    }

    public void addSpecificInhabitans(String specificInhabitansKey, int value) {
        this.getInhabitansBetween6And29().put(specificInhabitansKey, this.getInhabitansBetween6And29().get(specificInhabitansKey) + value);
    }
}
