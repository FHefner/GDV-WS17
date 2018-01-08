package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.ColorMarker;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.file.FileUtil;
import de.hsmannheim.util.location.LocationUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSpengelMapUtil;
import de.hsmannheim.util.map.zaehlersprengel.ZaehlerSprengelBasedStrategy;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.*;

public class UrbanDistrict implements ColorMarker {


    protected Map<Integer, Map<String, Integer>> inhabitantsBetween6And29 = new HashMap<>();

    private PApplet applet;
    private int zaehlerSprengel = -1;
    private int regionNumber = -1;
    private String name = "undefined";
    private List<Location> locations;
    private List<AbstractEducationalInstitution> schools = new ArrayList<>();
    private List<AbstractEducationalInstitution> universities = new ArrayList<>();
    private ColoredPolygonMarker marker;
    private boolean isSelected;
    private Integer color;

    private UrbanDistrict() {

    }

    public static UrbanDistrict buildDefaultDistrict(PApplet applet, Table rows, Integer rowCount) {

        Map<Integer, Map<String, Integer>> inhabitantsMap = getCompleteInhabitantsForDistrict(rows, rowCount);

        if (inhabitantsMap.isEmpty()) {
            return null;
        }

        return new UrbanDistrict()
                .withApplet(applet)
                .withZaehlerSprengel(rowCount)
                .withInhabitantsMap(inhabitantsMap)
                .withZaehlerSprengelToRegionNumberAndName()
                .withLocations(applet);

    }

    private static Map<Integer, Map<String, Integer>> getCompleteInhabitantsForDistrict(Table table, Integer rowCount) {
        Map<Integer, Map<String, Integer>> inhabitantsMap = new HashMap<>();
        for (TableRow row : table.rows()) {
            if (row.getInt("ZSPR") == rowCount) {
                inhabitantsMap.put(row.getInt("year"), withKeyValuePairs(row,
                        new LinkedHashMap<String, String>() {{
                            put("amountInhabitants6To9", "6_9");
                            put("amountInhabitants10To14", "10_14");
                            put("amountInhabitants15To19", "15_19");
                            put("amountInhabitants20To24", "20_24");
                            put("amountInhabitants25To29", "25_29");
                        }}));
            }
        }
        return inhabitantsMap;
    }

    private static Map<String, Integer> withKeyValuePairs(TableRow row, LinkedHashMap<String, String> inhabitantsAmountsMap) {
        Map<String, Integer> specificInhabitantsMap = new HashMap<>();
        for (Map.Entry<String, String> entries : inhabitantsAmountsMap.entrySet()) {
            specificInhabitantsMap.put(entries.getKey(), row.getInt(entries.getValue()));
        }
        return specificInhabitantsMap;
    }

    private UrbanDistrict withInhabitantsMap(Map<Integer, Map<String, Integer>> inhabitantsMap) {
        this.inhabitantsBetween6And29 = inhabitantsMap;
        return this;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<AbstractEducationalInstitution> getSchools() {
        return schools;
    }

    public int getSumSchools() {
        return schools.size();
    }

    public int getSumUniversities() {
        return universities.size();
    }

    public int getSumEducationalInstitutions() {
        return getSumSchools() + getSumUniversities();
    }

    public List<AbstractEducationalInstitution> getUniversities() {
        return universities;
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
            addToInhabitantsMap(entry, row);
        }
        return this;
    }

    private UrbanDistrict withZaehlerSprengelToRegionNumberAndName() {
        ZaehlerSpengelMapUtil.traverseOverTableAndSetResult(this, applet.loadTable(PathConfig.MAPPING_ZSPRL_CSV_PATH, "header"), new ZaehlerSprengelBasedStrategy());
        return this;
    }

    public void addToInhabitantsMap(Map.Entry<String, String> entry, TableRow row) {
        Integer year = FileUtil.getYearFromRow(row);
        if (DistrictUtil.isYearInMap(this.inhabitantsBetween6And29, year)) {
            this.inhabitantsBetween6And29.get(year).put(entry.getKey(), row.getInt(entry.getValue()));
        } else {
            this.inhabitantsBetween6And29.put(year, new HashMap<String, Integer>());
            this.inhabitantsBetween6And29.get(year).put(entry.getKey(), row.getInt(entry.getValue()));
        }
    }

    private UrbanDistrict withLocations(PApplet applet) {
        locations = LocationUtil.extractLocations(applet, this);
        return this;
    }

    public void createPolygonMarker() {
        this.marker = new ColoredPolygonMarker(this.applet, this.locations, this.color);
    }

    public void calculateTotalInhabitantsForGivenYear(Integer year) {
        if (!this.getInhabitantsBetween6And29().get(year).keySet().contains("totalAmountInhabitants"))
            this.inhabitantsBetween6And29.get(year).put("totalAmountInhabitants", DistrictUtil.calculateInhabitantsSum6to29(this, year));
    }


    public List<Marker> getSchoolMarkers() {
        List<Marker> schoolMarkers = new ArrayList<>();
        for (AbstractEducationalInstitution school : this.schools) {
            schoolMarkers.add(school.getMarker());
        }
        return schoolMarkers;
    }

    public List<Marker> getUniversityMarkers() {
        List<Marker> universityMarkers = new ArrayList<>();
        for (AbstractEducationalInstitution university : this.universities) {
            universityMarkers.add(university.getMarker());
        }
        return universityMarkers;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Map<String, Integer>> entry : this.inhabitantsBetween6And29.entrySet()) {
            builder.append(entry.getKey() + " - " + entry.getValue().get("totalAmountInhabitants"));
        }
        return builder.toString();
        /*return String.valueOf(this.zaehlerSprengel) +

                " " + this.name + "\n" +
                "Einwohner 6-9: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("amountInhabitants6To9")) + "\n" +
                "Einwohner 10-14: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("amountInhabitants10To14")) + "\n" +
                "Einwohner 15-19: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("amountInhabitants15To19")) + "\n" +
                "Einwohner 20-24: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("amountInhabitants20To24")) + "\n" +
                "Einwohner 25-29: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("amountInhabitants25To29")) + "\n" +
                "Gesamteinwohner: " + String.valueOf(inhabitantsBetween6And29.get(2017).get("totalAmountInhabitants")) + "\n";*/

    }

    public Map<Integer, Map<String, Integer>> getInhabitantsBetween6And29() {
        return this.inhabitantsBetween6And29;
    }

    public void setSelectedTrue(boolean selected){
        this.isSelected=selected;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public Integer getColor() {
        return this.color;
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
