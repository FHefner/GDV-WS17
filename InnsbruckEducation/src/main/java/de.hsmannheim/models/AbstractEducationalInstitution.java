package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class AbstractEducationalInstitution implements IEducationalInstitution {

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

    public AbstractEducationalInstitution(PApplet applet, String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        this.applet = applet;
        this.name = name;
        this.address = address;
        this.website = website;
        this.location = location;
        this.capacity = capacity;
        this.currentPeopleAmount = currentPeopleAmount;
    }

    public AbstractEducationalInstitution(PApplet applet, String name, String address, Location location) {
        this.applet = applet;
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public AbstractEducationalInstitution(PApplet applet, Location location) {
        this.applet = applet;
        this.location = location;
    }
}
