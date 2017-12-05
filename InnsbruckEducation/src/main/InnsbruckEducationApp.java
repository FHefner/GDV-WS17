package main;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import markers.MarkerType;
import models.School;
import models.SchoolCategory;
import models.University;
import models.UrbanDistrict;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnsbruckEducationApp extends PApplet{

    private UnfoldingMap map;
    private List<School> schools;
    private List<University> universities;
    private List<UrbanDistrict> districts;
    private Map<MarkerType, List<Marker>> markers;
    private boolean zoomedIntoDistrict;

    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(47.264874, 11.395907);

    private final static String WINDOW_NAME = "Innsbruck Education";
    private final static String VERSION = "0.1a";
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;



    private void loadDistricts() {
        Table districtData = loadTable(UrbanDistrict.CSV_DATA_PATH, "header");
        districts = new ArrayList<>();
        for (TableRow row : districtData.rows()) {
            int tmpZaehlerSprengel = row.getInt("ZSPR");
            int tmp6to9 = row.getInt("6_9");
            int tmp10to14 = row.getInt("10_14");
            int tmp15to19 = row.getInt("15_19");
            int tmp20to24 = row.getInt("20_24");
            int tmp25to29 = row.getInt("25_29");
            UrbanDistrict tmpDistrict = new UrbanDistrict(
                    this, tmpZaehlerSprengel, tmp6to9, tmp10to14,
                    tmp15to19, tmp20to24, tmp25to29);

            boolean districtAlreadyExisting = false;
            for (UrbanDistrict district : districts) {
                if (district.getRegionNumber() == tmpDistrict.getRegionNumber() && !districtAlreadyExisting) {
                    districtAlreadyExisting = true;
                    district.setAmountInhabitants6To9(district.getAmountInhabitants6To9() + tmp6to9);
                    district.setAmountInhabitants10To14(district.getAmountInhabitants10To14() + tmp10to14);
                    district.setAmountInhabitants15To19(district.getAmountInhabitants15To19() + tmp15to19);
                    district.setAmountInhabitants20To24(district.getAmountInhabitants20To24() + tmp20to24);
                    district.setAmountInhabitants25To29(district.getAmountInhabitants25To29() + tmp25to29);
                }
            }
            if (!districtAlreadyExisting) {
                districts.add(tmpDistrict);
            }
        }
        for (UrbanDistrict district : districts) {
            district.calculateTotalInhabitants();
        }
    }

    private void loadSchoolData() {
        Table schoolData = loadTable(School.CSV_DATA_PATH, "header");
        schools = new ArrayList<>();
        for (TableRow row : schoolData.rows()) {
            Location tmpLocation = new Location(
                    row.getFloat(School.LOCATION_X_HEADER_FIELD),
                    row.getFloat(School.LOCATION_Y_HEADER_FIELD));
            String tmpName = row.getString(School.NAME_HEADER_FIELD);
            String tmpAddress = row.getString(School.ADDRESS_HEADER_FIELD);
            String tmpWebsite = row.getString(School.WEBSITE_HEADER_FIELD);
            School tmpSchool = new School(this, SchoolCategory.HIGHER_EDUCATION, tmpName, tmpAddress, tmpLocation);
            tmpSchool.setWebsite(tmpWebsite);
            tmpSchool.setMarkerImage(loadImage(School.MARKER_IMAGE_PATH));
            schools.add(tmpSchool);
        }
    }

    private void loadUniversityData() {
        Table universityData = loadTable(University.CSV_DATA_PATH, "header");
        universities = new ArrayList<>();
        for (TableRow row : universityData.rows()) {
            Location tmpLocation = new Location(
                    row.getFloat(University.LOCATION_X_HEADER_FIELD),
                    row.getFloat(University.LOCATION_Y_HEADER_FIELD));
            String tmpName = row.getString(University.NAME_HEADER_FIELD);
            String tmpAddress = row.getString(University.ADDRESS_HEADER_FIELD);
            String tmpWebsite = row.getString(University.WEBSITE_HEADER_FIELD);
            University tmpUniversity = new University(this, tmpName, tmpAddress, tmpLocation);
            tmpUniversity.setWebsite(tmpWebsite);
            tmpUniversity.setMarkerImage(loadImage(University.MARKER_IMAGE_PATH));
            universities.add(tmpUniversity);
        }
    }

    private void addMarkersToMap() {
        markers = new HashMap<>();
        List<Marker> schoolMarkers = new ArrayList<>();
        for (School s : schools) {
            schoolMarkers.add(s.getMarker());
        }
        markers.put(MarkerType.SCHOOL_MARKER, schoolMarkers);

        List<Marker> universityMarkers = new ArrayList<>();
        for (University u : universities) {
            universityMarkers.add(u.getMarker());
        }
        markers.put(MarkerType.UNIVERSITY_MARKER, universityMarkers);

        List<Marker> districtMarkers = new ArrayList<>();
        for (UrbanDistrict d : districts) {
            districtMarkers.add(d.getMarker());
        }
        markers.put(MarkerType.DISTRICT_MARKER, districtMarkers);

        map.addMarkers(markers.get(MarkerType.DISTRICT_MARKER));
        map.addMarkers(markers.get(MarkerType.SCHOOL_MARKER));
        map.addMarkers(markers.get(MarkerType.UNIVERSITY_MARKER));
    }

    private void processData() {
        loadSchoolData();
        loadUniversityData();
        loadDistricts();
        addMarkersToMap();
    }

    private void resetView() {
        map.zoomAndPanTo(12, startingLocation);
        map.setZoomRange(10, 20);
        map.setPanningRestriction(startingLocation, 10);
        zoomedIntoDistrict = false;
        for (UrbanDistrict district : districts) {
            district.getMarker().resetColor();
        }
    }

    private void highlightDistrict(String districtName) {
        List<UrbanDistrict> otherDistricts = new ArrayList<>(districts);
        for (UrbanDistrict district : districts) {
            if (district.getName().equals(districtName)) {
                otherDistricts.remove(district);
            }
        }
        for (UrbanDistrict district : otherDistricts) {
            district.getMarker().setColor(color(140, 140, 140, 100));
        }
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, FX2D);
        map = new UnfoldingMap(this, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
        processData();
        resetView();
        // map.setTweening(true);
    }

    public void keyPressed() {
        // Kill the app if ESC is pressed
        if (key == ESC) {
            exit();
        }
        // Go Back to start view if BACKSPACE is pressed
        if (key == BACKSPACE) {
            resetView();
        }
    }

    public void mouseClicked() {
        for (UrbanDistrict district : districts) {
            if (district.getMarker().isInside(map, mouseX, mouseY)) {
                System.out.println(district);
                map.zoomAndPanToFit(district.getLocations());
                zoomedIntoDistrict = true;
                highlightDistrict(district.getName());
            }
        }
    }

    public void draw() {
        map.draw();
        Location location = map.getLocation(mouseX, mouseY);
        fill(80, 80);
        noStroke();
        // ellipse(mouseX, mouseY, 80, 80);

    }

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());
    }
}
