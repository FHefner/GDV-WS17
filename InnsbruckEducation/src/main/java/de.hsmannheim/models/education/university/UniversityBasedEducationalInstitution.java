package de.hsmannheim.models.education.university;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.LabeledMarker;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import processing.core.PFont;

public class UniversityBasedEducationalInstitution extends AbstractEducationalInstitution {

    protected final String GET_MARKER_IMAGE_PATH() {
        return PathConfig.UNIVERSITY_MARKER_IMAGE_PATH;
    }


    public void setShownOnMap(boolean shownOnMap) {
        this.getMarker().setHidden(!shownOnMap);
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
