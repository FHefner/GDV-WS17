package de.hsmannheim.models.education.university;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.models.education.AbstractEducationalInstitution;

public class UniversityBasedEducationalInstitution extends AbstractEducationalInstitution {

    protected final String GET_MARKER_IMAGE_PATH() {
        return PathConfig.UNIVERSITY_MARKER_IMAGE_PATH;
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
        marker.setHidden(true);
        setMarker(marker);
    }
}
