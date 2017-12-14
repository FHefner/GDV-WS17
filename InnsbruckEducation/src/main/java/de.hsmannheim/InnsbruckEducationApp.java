package de.hsmannheim;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.models.education.school.SchoolBasedCategory;
import de.hsmannheim.models.education.school.SchoolBasedEducationalInstitution;
import de.hsmannheim.models.education.university.UniversityBasedCategory;
import de.hsmannheim.models.education.university.UniversityBasedEducationalInstitution;
import de.hsmannheim.util.district.DistrictColorCalcUtil;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.marker.MarkerTypeUtil;
import de.hsmannheim.util.unfoldingMap.UnfoldingMapUtil;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnsbruckEducationApp extends PApplet {


    public List<AbstractEducationalInstitution> schools;
    public List<AbstractEducationalInstitution> universities;
    public List<UrbanDistrict> districts;
    private UnfoldingMap map;
    private UrbanDistrict selectedDistrict;
    private Map<MarkerType, List<Marker>> markers = new HashMap<>();
    private boolean zoomedIntoDistrict = false;
    private DistrictUtil districtUtil;
    private boolean mouseWasDragged = false;
    // The starting location (the center of the map) is Innsbruck
    private Location startingLocation = new Location(FormConfig.XStartLocation, FormConfig.YStartLocation);
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
            UrbanDistrict tmpDistrict = UrbanDistrict.buildDefaultDistrict(this, row);
            boolean districtAlreadyExisting = false;
            for (int districtStelle=0; districtStelle < districts.size(); districtStelle++) {
                if (districts.get(districtStelle).getRegionNumber() == tmpDistrict.getRegionNumber() && !districtAlreadyExisting) {
                    districtAlreadyExisting = true;
                    districts.set(districtStelle, DistrictUtil.addSpecificInhabitans(districts.get(districtStelle), tmpDistrict));
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

    private  void loadSchoolCategoryFromCSV (String path, SchoolBasedCategory schoolBasedCategory) {
        Table schoolData = loadTable(path, "header");
        if (schools == null) schools = new ArrayList<>();
        for (TableRow row : schoolData.rows()) {
            schools.add(new SchoolBasedEducationalInstitution().buildDefaultEducationalInstitution(this, row, schoolBasedCategory));
        }
    }

    private void loadSchoolData() {
        loadSchoolCategoryFromCSV(PathConfig.HIGHER_EDUCATION_CSV_DATA_PATH, SchoolBasedCategory.HIGHER_EDUCATION);
        System.out.println("School Size 1: " +schools.size());
        loadSchoolCategoryFromCSV(PathConfig.SPECIAL_SCHOOLS_CSV_DATA_PATH, SchoolBasedCategory.SPECIAL);
        System.out.println("School Size 2: " +schools.size());
        loadSchoolCategoryFromCSV(PathConfig.ELEMENTARY_SCHOOLS_CSV_DATA_PATH, SchoolBasedCategory.PRIMARY);
        System.out.println("School Size 3: " +schools.size());

    }

    private void loadUniversityData() {
        Table universityData = loadTable(PathConfig.UNIVERSITY_CSV_DATA_PATH, "header");
        universities = new ArrayList<>();
        for (TableRow row : universityData.rows()) {
            universities.add(new UniversityBasedEducationalInstitution().buildDefaultEducationalInstitution(this, row, UniversityBasedCategory.DEFAULT));
        }
    }


    private void addMarkersToMap() {
        markers = MarkerTypeUtil.getAllMarkersForAllMarkerType(this);
        map = UnfoldingMapUtil.addMarkersToUnfoldingMap(map, markers);
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
