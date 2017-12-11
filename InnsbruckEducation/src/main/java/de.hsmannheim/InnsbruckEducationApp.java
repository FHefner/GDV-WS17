package de.hsmannheim;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.SchoolBasedEducationalInstitution;
import de.hsmannheim.models.SchoolCategory;
import de.hsmannheim.models.UniversityBasedEducationalInstitution;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.helper.DistrictHelper;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnsbruckEducationApp extends PApplet {

    private UnfoldingMap map;
    private List<SchoolBasedEducationalInstitution> schools;
    private List<UniversityBasedEducationalInstitution> universities;
    private List<UrbanDistrict> districts;
    private Map<MarkerType, List<Marker>> markers;
    private boolean zoomedIntoDistrict;
    private DistrictHelper districtHelper;
    private boolean mouseWasDragged = false;
    private int[] savedMousePositions = {-1, -1};


    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(47.264874, 11.395907);

    private final static String WINDOW_NAME = "Innsbruck Education";
    private final static String VERSION = "0.1a";
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;


    private void initDistrictHelper() {
        this.districtHelper = new DistrictHelper(districts);
    }


    private void loadDistricts() {
        Table districtData = loadTable(UrbanDistrict.CSV_DATA_PATH, "header");
        districts = new ArrayList<>();
        for (TableRow row : districtData.rows()) {
            int tmpZaehlerSprengel = row.getInt("ZSPR");
            int tmp6to9 = row.getInt("6_9");
            int tmp10to14 = row.getInt("10_14");
            int tmp15to19 = row.getInt("15_19");
            int tmp20to24 = row.getInt("20_24");
            int tmp25to29 = row.getInt("25_29");
            UrbanDistrict tmpDistrict = new UrbanDistrict(
                    this, tmpZaehlerSprengel, tmp6to9, tmp10to14,
                    tmp15to19, tmp20to24, tmp25to29);
            boolean districtAlreadyExisting = false;
            for (UrbanDistrict district : districts) {
                if (district.getRegionNumber() == tmpDistrict.getRegionNumber() && !districtAlreadyExisting) {
                    districtAlreadyExisting = true;
                    district.setAmountInhabitants6To9(district.getAmountInhabitants6To9() + tmp6to9);
                    district.setAmountInhabitants10To14(district.getAmountInhabitants10To14() + tmp10to14);
                    district.setAmountInhabitants15To19(district.getAmountInhabitants15To19() + tmp15to19);
                    district.setAmountInhabitants20To24(district.getAmountInhabitants20To24() + tmp20to24);
                    district.setAmountInhabitants25To29(district.getAmountInhabitants25To29() + tmp25to29);
                }
            }
            if (!districtAlreadyExisting) {
                districts.add(tmpDistrict);
            }
        }
        for (UrbanDistrict district : districts) {
            district.calculateTotalInhabitants();
        }
        initDistrictHelper();
    }

    private void loadSchoolData() {
        Table schoolData = loadTable(SchoolBasedEducationalInstitution.CSV_DATA_PATH, "header");
        schools = new ArrayList<>();
        for (TableRow row : schoolData.rows()) {
            Location tmpLocation = new Location(
                    row.getFloat(SchoolBasedEducationalInstitution.LOCATION_X_HEADER_FIELD),
                    row.getFloat(SchoolBasedEducationalInstitution.LOCATION_Y_HEADER_FIELD));
            String tmpName = row.getString(SchoolBasedEducationalInstitution.NAME_HEADER_FIELD);
            String tmpAddress = row.getString(SchoolBasedEducationalInstitution.ADDRESS_HEADER_FIELD);
            String tmpWebsite = row.getString(SchoolBasedEducationalInstitution.WEBSITE_HEADER_FIELD);
            SchoolBasedEducationalInstitution tmpSchool = new SchoolBasedEducationalInstitution(this, SchoolCategory.HIGHER_EDUCATION, tmpName, tmpAddress, tmpLocation);
            tmpSchool.setWebsite(tmpWebsite);
            tmpSchool.setMarkerImage(loadImage(SchoolBasedEducationalInstitution.MARKER_IMAGE_PATH));
            schools.add(tmpSchool);
        }
    }

    private void loadUniversityData() {
        Table universityData = loadTable(UniversityBasedEducationalInstitution.CSV_DATA_PATH, "header");
        universities = new ArrayList<>();
        for (TableRow row : universityData.rows()) {
            Location tmpLocation = new Location(
                    row.getFloat(UniversityBasedEducationalInstitution.LOCATION_X_HEADER_FIELD),
                    row.getFloat(UniversityBasedEducationalInstitution.LOCATION_Y_HEADER_FIELD));
            String tmpName = row.getString(UniversityBasedEducationalInstitution.NAME_HEADER_FIELD);
            String tmpAddress = row.getString(UniversityBasedEducationalInstitution.ADDRESS_HEADER_FIELD);
            String tmpWebsite = row.getString(UniversityBasedEducationalInstitution.WEBSITE_HEADER_FIELD);
            UniversityBasedEducationalInstitution tmpUniversity = new UniversityBasedEducationalInstitution(this, tmpName, tmpAddress, tmpLocation);
            tmpUniversity.setWebsite(tmpWebsite);
            tmpUniversity.setMarkerImage(loadImage(UniversityBasedEducationalInstitution.MARKER_IMAGE_PATH));
            universities.add(tmpUniversity);
        }
    }

    private void addMarkersToMap() {
        markers = new HashMap<>();
        List<Marker> schoolMarkers = new ArrayList<>();
        for (SchoolBasedEducationalInstitution s : schools) {
            schoolMarkers.add(s.getMarker());
        }
        markers.put(MarkerType.SCHOOL_MARKER, schoolMarkers);

        List<Marker> universityMarkers = new ArrayList<>();
        for (UniversityBasedEducationalInstitution u : universities) {
            universityMarkers.add(u.getMarker());
        }
        markers.put(MarkerType.UNIVERSITY_MARKER, universityMarkers);

        List<Marker> districtMarkers = new ArrayList<>();
        for (UrbanDistrict d : districts) {
            districtMarkers.add(d.getMarker());
        }
        markers.put(MarkerType.DISTRICT_MARKER, districtMarkers);

        map.addMarkers(markers.get(MarkerType.DISTRICT_MARKER));
        map.addMarkers(markers.get(MarkerType.SCHOOL_MARKER));
        map.addMarkers(markers.get(MarkerType.UNIVERSITY_MARKER));
    }

    private void processData() {
        loadSchoolData();
        loadUniversityData();
        loadDistricts();
        addMarkersToMap();
    }

    private void resetView() {
        map.zoomAndPanTo(12, startingLocation);
        map.setZoomRange(10, 20);
        map.setPanningRestriction(startingLocation, 10);
        zoomedIntoDistrict = false;
        resetColorDistricts();
    }

    /**
     * Iterates over the existing Districts
     * and resets the color of the areas to their initial color
     */
    private void resetColorDistricts() {
        for (UrbanDistrict district : districts) {
            district.getMarker().resetColor();
        }
    }

    private void highlightDistrict(String districtName) {
        List<UrbanDistrict> otherDistricts = new ArrayList<>(districts);
        for (UrbanDistrict district : districts) {
            if (district.getName().equals(districtName)) {
                otherDistricts.remove(district);
            }
        }
        for (UrbanDistrict district : otherDistricts) {
            district.getMarker().setColor(color(140, 140, 140, 100));
        }
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, FX2D);
        map = new UnfoldingMap(this, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
        processData();
        resetView();
        // map.setTweening(true);
    }

    public void keyPressed() {
        // Kill the app if ESC is pressed
        if (key == ESC) {
            exit();
        }
        // Go Back to start view if BACKSPACE is pressed
        if (key == BACKSPACE) {
            resetView();
        }
    }

    public void mouseClicked() {
        districtHelper.checkIfDistrictIsSelected(map, mouseX, mouseY);
        if (districtHelper.isDistrictSelected() && !mouseWasDragged) {
            for (UrbanDistrict district : districts) {
                changeColorOfSelectedDistrict(district);
            }
        }
        mouseWasDragged = false;
    }


    void changeColorOfSelectedDistrict(UrbanDistrict district) {
        if (district.getIsSelected()) {
            System.out.println(district);
            map.zoomAndPanToFit(district.getLocationsFromJSONArray());
            zoomedIntoDistrict = true;
            highlightDistrict(district.getName());
            //TODO-Heatmap If Heatmap implemented delete this line
            district.getMarker().setPolygonColor(district.getMarker().getInitialColor());
        } else {
            district.getMarker().setPolygonColor(color(90, 90, 90));
        }
    }

    public void mouseDragged() {
        mouseWasDragged=true;
    }

    public void mouseReleased() {
    }

    private void setSavedMousePositions(int mousePX, int mousePY) {
        savedMousePositions[0] = mousePX;
        savedMousePositions[1] = mousePY;
    }

    public void draw() {
        map.draw();
        Location location = map.getLocation(mouseX, mouseY);
        fill(80, 80);
        noStroke();
        // ellipse(mouseX, mouseY, 80, 80);

    }

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());
    }
}
