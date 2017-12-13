package de.hsmannheim.models.education;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.models.ColorMarker;
import de.hsmannheim.models.education.school.SchoolBasedEducationalInstitution;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.TableRow;

import java.util.Arrays;

public abstract class AbstractEducationalInstitution implements ColorMarker{

    public final static String NAME_HEADER_FIELD = "Bezeichnung";
    public final static String ADDRESS_HEADER_FIELD = "Adresse";
    public final static String WEBSITE_HEADER_FIELD = "Link";
    public final static String LOCATION_X_HEADER_FIELD = "Lat";
    public final static String LOCATION_Y_HEADER_FIELD = "Lon";
    protected Integer color;
    ICategory category;
    private String name;
    private String address;
    private String website;
    private Location location;
    private Marker marker;
    private int capacity = -1;
    private int currentPeopleAmount = -1;
    private PImage markerImage;
    private PApplet applet;


    public abstract void createMarker();

    protected abstract String GET_MARKER_IMAGE_PATH();

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public PImage getMarkerImage() {
        return markerImage;
    }

    public void setMarkerImage(PImage markerImage) {
        this.markerImage = markerImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentPeopleAmount() {
        return currentPeopleAmount;
    }

    public void setCurrentPeopleAmount(int currentPeopleAmount) {
        this.currentPeopleAmount = currentPeopleAmount;
    }

    public AbstractEducationalInstitution buildDefaultEducationalInstitution(PApplet applet, TableRow row, ICategory category) {
        return this
                .withApplet(applet)
                .withName(row.getString(NAME_HEADER_FIELD))
                .withAddress(row.getString(ADDRESS_HEADER_FIELD))
                .withWebsite(row.getString(WEBSITE_HEADER_FIELD))
                .withLocation(row.getFloat(SchoolBasedEducationalInstitution.LOCATION_X_HEADER_FIELD), row.getFloat(SchoolBasedEducationalInstitution.LOCATION_Y_HEADER_FIELD))
                .withCategory(category)
                .withColor(applet, category)
                .withMarkerImage(GET_MARKER_IMAGE_PATH());
    }

    private AbstractEducationalInstitution withColor(PApplet applet, ICategory category) {
        int[] colors = category.getSchoolCategoryNameToColor().get(category.name());
        this.color = applet.color(colors[0], colors[1], colors[2]);
        createMarker();
        return this;
    }

    private AbstractEducationalInstitution withLocation(float X, float Y) {
        this.location = new Location(X, Y);
        return this;
    }

    private AbstractEducationalInstitution withMarkerImage(String markerImagePath) {
        this.markerImage = this.applet.loadImage(markerImagePath);
        return this;
    }

    private AbstractEducationalInstitution withWebsite(String website) {
        this.website = website;
        return this;
    }

    private AbstractEducationalInstitution withCategory(ICategory category) {
        this.category = category;
        return this;
    }

    private AbstractEducationalInstitution withAddress(String address) {
        this.address = address;
        return this;
    }

    private AbstractEducationalInstitution withName(String name) {
        this.name = name;
        return this;
    }

    private AbstractEducationalInstitution withApplet(PApplet applet) {
        this.applet = applet;
        return this;
    }
}
