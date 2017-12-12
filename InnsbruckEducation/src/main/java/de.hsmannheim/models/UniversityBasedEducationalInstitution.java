package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import processing.core.PApplet;

public class UniversityBasedEducationalInstitution extends AbstractEducationalInstitution {


    public UniversityBasedEducationalInstitution(PApplet applet, String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        super(applet, name, address, website, location, capacity, currentPeopleAmount);
        setMarkerImage(applet.loadImage(PathConfig.UNIVERSITY_MARKER_IMAGE_PATH));
        this.color = applet.color(161, 0, 255);
        createMarker();
    }

    public UniversityBasedEducationalInstitution(PApplet applet, String name, String address, Location location) {
        super(applet, name, address, location);
        setMarkerImage(applet.loadImage(PathConfig.UNIVERSITY_MARKER_IMAGE_PATH));
        this.color = applet.color(161, 0, 255);
        createMarker();
    }

    public UniversityBasedEducationalInstitution(PApplet applet, Location location) {
        super(applet, location);
        setMarkerImage(applet.loadImage(PathConfig.UNIVERSITY_MARKER_IMAGE_PATH));
        this.color = applet.color(161, 0, 255);
        createMarker();
    }

    public void setShownOnMap(boolean shownOnMap) {
        this.getMarker().setHidden(!shownOnMap);
    }

    @Override
    public void createMarker() {
        SimplePointMarker marker = new SimplePointMarker(this.getLocation());
        marker.setColor(this.color);
        marker.setStrokeColor(90);
        marker.setStrokeWeight(5);
        marker.setHidden(false);
        setMarker(marker);
    }
}
