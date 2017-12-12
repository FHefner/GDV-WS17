package de.hsmannheim.models;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import processing.core.PApplet;

public class SchoolBasedEducationalInstitution extends AbstractEducationalInstitution {

    private SchoolCategory category;


    public SchoolCategory getCategory() {
        return category;
    }

    public void setCategory(SchoolCategory category) {
        this.category = category;
    }

    private void setSchoolColorBasedOnCategory(PApplet applet) {
        switch (this.category) {
            case HIGHER_EDUCATION: {
                this.color = applet.color(15, 132, 0);
                break;
            }
        }
    }

    public SchoolBasedEducationalInstitution(PApplet applet, SchoolCategory category, String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        super(applet, name, address, website, location, capacity, currentPeopleAmount);
        this.category = category;
        setMarkerImage(applet.loadImage(PathConfig.HIGHSCHOOL_MARKER_IMAGE_PATH));
        createMarker();

    }

    public SchoolBasedEducationalInstitution(PApplet applet, SchoolCategory category, String name, String address, Location location) {
        super(applet, name, address, location);
        this.category = category;
        setMarkerImage(applet.loadImage(PathConfig.HIGHSCHOOL_MARKER_IMAGE_PATH));
        setSchoolColorBasedOnCategory(applet);
        createMarker();
    }

    public SchoolBasedEducationalInstitution(PApplet applet, SchoolCategory category, Location location) {
        super(applet, location);
        this.category = category;
        setMarkerImage(applet.loadImage(PathConfig.HIGHSCHOOL_MARKER_IMAGE_PATH));
        setSchoolColorBasedOnCategory(applet);
        createMarker();
    }

    public void setShownOnMap(boolean shownOnMap) {
        this.getMarker().setHidden(!shownOnMap);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(this.category.toString());
        if (this.getName() != null) {
            output.append(": '").append(this.getName()).append("'\n");
        } else {
            output.append(":\n");
        }
        if (this.getAddress() != null) output.append("Adresse: ").append(this.getAddress()).append("\n");
        if (this.getWebsite() != null) output.append("Website: ").append(this.getWebsite()).append("\n");
        if (this.getLocation() != null) output.append("Geo-Koordinaten: (").append(this.getLocation().getLat()).append(",")
                .append(this.getLocation().getLon()).append(")\n");
        if (this.getCapacity() != -1) output.append("Kapazität: ").append(this.getCapacity()).append("\n");
        if (this.getCurrentPeopleAmount() != -1) output.append("Aktuelle Schüleranzahl: ")
                .append(this.getCurrentPeopleAmount()).append("\n");
        return output.toString();
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
