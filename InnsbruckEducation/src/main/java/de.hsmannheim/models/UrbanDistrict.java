package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.hsmannheim.markers.ColoredPolygonMarker;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.List;

public class UrbanDistrict {

    private final static String GEO_JSON_PATH = "data/statistischeStadtteile_headless.geo.json";
    private final static String MAPPING_CSV_PATH = "data/zaehlersprengel_filtered.csv";
    public final static String CSV_DATA_PATH = "data/bevoelkerung_2017.csv";
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

    public ColoredPolygonMarker getMarker() {
        return marker;
    }

    public List<Location> getLocations() {
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

    private void mapZaehlerSprengelToRegionNumberAndName() {
        Table mappingTable = applet.loadTable(MAPPING_CSV_PATH, "header");
        for (TableRow row : mappingTable.rows()) {
            String zsprlRange = row.getString("ZSPRL");
            if (!zsprlRange.contains("-")) {
                if (Integer.valueOf(zsprlRange) == this.zaehlerSprengel) {
                    this.regionNumber = row.getInt("BZR");
                    this.name = row.getString("Name");
                }
            }
            else {
                int startValue = Integer.valueOf(zsprlRange.split("-")[0]);
                int endValue = Integer.valueOf(zsprlRange.split("-")[1]);
                for (int i = startValue; i < (endValue + 1); i++) {
                    if (i == this.zaehlerSprengel) {
                        this.regionNumber = row.getInt("BZR");
                        this.name = row.getString("Name");
                    }
                }
            }
        }
    }

    private void splitCoordinatesIntoLocations(JSONArray coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            JSONArray coordinate = coordinates.getJSONArray(i);
            float lat = coordinate.getFloat(1);
            float lon = coordinate.getFloat(0);
            locations.add(new Location(lat, lon));
        }
    }

    private void extractLocations() {
        locations = new ArrayList<>();
        JSONArray districtJSON = applet.loadJSONArray(GEO_JSON_PATH);
        for (int i = 0; i < districtJSON.size(); i++) {
            JSONObject districtObject = districtJSON.getJSONObject(i);
            JSONObject districtProperties = districtObject.getJSONObject("properties");
            if (districtProperties.getInt("NR") == this.regionNumber) {
                JSONObject districtGeometry = districtObject.getJSONObject("geometry");
                JSONArray districtCoordinates = districtGeometry.getJSONArray("coordinates").getJSONArray(0);
                splitCoordinatesIntoLocations(districtCoordinates);
            }
        }
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
        mapZaehlerSprengelToRegionNumberAndName();
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
