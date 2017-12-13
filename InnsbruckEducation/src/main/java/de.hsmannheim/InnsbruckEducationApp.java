package de.hsmannheim;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.ColorMarker;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.models.education.school.SchoolBasedCategory;
import de.hsmannheim.models.education.school.SchoolBasedEducationalInstitution;
import de.hsmannheim.models.education.university.UniversityBasedCategory;
import de.hsmannheim.models.education.university.UniversityBasedEducationalInstitution;
import de.hsmannheim.util.district.DistrictColorCalcUtil;
import de.hsmannheim.util.district.DistrictUtil;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnsbruckEducationApp extends PApplet {


    private UnfoldingMap map;
    private List<AbstractEducationalInstitution> schools;
    private List<AbstractEducationalInstitution> universities;
    private List<UrbanDistrict> districts;
    private UrbanDistrict selectedDistrict;
    private Map<MarkerType, List<Marker>> markers = new HashMap<>();
    private boolean zoomedIntoDistrict = false;
    private DistrictUtil districtUtil;
    private boolean mouseWasDragged = false;
    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(FormConfig.XValue, FormConfig.YValue);
    private Location currentMapLocation;

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());
    }

    private void initDistrictHelper() {
        this.districtUtil = new DistrictUtil(districts);
    }

    private void loadDistricts() {
        Table districtData = loadTable(PathConfig.BEVOELKERUNG_CSV_DATA_PATH, "header");
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
                    district.addSpecificInhabitans("amountInhabitants6To9", tmp6to9);
                    district.addSpecificInhabitans("amountInhabitants10To14", tmp10to14);
                    district.addSpecificInhabitans("amountInhabitants15To19", tmp15to19);
                    district.addSpecificInhabitans("amountInhabitants20To24", tmp20to24);
                    district.addSpecificInhabitans("amountInhabitants25To29", tmp25to29);
                }
            }
            if (!districtAlreadyExisting) {
                districts.add(tmpDistrict);
            }
        }
        for (UrbanDistrict district : districts) {
            district.calculateTotalInhabitants();
            district.setColor(DistrictColorCalcUtil.calcDistrictColor(district, this));
            district.createPolygonMarker();
        }
        initDistrictHelper();
    }

    private void loadSchoolData() {
        Table schoolData = loadTable(PathConfig.HIGHSCHOOL_CSV_DATA_PATH, "header");
        schools = new ArrayList<>();
        for (TableRow row : schoolData.rows()) {
            schools.add(new SchoolBasedEducationalInstitution().buildDefaultEducationalInstitution(this, row, SchoolBasedCategory.HIGHER_EDUCATION));
        }
    }

    private void loadUniversityData() {
        Table universityData = loadTable(PathConfig.UNIVERSITY_CSV_DATA_PATH, "header");
        universities = new ArrayList<>();
        for (TableRow row : universityData.rows()) {
            universities.add(new UniversityBasedEducationalInstitution().buildDefaultEducationalInstitution(this, row, UniversityBasedCategory.DEFAULT));
        }
    }

    private List<Marker> getMarkersForMakerType(MarkerType markerType) {
        List<Marker> markerList = new ArrayList<>();
        Field objects = MarkerType.markerTypeToStrategy.get(markerType.name());
        try {
            for (ColorMarker entry : (List<ColorMarker>) objects.get(this)) {
                markerList.add(entry.getMarker());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return markerList;
    }

    private void addMarkersToMap() {

        markers.put(MarkerType.SCHOOL_MARKER, getMarkersForMakerType(MarkerType.SCHOOL_MARKER));
        markers.put(MarkerType.UNIVERSITY_MARKER, getMarkersForMakerType(MarkerType.UNIVERSITY_MARKER));
        markers.put(MarkerType.DISTRICT_MARKER, getMarkersForMakerType(MarkerType.DISTRICT_MARKER));

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
        resetDistrictColors();
        // resetEducationMarkers();
    }

    private void resetDistrictColors() {
        for (UrbanDistrict district : districts) {
            district.getMarker().resetColor();
        }
    }

    private void resetEducationMarkers() {
        for (Marker marker : markers.get(MarkerType.UNIVERSITY_MARKER)) {
            marker.setHidden(true);
        }
        for (Marker marker : markers.get(MarkerType.SCHOOL_MARKER)) {
            marker.setHidden(true);
        }
    }

    private void applyMapSettings() {
        map = new UnfoldingMap(this, "MAIN_MAP", FormConfig.MAP_X_WINDOW_OFFSET, FormConfig.MAP_Y_WINDOW_OFFSET,
                FormConfig.WINDOW_WIDTH - FormConfig.MAP_X_WINDOW_OFFSET, FormConfig.WINDOW_HEIGHT - FormConfig.MAP_Y_WINDOW_OFFSET,
                false, false, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
        // map.setTweening(true);
    }

    public void settings() {
        size(FormConfig.WINDOW_WIDTH, FormConfig.WINDOW_HEIGHT, FX2D);
        applyMapSettings();
        processData();
        resetView();

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
        System.out.println(currentMapLocation.getLat() + ", " + currentMapLocation.getLon());
        districtUtil.checkIfDistrictIsSelected(map, mouseX, mouseY);
        if (districtUtil.isDistrictSelected() && !mouseWasDragged) {
            for (UrbanDistrict district : districts) {
                changeColorOfSelectedDistrict(district);
            }
        }
        mouseWasDragged = false;
    }

    private void showEducationalInstitionsInSelectedDistrict() {
        for (Marker marker : markers.get(MarkerType.UNIVERSITY_MARKER)) {
            if (selectedDistrict.getMarker().isInside(map,
                    marker.getLocation().getLat(), marker.getLocation().getLon())) {
                marker.setHidden(false);
            }
        }
        for (Marker marker : markers.get(MarkerType.SCHOOL_MARKER)) {
            if (selectedDistrict.getMarker().isInside(map,
                    marker.getLocation().getLat(), marker.getLocation().getLon())) {
                marker.setHidden(false);
            }
        }
    }

    private void changeColorOfSelectedDistrict(UrbanDistrict district) {
        if (district.getIsSelected()) {
            selectedDistrict = district;
            System.out.println(district);
            map.zoomAndPanToFit(district.getLocationsFromJSONArray());
            zoomedIntoDistrict = true;
            showEducationalInstitionsInSelectedDistrict();
            district.getMarker().setPolygonColor(district.getMarker().getInitialColor());
        } else {
            district.getMarker().setPolygonColor(color(90, 90, 90));
        }
    }

    public void mouseDragged() {
        mouseWasDragged = true;
    }

    public void draw() {
        map.draw();
        currentMapLocation = map.getLocation(mouseX, mouseY);
        fill(80, 80);
        noStroke();
    }
}
