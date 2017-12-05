package models;

import de.fhpotsdam.unfolding.geo.Location;
import markers.ImageMarker;
import processing.core.PApplet;

public class University extends EducationalInstitution implements IEducationalInstitution {

    /* Constants */
    public final static String MARKER_IMAGE_PATH = "res/img/university.png";
    public final static String CSV_DATA_PATH = "data/universitaet_standorte.csv";
    public final static String NAME_HEADER_FIELD = "Bezeichnung";
    public final static String ADDRESS_HEADER_FIELD = "Adresse";
    public final static String WEBSITE_HEADER_FIELD = "Link";
    public final static String LOCATION_X_HEADER_FIELD = "Lat";
    public final static String LOCATION_Y_HEADER_FIELD = "Lon";

    public University(PApplet applet, String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        super(applet, name, address, website, location, capacity, currentPeopleAmount);
        setMarkerImage(applet.loadImage(MARKER_IMAGE_PATH));
        createMarker();
    }

    public University(PApplet applet, String name, String address, Location location) {
        super(applet, name, address, location);
        setMarkerImage(applet.loadImage(MARKER_IMAGE_PATH));
        createMarker();
    }

    public University(PApplet applet, Location location) {
        super(applet, location);
        setMarkerImage(applet.loadImage(MARKER_IMAGE_PATH));
        createMarker();
    }

    @Override
    public void createMarker() {
        ImageMarker marker = new ImageMarker(getLocation(), getMarkerImage());
        marker.setStrokeColor(90);
        marker.setStrokeWeight(5);
        setMarker(marker);
    }
}
