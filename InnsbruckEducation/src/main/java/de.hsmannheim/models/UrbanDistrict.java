package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.ColorMarker;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.location.LocationUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSpengelMapUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSprengelBasedStrategy;
import processing.core.PApplet;
import processing.data.TableRow;

import java.util.*;

public class UrbanDistrict implements ColorMarker {


    protected Map<String, Integer> inhabitansBetween6And29 = new HashMap<>();
    private PApplet applet;
    private int zaehlerSprengel = -1;
    private int regionNumber = -1;
    private String name = "undefined";
    private List<Location> locations;
    private ColoredPolygonMarker marker;
    private boolean isSelected;
    private Integer color;

    private UrbanDistrict() {

    }

    public static UrbanDistrict buildDefaultDistrict(PApplet applet, TableRow row) {
        return new UrbanDistrict()
                .withApplet(applet)
                .withZaehlerSprengel(row.getInt("ZSPR"))
                .withKeyValuePairs(new LinkedHashMap<String, String>() {{
                    put("amountInhabitants6To9", "6_9");
                    put("amountInhabitants10To14", "10_14");
                    put("amountInhabitants15To19", "15_19");
                    put("amountInhabitants20To24", "20_24");
                    put("amountInhabitants25To29", "25_29");
                }}, row)
                .withZaehlerSprengelToRegionNumberAndName()
                .withLocations(applet);

    }

    private UrbanDistrict withZaehlerSprengel(int zspr) {
        this.zaehlerSprengel = zspr;
        return this;
    }

    private UrbanDistrict withApplet(PApplet applet) {
        this.applet = applet;
        return this;
    }

    private UrbanDistrict withKeyValuePairs(LinkedHashMap<String, String> inhabitantsMap, TableRow row) {
        for (Map.Entry<String, String> entry : inhabitantsMap.entrySet()) {
            this.inhabitansBetween6And29.put(entry.getKey(), row.getInt(entry.getValue()));
        }
        return this;
    }

    private UrbanDistrict withZaehlerSprengelToRegionNumberAndName() {
        ZaehlerSpengelMapUtil.traverseOverTableAndSetResult(this, applet.loadTable(PathConfig.MAPPING_ZSPRL_CSV_PATH, "header"), new ZaehlerSprengelBasedStrategy());
        return this;
    }

    private UrbanDistrict withLocations(PApplet applet) {
        locations = LocationUtil.extractLocations(applet, this);
        return this;
    }

    public void createPolygonMarker() {
        this.marker = new ColoredPolygonMarker(this.applet, this.locations, this.color);
    }

    public void calculateTotalInhabitants() {
        this.inhabitansBetween6And29.put("totalAmountInhabitants", DistrictUtil.calculateInhabitantsSum(this));
    }


    @Override
    public String toString() {
        return String.valueOf(this.zaehlerSprengel) +
                " " + this.name + "\n" +
                "Einwohner 6-9: " + String.valueOf(inhabitansBetween6And29.get("amountInhabitants6To9")) + "\n" +
                "Einwohner 10-14: " + String.valueOf(inhabitansBetween6And29.get("amountInhabitants10To14")) + "\n" +
                "Einwohner 15-19: " + String.valueOf(inhabitansBetween6And29.get("amountInhabitants15To19")) + "\n" +
                "Einwohner 20-24: " + String.valueOf(inhabitansBetween6And29.get("amountInhabitants20To24")) + "\n" +
                "Einwohner 25-29: " + String.valueOf(inhabitansBetween6And29.get("amountInhabitants25To29")) + "\n" +
                "Gesamteinwohner: " + String.valueOf(inhabitansBetween6And29.get("totalAmountInhabitants")) + "\n";
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
}
