package de.hsmannheim.models.education.school;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.LabeledMarker;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import processing.core.PApplet;
import processing.core.PFont;

public class SchoolBasedEducationalInstitution extends AbstractEducationalInstitution {

    protected final String GET_MARKER_IMAGE_PATH() {
        return PathConfig.HIGHER_EDUCATION_MARKER_IMAGE_PATH;
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
        /*
        SimplePointMarker marker = new SimplePointMarker(this.getLocation());
        marker.setColor(this.color);
        marker.setStrokeColor(90);
        marker.setStrokeWeight(5);
        marker.setHidden(true);
        setMarker(marker);
        */
        PFont font = applet.loadFont("src/main/resources/fonts/OpenSans-12.vlw");
        LabeledMarker marker = new LabeledMarker(this.getLocation(), this, font, 15);
        marker.setColor(this.color);
        marker.setHighlightColor(this.color);
        marker.setStrokeColor(90);
        marker.setHighlightStrokeColor(90);
        marker.setStrokeWeight(5);
        marker.setHidden(true);
        setMarker(marker);
    }
}
