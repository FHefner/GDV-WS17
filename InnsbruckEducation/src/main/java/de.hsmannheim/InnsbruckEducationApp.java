package de.hsmannheim;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.config.PathConfig;
import de.hsmannheim.markers.LabeledMarker;
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
import de.hsmannheim.util.marker.LabeledMarkerUtil;
import de.hsmannheim.util.marker.MarkerTypeUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import de.hsmannheim.util.plots.ScatterPlotUtil;
import de.hsmannheim.util.plots.Strategies.ScatterPlotAll;
import de.hsmannheim.util.unfoldingMap.UnfoldingMapUtil;
import org.gicentre.utils.stat.XYChart;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private ScatterPlotUtil scatterPlotUtil= new ScatterPlotUtil();
    private ScatterPlotAbstract scatterPlotStrategy;
    private Location currentMapLocation;
    private boolean mouseWasDragged = false;
    private boolean executedAfterFirstDraw = false;
    private XYChart scatterPlotChart;
    private CardsUI cardsUI;
    private boolean[] showSchools = {true, true};
    private boolean[] showUniversities = {true, true};
    private int[] yearToShow = {2017, 2017};
    private boolean sliderMoved = false;
    private PImage colorImage;
    private PImage redImage;
    private PImage whiteImage;
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
        this.scatterPlotChart = new XYChart(this);
        List<PVector> ScatterPlotChartData = scatterPlotStrategy.createDataset(allDistrictsList, year);
        scatterPlotChart.setData(ScatterPlotChartData);
        scatterPlotChart.showXAxis(true);
        scatterPlotChart.showYAxis(true);
        scatterPlotChart.setXAxisLabel(scatterPlotStrategy.getEducationType());
        scatterPlotChart.setYAxisLabel(scatterPlotStrategy.getAgeBand());
        scatterPlotChart.setXAxisAt(0);
        scatterPlotChart.setYAxisAt(0);

        if (!ScatterPlotUtil.districtIsHighlightedInScatterPlot) {
            scatterPlotChart.setPointColour(scatterPlotStrategy.getInhabitantsForColor(), ScatterPlotUtil.colorTable);
        } else {
            scatterPlotChart.setPointColour(ScatterPlotUtil.colorDataWithHighlightedDistrict, ScatterPlotUtil.colorTable);
        }
        scatterPlotChart.setPointSize(10);
    }

    private void changeScatterPlot() {
        this.scatterPlotStrategy = ScatterPlotUtil.changeScatterPlotStrategy(showSchools[1], showUniversities[1]);
        createScatterPlot(yearToShow[1]);
        scatterPlotChart.draw(870, 50, 380, 380);
    }

    private void highlightDistrictInScatterPlot(UrbanDistrict district) {
        ScatterPlotUtil.districtIsHighlightedInScatterPlot = true;
        ScatterPlotUtil.setSpecificColorTable();
        ScatterPlotUtil.colorDataWithHighlightedDistrict = scatterPlotStrategy.createDataSetWithHighlightedDistrict(district);
        scatterPlotChart.setPointColour(ScatterPlotUtil.colorDataWithHighlightedDistrict, ScatterPlotUtil.colorTable);

    }

    private void resetScatterPlot() {
        ScatterPlotUtil.resetColorTable();
        ScatterPlotUtil.districtIsHighlightedInScatterPlot = false;
        changeScatterPlot();
    }

    private void processData() {
        loadSchoolData();
        loadUniversityData();
        loadDistrictsData();
        addMarkersToMap();
    }

    private void hideEducationLabels() {
        if (selectedDistrict != null) {
            List<Marker> educationMarkers = new ArrayList<>(selectedDistrict.getSchoolMarkers());
            educationMarkers.addAll(selectedDistrict.getUniversityMarkers());
            for (Marker marker : educationMarkers) {
                LabeledMarker labeledMarker = (LabeledMarker) marker;
                labeledMarker.setShowLabel(false);
            }
        }

    }

    private void resetView() {
        UnfoldingMapUtil.setPropertiesToMap(map);
        zoomedIntoDistrict = false;
        InnsbruckEducationAppUtil.setStartingDistrictsAndMarkers(markers, allDistrictsList);
        if (executedAfterFirstDraw) {
            hideEducationLabels();
        }
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
        colorImage = loadImage(PathConfig.colorImage, "png");
        redImage = loadImage(PathConfig.redImage, "jpg");
        whiteImage = loadImage(PathConfig.whiteImage, "jpg");
    }

    public void keyPressed() {
        // Kill the app if ESC is pressed
        if (key == ESC) {
            exit();
        }
        // Go Back to start view if BACKSPACE is pressed
        if (key == BACKSPACE) {
            resetView();
            resetScatterPlot();
        }
    }

    private void handleYearSlider(int oldValue, int newValue) {
        if (oldValue != newValue) {
            sliderMoved = true;
            districtUtil.setDistrictColorsBasedOnPopulation(newValue);
            addMarkersToMap();
            changeScatterPlot();
            if (zoomedIntoDistrict)
                for (UrbanDistrict district : allDistrictsList) {
                    changeColorOfSelectedDistrict(district);
                }
        }
    }

    private void handleToggle(boolean oldValue, boolean newValue, MarkerType markerType) {
        if (oldValue != newValue) {
            changeScatterPlot();
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

        if (showUniversities[1] || showSchools[1]) {
            markerInPlotClicked();
        }

        mouseWasDragged = false;
    }

    private void markerInPlotClicked() {
        float[][] plotMarkersPixelCoordinates= scatterPlotUtil.getPlotMarkersPixelCoordinates(scatterPlotChart, showSchools[1], showUniversities[1], yearToShow[0]);
        float[] scatterPlotHovered = InnsbruckEducationAppUtil.getHoveredScatterPlotMarker(mouseX, mouseY, plotMarkersPixelCoordinates);
        if (scatterPlotHovered[0] != -1) {
            try {
                highlightDistrictInScatterPlot(whichDistrictClickedInPlot(scatterPlotUtil.getMarkersAxisCoordinates(scatterPlotHovered[0], scatterPlotHovered[1])));
            } catch (NullPointerException e) {
                System.err.println("District that has been selected couldnt be matched with the Coordinates in the Plot");
            }
        }

    }

    private UrbanDistrict whichDistrictClickedInPlot(int[] markersAxisCoordinates) {
        UrbanDistrict targetDistrict = null;
        for (UrbanDistrict district : allDistrictsList) {
            int totalInhabitantsDistrict = district.getInhabitantsBetween6And29().get(yearToShow[1]).get("totalAmountInhabitants");
            int totalEducationalInstitutionsDistrict = DistrictUtil.getDistrictEduInstitutionsToggleListener(district, showUniversities[1], showSchools[1]);
            if (totalEducationalInstitutionsDistrict == markersAxisCoordinates[0] &&
                    totalInhabitantsDistrict == markersAxisCoordinates[1]) {
                district.setSelected(true);
                targetDistrict = district;
            } else {
                district.setSelected(false);
            }
            changeColorOfSelectedDistrict(district);
        }
        return targetDistrict;
    }

    private void highlightSelectedDistrict() {
        showOrHideMarkers(MarkerType.SCHOOL_MARKER, showSchools[1]);
        showOrHideMarkers(MarkerType.UNIVERSITY_MARKER, showUniversities[1]);
    }

    private int[] extractRGB(UrbanDistrict district) {
        int[] rgb = new int[3];
        rgb[0] = (int) red(district.getColor());
        rgb[1] = (int) green(district.getColor());
        rgb[2] = (int) blue(district.getColor());
        return rgb;
    }

    private void changeColorOfSelectedDistrict(UrbanDistrict district) {
        if (district.getIsSelected()) {
            highlightDistrictInScatterPlot(district);
            selectedDistrict = district;
            map.zoomAndPanToFit(district.getLocationsFromJSONArray());
            zoomedIntoDistrict = true;
            highlightSelectedDistrict();

            district.getMarker().setPolygonColor(district.getMarker().getInitialColor());
        } else {
            if (zoomedIntoDistrict) {
                MarkerTypeUtil.hideEducationMarkersInGivenDistrict(district);
            }
            int rgb[] = extractRGB(district);
            district.getMarker().setPolygonColor(color(rgb[0], rgb[1], rgb[2], 50));
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
        ScatterPlotUtil.createSpecificColorTable();
        this.scatterPlotStrategy = new ScatterPlotAll();
        createScatterPlot(yearToShow[1]);
        executedAfterFirstDraw = true;
    }

    public void mouseDragged() {
        mouseWasDragged = true;
    }

    private void showLabelOnHoveredEducationalInstitution() {
        List<Marker> educationMarkers = new ArrayList<>(selectedDistrict.getSchoolMarkers());
        educationMarkers.addAll(selectedDistrict.getUniversityMarkers());
        LabeledMarker activeMarker = null;
        int amountInside = 0;
        for (Marker marker : educationMarkers) {
            LabeledMarker labeledMarker = (LabeledMarker) marker;
            if (labeledMarker.isInside(map, mouseX, mouseY)) {
                amountInside++;
            }
            if (amountInside == 1 && labeledMarker.isInside(map, mouseX, mouseY)) {
                labeledMarker.setShowLabel(true);
                activeMarker = labeledMarker;
            } else {
                labeledMarker.setShowLabel(false);
            }
        }
        for (Marker marker : educationMarkers) {
            if (activeMarker != null && marker != activeMarker && LabeledMarkerUtil.isMarkerInsideLabeledMarker(map, marker, activeMarker)) {
                marker.setHidden(true);
            }
            if (activeMarker == null) {
                showOrHideMarkers(MarkerType.SCHOOL_MARKER, showSchools[1]);
                showOrHideMarkers(MarkerType.UNIVERSITY_MARKER, showUniversities[1]);
            }
        }
    }

    private void drawElementOnProcessing(int[] fillColor, int[] strokeColor, int x, int y, float width, int height, String geometryString, float strokeWidth) {
        fill(fillColor[0], fillColor[1], fillColor[2]);
        strokeWeight(strokeWidth);
        stroke(strokeColor[0], strokeColor[1], strokeColor[2]);

        try {
            Method method = this.getClass().getMethod(geometryString, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE);
            method.invoke(this, x, y, width, height);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void drawLegend() {
        drawElementOnProcessing(new int[]{51, 64, 80}, new int[]{233, 233, 233}, 870, 450,
                (float) (FormConfig.SIDE_PANEL_WIDTH * 0.95), 145, "rect", 3);

        drawElementOnProcessing(new int[]{117, 157, 221}, new int[]{90, 90, 90}, 1145, 500,
                20, 20, "ellipse", 4);
        cardsUI.Label(SchoolBasedCategory.HIGHER_EDUCATION.toString(), 912, 470);

        cardsUI.Label(SchoolBasedCategory.PRIMARY.toString(), 912, 500);
        drawElementOnProcessing(new int[]{115, 134, 52}, new int[]{90, 90, 90}, 890, 470,
                20, 20, "ellipse", 4);

        cardsUI.Label(SchoolBasedCategory.SPECIAL.toString(), 912, 530);
        drawElementOnProcessing(new int[]{156, 194, 34}, new int[]{90, 90, 90}, 890, 500,
                20, 20, "ellipse", 4);

        cardsUI.Label(UniversityBasedCategory.UNIVERSITY.toString(), 1170, 470);
        drawElementOnProcessing(new int[]{192, 247, 12}, new int[]{90, 90, 90}, 890, 530,
                20, 20, "ellipse", 4);

        cardsUI.Label(UniversityBasedCategory.UAS.toString(), 1170, 500);
        drawElementOnProcessing(new int[]{56, 121, 226}, new int[]{90, 90, 90}, 1145, 470,
                20, 20, "ellipse", 4);

        image(colorImage, 883, 552, colorImage.width * 0.80f, colorImage.height * 0.80f);
        cardsUI.Label("#Bewohner (6-29 Jahre)", 1085, 552);
        image(whiteImage, 1085, 565, redImage.width * 0.15f, redImage.height * 0.15f);
        cardsUI.Label("wenig", 1105, 573);
        image(redImage, 1165, 565, redImage.width * 0.15f, redImage.height * 0.15f);
        cardsUI.Label("viel", 1185, 573);
    }

    public void draw() {
        map.draw();
        // Just for debugging current location
        currentMapLocation = map.getLocation(mouseX, mouseY);
        //Generate White Box for the Background where the ScatterPlot will be displayed
        rect(860, 0, width - 860, height);
        cardsUI.beginCard("InnsbruckEducation", 860, 0, FormConfig.SIDE_PANEL_WIDTH, FormConfig.SIDE_PANEL_HEIGHT);
        if (executedAfterFirstDraw) {
            //generates the ScatterPlot
            scatterPlotChart.draw(870, 50, 380, 380);
        } else {
            executeAfterFirstDraw();
        }
        drawLegend();

        // Interactive UI elements
        cardsUI.Label("Anzuzeigende Bildungseinrichtungen:", FormConfig.SIDE_PANEL_WIDTH, FormConfig.SIDE_PANEL_HEIGHT);
        showSchools[1] = cardsUI.Toggle("Schulen:", showSchools[1], 870, 615);
        showUniversities[1] = cardsUI.Toggle("Unis/Hochschulen:", showUniversities[1], 1045, 615);
        yearToShow[1] = cardsUI.Slider("Jahr: ", 2013, 2017, yearToShow[1], 870, 655, 380, 30);
        cardsUI.Label(String.valueOf(yearToShow[1]), (int) (860 + (FormConfig.SIDE_PANEL_WIDTH / 2) - (textWidth(String.valueOf(yearToShow[1])) / 2)), 700);









        cardsUI.endCard();
        if (zoomedIntoDistrict) {
            showLabelOnHoveredEducationalInstitution();
        }
    }
}
