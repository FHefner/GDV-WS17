package main;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import markers.ImageMarker;
import models.EducationalInstitution;
import models.School;
import models.SchoolCategory;
import models.University;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.List;

public class InnsbruckEducationApp extends PApplet{

    private UnfoldingMap map;
    private List<School> schools;
    private List<University> universities;
    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(47.264874, 11.395907);

    private final static String WINDOW_NAME = "Innsbruck Education";
    private final static String VERSION = "0.1a";
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;

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
            School tmpSchool = new School(SchoolCategory.HIGHER_EDUCATION, tmpName, tmpAddress, tmpLocation);
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
                    row.getFloat(School.LOCATION_X_HEADER_FIELD),
                    row.getFloat(School.LOCATION_Y_HEADER_FIELD));
            String tmpName = row.getString(School.NAME_HEADER_FIELD);
            String tmpAddress = row.getString(School.ADDRESS_HEADER_FIELD);
            String tmpWebsite = row.getString(School.WEBSITE_HEADER_FIELD);
            University tmpUniversity = new University(tmpName, tmpAddress, tmpLocation);
            tmpUniversity.setWebsite(tmpWebsite);
            tmpUniversity.setMarkerImage(loadImage(University.MARKER_IMAGE_PATH));
            universities.add(tmpUniversity);
        }
    }

    private void addMarkersToMap() {
        List<Marker> schoolLocations = new ArrayList<>();
        for (School s : schools) {
            ImageMarker tmpMarker = new ImageMarker(s.getLocation(),
                    s.getMarkerImage());
            tmpMarker.setStrokeColor(90);
            tmpMarker.setStrokeWeight(5);
            schoolLocations.add(tmpMarker);
        }
        List<Marker> universityLocations = new ArrayList<>();
        for (University u : universities) {
            ImageMarker tmpMarker = new ImageMarker(u.getLocation(),
                    u.getMarkerImage());
            tmpMarker.setStrokeColor(90);
            tmpMarker.setStrokeWeight(5);
            universityLocations.add(tmpMarker);
        }
        map.addMarkers(schoolLocations);
        map.addMarkers(universityLocations);
    }

    private void processData() {
        loadSchoolData();
        loadUniversityData();
        addMarkersToMap();
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, FX2D);
        map = new UnfoldingMap(this, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
        processData();
        map.zoomAndPanTo(14, startingLocation);
        map.setZoomRange(10, 20);
        map.setTweening(true);
    }

    public void keyPressed() {
        // Kill the app if ESC, 'x' or 'X' is pressed
        if (key == ESC || keyCode == (int) 'x' || keyCode == (int) 'X') {
            exit();
        }
    }

    public void draw() {
        map.draw();
        Location location = map.getLocation(mouseX, mouseY);
        fill(0);
        for (Marker marker: map.getMarkers()) {
            if (marker.isInside(map, mouseX, mouseY)) {
                stroke(95, 244, 66);
                text("Innerhalb von Marker #" + marker.getId(), mouseX, mouseY);
            } else {
                stroke(255, 0, 0);
                text("Breite: " + location.getLat() + " || Länge: " + location.getLon(), mouseX, mouseY);
            }
        }

    }

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());

    }
}
