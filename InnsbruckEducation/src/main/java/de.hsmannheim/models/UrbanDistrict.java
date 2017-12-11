package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSpengelMapUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSprengelBasedStrategy;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UrbanDistrict {

    private final static String GEO_JSON_PATH = "src/main/resources/data/statistischeStadtteile_headless.geo.json";
    private final static String MAPPING_CSV_PATH = "src/main/resources/data/zaehlersprengel_filtered.csv";
    public final static String CSV_DATA_PATH = "src/main/resources/data/bevoelkerung_2017.csv";
    private PApplet applet;
    private int zaehlerSprengel = -1;
    private int regionNumber = -1;
    private String name = "undefined";
    private int amountInhabitants6To9;
    private int amountInhabitants10To14;
    private int amountInhabitants15To19;
    private int amountInhabitants20To24;
    private int amountInhabitants25To29;
    private int totalAmountInhabitants;
    private List<Location> locations;
    private ColoredPolygonMarker marker;
    private  boolean isSelected;

    public boolean getIsSelected() {
        return isSelected;
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
        ZaehlerSpengelMapUtil.traverseOverTableAndSetResult(this, applet.loadTable(MAPPING_CSV_PATH, "header"), new ZaehlerSprengelBasedStrategy());
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
        JSONArray districtJSON = applet.loadJSONArray(GEO_JSON_PATH);
        for (int i = 0; i < districtJSON.size(); i++) {
            if (regionNumberIsEqualToNR(districtJSON,i))
                locations.addAll(getLocationsFromJSONArray(getDistrictGeometry(districtJSON.getJSONObject(i))));
        }
    }

    private boolean regionNumberIsEqualToNR(JSONArray districtJSON, int index) {
        return districtJSON.getJSONObject(index).getJSONObject("properties").getInt("NR") == this.regionNumber;
    }

    private JSONArray getDistrictGeometry(JSONObject districtObject) {
        return districtObject.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
    }

    private void createPolygonMarker() {
        this.marker = new ColoredPolygonMarker(this.applet, this.locations);
    }

    public int getTotalAmountInhabitants() {
        return totalAmountInhabitants;
    }

    public void calculateTotalInhabitants() {
        this.totalAmountInhabitants = this.amountInhabitants6To9 + this.amountInhabitants10To14
                + this.amountInhabitants15To19 + this.amountInhabitants20To24 + this.amountInhabitants25To29;
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

    public int getAmountInhabitants6To9() {
        return amountInhabitants6To9;
    }

    public void setAmountInhabitants6To9(int amountInhabitants6To9) {
        this.amountInhabitants6To9 = amountInhabitants6To9;
    }

    public int getAmountInhabitants10To14() {
        return amountInhabitants10To14;
    }

    public void setAmountInhabitants10To14(int amountInhabitants10To14) {
        this.amountInhabitants10To14 = amountInhabitants10To14;
    }

    public int getAmountInhabitants15To19() {
        return amountInhabitants15To19;
    }

    public void setAmountInhabitants15To19(int amountInhabitants15To19) {
        this.amountInhabitants15To19 = amountInhabitants15To19;
    }

    public int getAmountInhabitants20To24() {
        return amountInhabitants20To24;
    }

    public void setAmountInhabitants20To24(int amountInhabitants20To24) {
        this.amountInhabitants20To24 = amountInhabitants20To24;
    }

    public int getAmountInhabitants25To29() {
        return amountInhabitants25To29;
    }

    public void setAmountInhabitants25To29(int amountInhabitants25To29) {
        this.amountInhabitants25To29 = amountInhabitants25To29;
    }

    public UrbanDistrict(PApplet applet, int zaehlerSprengel, int amountHabitants6To9, int amountHabitants10To14, int amountHabitants15To19, int amountHabitants20To24, int amountHabitants25To29) {
        this.applet = applet;
        this.zaehlerSprengel = zaehlerSprengel;
        this.amountInhabitants6To9 = amountHabitants6To9;
        this.amountInhabitants10To14 = amountHabitants10To14;
        this.amountInhabitants15To19 = amountHabitants15To19;
        this.amountInhabitants20To24 = amountHabitants20To24;
        this.amountInhabitants25To29 = amountHabitants25To29;
        setMapZaehlerSprengelToRegionNumberAndName();
        extractLocations();
        createPolygonMarker();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(this.zaehlerSprengel);
        output.append(" ").append(this.name).append("\n");
        output.append("Einwohner 6-9: ").append(String.valueOf(this.amountInhabitants6To9)).append("\n");
        output.append("Einwohner 10-14: ").append(String.valueOf(this.amountInhabitants10To14)).append("\n");
        output.append("Einwohner 15-19: ").append(String.valueOf(this.amountInhabitants15To19)).append("\n");
        output.append("Einwohner 20-24: ").append(String.valueOf(this.amountInhabitants20To24)).append("\n");
        output.append("Einwohner 25-29: ").append(String.valueOf(this.amountInhabitants25To29)).append("\n");
        output.append("Gesamteinwohner: ").append(String.valueOf(this.totalAmountInhabitants)).append("\n");
        return output.toString();
    }
}
