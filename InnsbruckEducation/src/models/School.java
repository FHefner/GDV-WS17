package models;

import de.fhpotsdam.unfolding.geo.Location;

public class School extends EducationalInstitution {
    private SchoolCategory category;

    /* Constants */
    public final static String MARKER_IMAGE_PATH = "res/img/school.png";
    public final static String CSV_DATA_PATH = "data/hoeher_bildende_schulen.csv";
    public final static String NAME_HEADER_FIELD = "Bezeichnung";
    public final static String ADDRESS_HEADER_FIELD = "Adresse";
    public final static String WEBSITE_HEADER_FIELD = "Link";
    public final static String LOCATION_X_HEADER_FIELD = "Lat";
    public final static String LOCATION_Y_HEADER_FIELD = "Lon";

    public SchoolCategory getCategory() {
        return category;
    }

    public void setCategory(SchoolCategory category) {
        this.category = category;
    }

    public School(SchoolCategory category, String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        super(name, address, website, location, capacity, currentPeopleAmount);
        this.category = category;
    }

    public School(SchoolCategory category, String name, String address, Location location) {
        super(name, address, location);
        this.category = category;
    }

    public School(SchoolCategory category, Location location) {
        super(location);
        this.category = category;
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
}
