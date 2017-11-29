package models;

import de.fhpotsdam.unfolding.geo.Location;

public class University extends EducationalInstitution {

    /* Constants */
    public final static String MARKER_IMAGE_PATH = "res/img/university.png";
    public final static String CSV_DATA_PATH = "data/universitaet_standorte.csv";
    public final static String NAME_HEADER_FIELD = "Bezeichnung";
    public final static String ADDRESS_HEADER_FIELD = "Adresse";
    public final static String WEBSITE_HEADER_FIELD = "Link";
    public final static String LOCATION_X_HEADER_FIELD = "Lat";
    public final static String LOCATION_Y_HEADER_FIELD = "Lon";

    public University(String name, String address, String website, Location location, int capacity, int currentPeopleAmount) {
        super(name, address, website, location, capacity, currentPeopleAmount);
    }

    public University(String name, String address, Location location) {
        super(name, address, location);
    }

    public University(Location location) {
        super(location);
    }
}
