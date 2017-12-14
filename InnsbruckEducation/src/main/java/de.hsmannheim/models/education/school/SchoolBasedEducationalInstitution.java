package de.hsmannheim.models.education.school;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import processing.core.PApplet;

public class SchoolBasedEducationalInstitution extends AbstractEducationalInstitution {

    private SchoolBasedCategory category;

    protected final String GET_MARKER_IMAGE_PATH() {
        return PathConfig.HIGHER_EDUCATION_MARKER_IMAGE_PATH;
    }

    public SchoolBasedCategory getCategory() {
        return category;
    }

    public void setCategory(SchoolBasedCategory category) {
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
