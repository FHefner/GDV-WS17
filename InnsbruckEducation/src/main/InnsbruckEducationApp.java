package main;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import markers.ImageMarker;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.List;

public class InnsbruckEducationApp extends PApplet{

    private UnfoldingMap map;
    private Table schoolData;
    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(47.264874, 11.395907);
    private List<Marker> schoolLocations;

    private final static String WINDOW_NAME = "Innsbruck Education";
    private final static String VERSION = "0.1a";
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;

    private void loadSchoolData() {
        String dataCSV = "data/hoeher_bildende_schulen.csv";
        schoolData = loadTable(dataCSV, "header");
        println(schoolData.getRowCount() + " Zeilen geladen!");
        schoolLocations = new ArrayList<>();

        for (TableRow row : schoolData.rows()) {
            Location tmpLocation = new Location(row.getFloat("Lat"), row.getFloat("Lon"));

            ImageMarker tmpMarker = new ImageMarker(tmpLocation, loadImage("res/img/lecture.png"));
            tmpMarker.setStrokeColor(90);
            tmpMarker.setStrokeWeight(5);
            /* SimplePointMarker = green dot
            SimplePointMarker tmpMarker = new SimplePointMarker(tmpLocation);
            tmpMarker.setDiameter(30);
            // Bright green
            tmpMarker.setColor(color(95, 244, 66));
            tmpMarker.setHighlightColor(150);
            tmpMarker.setStrokeColor(90);
            tmpMarker.setStrokeWeight(5);
            */
            schoolLocations.add(tmpMarker);
        }
    }

    private void processData() {
        loadSchoolData();
        map.addMarkers(schoolLocations);
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, FX2D);
        map = new UnfoldingMap(this, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
        processData();
        map.zoomAndPanTo(15, startingLocation);
        map.setZoomRange(10, 20);
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
