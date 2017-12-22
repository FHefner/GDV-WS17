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
    private boolean showSchools = true;
    private boolean showUniversities = true;
    private int currentYearToShow = 2017;
    private int oldYearToShow = -1;

    public static void main(String[] args) {
        PApplet.main(InnsbruckEducationApp.class.getName());
    }

    private void initDistrictUtil() {
        districtUtil = new DistrictUtil(allDistrictsList);
    }

    private void loadDistrictsData() {
        Table districtData = loadTable(PathConfig.BEVOELKERUNG_CSV_DATA_PATH, "header");
        initDistrictUtil();
        for (TableRow row : districtData.rows()) {
            UrbanDistrict tmpDistrict = UrbanDistrict.buildDefaultDistrict(this, row);
            districtUtil.setDistrictInhabitantsInformation(tmpDistrict);
        }
        districtUtil.setDistrictColorsBasedOnPopulation();
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
        markers = MarkerTypeUtil.getAllMarkersForAllMarkerType(this);
        map = UnfoldingMapUtil.addMarkersToUnfoldingMap(map, markers);
    }

    private void createScatterPlot() {
        this.scatterplotChart = new XYChart(this);
        List<PVector> scatterplotChartData = scatterplotStrategy.createDataset(allDistrictsList);
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
        // this.controlFrame.activateAllCheckboxes();
    }

    private void applyMapSettings() {
        map = new UnfoldingMap(this, "MAIN_MAP", FormConfig.MAP_X_WINDOW_OFFSET, FormConfig.MAP_Y_WINDOW_OFFSET,
                FormConfig.WINDOW_WIDTH - FormConfig.MAP_X_WINDOW_OFFSET - FormConfig.SIDE_PANEL_WIDTH,
                FormConfig.WINDOW_HEIGHT - FormConfig.MAP_Y_WINDOW_OFFSET,
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

    public void mouseReleased() {
        cardsUI.mouseReleased();
        if (currentYearToShow != oldYearToShow) {
            System.out.println("Now selected the year " + currentYearToShow);
        }
    }

    public void mouseClicked() {
        cardsUI.mousePressed();
        oldYearToShow = currentYearToShow;
        districtUtil.checkIfDistrictIsSelected(map, mouseX, mouseY);
        if (districtUtil.isDistrictSelected() && !mouseWasDragged) {
            for (UrbanDistrict district : allDistrictsList) {
                changeColorOfSelectedDistrict(district);
            }
        }
        mouseWasDragged = false;
    }

    private void changeColorOfSelectedDistrict(UrbanDistrict district) {
        if (district.getIsSelected()) {
            selectedDistrict = district;
            map.zoomAndPanToFit(district.getLocationsFromJSONArray());
            zoomedIntoDistrict = true;
            MarkerTypeUtil.showEducationalInstitutionsInSelectedDistrict(markers, map, selectedDistrict);
            district.getMarker().setPolygonColor(district.getMarker().getInitialColor());
        } else {
            district.getMarker().setPolygonColor(color(90, 90, 90));
        }
    }

    private void executeAfterFirstDraw() {
        allDistrictsList = DistrictUtil.addEducationalInstitutionsToDistricts(map, schools, universities, allDistrictsList);
        this.scatterplotStrategy = new ScatterPlotAll();
        createScatterPlot();
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
        rect(800, -1, 500, 730);
        cardsUI.beginCard("InnsbruckEducation", 800, 0, width - 800, height);
        if (executedAfterFirstDraw) {
            //generates the Scatterplot
            scatterplotChart.draw(810, 50, width - 850, height - 300);
        } else {
            executeAfterFirstDraw();
        }
        cardsUI.Label("Anzuzeigende Bildungseinrichtungen:", 810, 520);
        showSchools = cardsUI.Toggle("Schulen:", showSchools, 810, 540);
        showUniversities = cardsUI.Toggle("Unis/Hochschulen:", showUniversities, 1000, 540);
        currentYearToShow = cardsUI.Slider("Anzuzeigendes Jahr: " , 2013, 2017, currentYearToShow, 810, 580, 450, 30);
        cardsUI.Label(String.valueOf(currentYearToShow), 1050, 630);
        cardsUI.endCard();
    }
}
