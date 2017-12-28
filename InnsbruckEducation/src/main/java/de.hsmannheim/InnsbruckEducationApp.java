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
import de.hsmannheim.ui.CardsUI;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.innsbruckEducation.InnsbruckEducationAppUtil;
import de.hsmannheim.util.marker.MarkerTypeUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import de.hsmannheim.util.plots.Strategies.ScatterPlotAll;
import de.hsmannheim.util.unfoldingMap.UnfoldingMapUtil;
import org.gicentre.utils.colour.ColourTable;
import org.gicentre.utils.stat.XYChart;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InnsbruckEducationApp extends PApplet {


    public List<AbstractEducationalInstitution> schools;
    public List<AbstractEducationalInstitution> universities;
    public List<UrbanDistrict> allDistrictsList = new ArrayList<>();
    private UnfoldingMap map;
    private UrbanDistrict selectedDistrict;
    private Map<MarkerType, List<Marker>> markers = new HashMap<>();
    private boolean zoomedIntoDistrict = false;
    private DistrictUtil districtUtil;
    private ScatterPlotAbstract scatterplotStrategy;
    private Location currentMapLocation;
    private boolean mouseWasDragged = false;
    private boolean executedAfterFirstDraw = false;
    private XYChart scatterplotChart;
    private CardsUI cardsUI;
    private boolean[] showSchools = {true, true};
    private boolean[] showUniversities = {true, true};
    private int[] yearToShow = {2017, 2017};
    private boolean sliderMoved = false;

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());
    }

    private void initDistrictUtil() {
        districtUtil = new DistrictUtil(allDistrictsList);
    }

    private void loadDistrictsData() {
        Table districtData = loadTable(PathConfig.BEVOELKERUNG_CSV_DATA_PATH, "header");
        initDistrictUtil();

        for (int i = 0; i < 1000; i++) {
            UrbanDistrict newDistrict = UrbanDistrict.buildDefaultDistrict(this, districtData, i);
            if (newDistrict != null)
                districtUtil.setDistrictInhabitantsInformation(newDistrict, districtData, i);
        }
        districtUtil.setDistrictColorsBasedOnPopulation(yearToShow[1]);
    }

    private void loadSchoolCategoryFromCSV(String path, SchoolBasedCategory schoolBasedCategory) {
        Table schoolData = loadTable(path, "header");
        if (schools == null) schools = new ArrayList<>();
        for (TableRow row : schoolData.rows()) {
            schools.add(new SchoolBasedEducationalInstitution().
                    buildDefaultEducationalInstitution(this, row, schoolBasedCategory));
        }
    }

    private void loadUniversityCategoryFromCSV(String path, UniversityBasedCategory universityBasedCategory) {
        Table universityData = loadTable(path, "header");
        if (universities == null) universities = new ArrayList<>();
        for (TableRow row : universityData.rows()) {
            universities.add(new UniversityBasedEducationalInstitution().
                    buildDefaultEducationalInstitution(this, row, universityBasedCategory));
        }
    }

    private void loadSchoolData() {
        loadSchoolCategoryFromCSV(PathConfig.HIGHER_EDUCATION_CSV_DATA_PATH, SchoolBasedCategory.HIGHER_EDUCATION);
        loadSchoolCategoryFromCSV(PathConfig.SPECIAL_SCHOOLS_CSV_DATA_PATH, SchoolBasedCategory.SPECIAL);
        loadSchoolCategoryFromCSV(PathConfig.ELEMENTARY_SCHOOLS_CSV_DATA_PATH, SchoolBasedCategory.PRIMARY);
    }

    private void loadUniversityData() {
        loadUniversityCategoryFromCSV(PathConfig.UNIVERSITY_CSV_DATA_PATH, UniversityBasedCategory.UNIVERSITY);
        loadUniversityCategoryFromCSV(PathConfig.UAS_CSV_DATA_PATH, UniversityBasedCategory.UAS);
    }

    private void addMarkersToMap() {
        markers = MarkerTypeUtil.getAllMarkersForAllMarkerTypes(this);
        map = UnfoldingMapUtil.addMarkersToUnfoldingMap(map, markers);
    }

    private void createScatterPlot(int year) {
        this.scatterplotChart = new XYChart(this);
        List<PVector> scatterplotChartData = scatterplotStrategy.createDataset(allDistrictsList, year);
        scatterplotChart.setData(scatterplotChartData);
        scatterplotChart.showXAxis(true);
        scatterplotChart.showYAxis(true);
        scatterplotChart.setXAxisLabel(scatterplotStrategy.getEducationType());
        scatterplotChart.setYAxisLabel(scatterplotStrategy.getAgeBand());
        // Symbol styles
        ColourTable colourTable = ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);

        scatterplotChart.setPointColour(scatterplotStrategy.getInhabitantsForColor(), colourTable);
        scatterplotChart.setPointSize(8);

    }

    private void processData() {
        loadSchoolData();
        loadUniversityData();
        loadDistrictsData();
        addMarkersToMap();
    }

    private void resetView() {
        UnfoldingMapUtil.setPropertiesToMap(map);
        zoomedIntoDistrict = false;
        InnsbruckEducationAppUtil.setStartingDistrictsAndMarkers(markers, allDistrictsList);
    }

    private void applyMapSettings() {
        map = new UnfoldingMap(this, "MAIN_MAP", FormConfig.MAP_X_WINDOW_OFFSET, FormConfig.MAP_Y_WINDOW_OFFSET,
                FormConfig.MAP_WIDTH,
                FormConfig.MAP_HEIGHT,
                false, false, new OpenStreetMap.PositronMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void settings() {
        size(FormConfig.WINDOW_WIDTH, FormConfig.WINDOW_HEIGHT, FX2D);
        applyMapSettings();
        processData();
        resetView();
        this.cardsUI = new CardsUI(this);
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

    private void handleYearSlider(int oldValue, int newValue) {
        if (oldValue != newValue) {
            sliderMoved = true;
            districtUtil.setDistrictColorsBasedOnPopulation(newValue);
            addMarkersToMap();
            if (zoomedIntoDistrict)
                for (UrbanDistrict district : allDistrictsList) {
                    changeColorOfSelectedDistrict(district);
                }
        }
    }

    private void handleToggle(boolean oldValue, boolean newValue, MarkerType markerType) {
        if (oldValue != newValue) {
            showOrHideMarkers(markerType, newValue);
        }
    }

    private void saveOldUiElementValues() {
        yearToShow[0] = yearToShow[1];
        showSchools[0] = showSchools[1];
        showUniversities[0] = showUniversities[1];
    }

    public void mouseReleased() {
        if (InnsbruckEducationAppUtil.isMouseInsideSidepanel(mouseX, mouseY)) {
            cardsUI.mouseReleased();
            handleYearSlider(yearToShow[0], yearToShow[1]);
            if (!sliderMoved) {
                handleToggle(showSchools[0], showSchools[1], MarkerType.SCHOOL_MARKER);
                handleToggle(showUniversities[0], showUniversities[1], MarkerType.UNIVERSITY_MARKER);
            }
        }
        sliderMoved = false;
    }

    public void mouseClicked() {
        if (InnsbruckEducationAppUtil.isMouseInsideSidepanel(mouseX, mouseY)) {
            cardsUI.mousePressed();
            saveOldUiElementValues();
        }
        if (InnsbruckEducationAppUtil.isMouseInsideUnfoldingMap(mouseX, mouseY)) {
            districtUtil.checkIfDistrictIsSelected(map, mouseX, mouseY);
            if (districtUtil.isDistrictSelected() && !mouseWasDragged) {
                for (UrbanDistrict district : allDistrictsList) {
                    changeColorOfSelectedDistrict(district);
                }
            }
        }
        mouseWasDragged = false;
    }

    private void highlightSelectedDistrict() {
        showOrHideMarkers(MarkerType.SCHOOL_MARKER, showSchools[1]);
        showOrHideMarkers(MarkerType.UNIVERSITY_MARKER, showUniversities[1]);
    }

    private void changeColorOfSelectedDistrict(UrbanDistrict district) {
        if (district.getIsSelected()) {
            selectedDistrict = district;
            System.out.println(selectedDistrict);
            map.zoomAndPanToFit(district.getLocationsFromJSONArray());
            zoomedIntoDistrict = true;
            highlightSelectedDistrict();
            district.getMarker().setPolygonColor(district.getMarker().getInitialColor());
        } else {
            if (zoomedIntoDistrict) {
                MarkerTypeUtil.hideEducationMarkersInGivenDistrict(district);
            }
            district.getMarker().setPolygonColor(color(90, 90, 90));
        }
    }

    private void showOrHideMarkers(MarkerType markerType, boolean showMarkers) {
        if (zoomedIntoDistrict) {
            if (markerType == MarkerType.SCHOOL_MARKER) {
                for (Marker marker : selectedDistrict.getSchoolMarkers()) {
                    marker.setHidden(!showMarkers);
                }
            }
            if (markerType == MarkerType.UNIVERSITY_MARKER) {
                for (Marker marker : selectedDistrict.getUniversityMarkers()) {
                    marker.setHidden(!showMarkers);
                }
            }
        }
    }

    private void executeAfterFirstDraw() {
        allDistrictsList = DistrictUtil.addEducationalInstitutionsToDistricts(map, schools, universities, allDistrictsList, yearToShow[1]);
        this.scatterplotStrategy = new ScatterPlotAll();
        createScatterPlot(yearToShow[1]);
        executedAfterFirstDraw = true;
    }

    public void mouseDragged() {
        mouseWasDragged = true;
    }

    public void draw() {
        map.draw();
        // Just for debugging current location
        currentMapLocation = map.getLocation(mouseX, mouseY);
        //Generate White Box for the Background where the scatterplot will be displayed
        rect(860, 0, width - 860, height);
        cardsUI.beginCard("InnsbruckEducation", 860, 0, FormConfig.SIDE_PANEL_WIDTH, FormConfig.SIDE_PANEL_HEIGHT);
        if (executedAfterFirstDraw) {
            //generates the Scatterplot
            scatterplotChart.draw(870, 50, 380, 380);
        } else {
            executeAfterFirstDraw();
        }
        cardsUI.Label("Anzuzeigende Bildungseinrichtungen:", FormConfig.SIDE_PANEL_WIDTH, FormConfig.SIDE_PANEL_HEIGHT);
        showSchools[1] = cardsUI.Toggle("Schulen:", showSchools[1], 870, 540);
        showUniversities[1] = cardsUI.Toggle("Unis/Hochschulen:", showUniversities[1], 1050, 540);
        yearToShow[1] = cardsUI.Slider("Anzuzeigendes Jahr: ", 2013, 2017, yearToShow[1], 870, 580, 380, 30);
        cardsUI.Label(String.valueOf(yearToShow[1]), 1110, 630);
        cardsUI.endCard();
    }
}
