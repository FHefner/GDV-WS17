package de.hsmannheim.util.plots;

import de.hsmannheim.util.plots.Strategies.ScatterPlotAll;
import de.hsmannheim.util.plots.Strategies.ScatterPlotEmpty;
import de.hsmannheim.util.plots.Strategies.ScatterPlotSchools;
import de.hsmannheim.util.plots.Strategies.ScatterPlotUniversities;
import org.gicentre.utils.colour.ColourTable;
import org.gicentre.utils.stat.XYChart;

import java.util.ArrayList;
import java.util.List;

public class ScatterPlotUtil {
    public static ColourTable colorTable = ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
    public static float[] colorDataWithHighlightedDistrict;
    public static boolean districtIsHighlightedInScatterPlot = false;
    private static List<Float[]> markerAxisAndPixelCoordinates = new ArrayList<>(4);


    public static void resetColorTable() {
        colorTable = ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
    }

    public static void setSpecificColorTable() {
        colorTable = createSpecificColorTable();
    }

    public static ScatterPlotAbstract changeScatterPlotStrategy(boolean showSchools, boolean showUniversities) {
        ScatterPlotAbstract ScatterPlotStrategy;
        if (showSchools && showUniversities) {
            ScatterPlotStrategy = new ScatterPlotAll();
        } else if (showSchools) {
            ScatterPlotStrategy = new ScatterPlotSchools();
        } else if (showUniversities) {
            ScatterPlotStrategy = new ScatterPlotUniversities();
        } else {
            ScatterPlotStrategy = new ScatterPlotEmpty();
        }
        return ScatterPlotStrategy;
    }

    public static ColourTable createSpecificColorTable() {
        ColourTable colourTableNew = new ColourTable();
        colourTableNew.addContinuousColourRule(0, 247, 244, 249, 70);
        colourTableNew.addContinuousColourRule(1, 247, 244, 249, 70);
        colourTableNew.addContinuousColourRule(2, 231, 225, 239, 70);
        colourTableNew.addContinuousColourRule(3, 212, 185, 218, 70);
        colourTableNew.addContinuousColourRule(4, 201, 148, 199, 70);
        colourTableNew.addContinuousColourRule(5, 223, 101, 176, 70);
        colourTableNew.addContinuousColourRule(6, 231, 41, 138, 70);
        colourTableNew.addContinuousColourRule(7, 206, 18, 86, 70);
        colourTableNew.addContinuousColourRule(8, 152, 0, 67, 70);
        colourTableNew.addContinuousColourRule(9, 103, 0, 31, 70);
        colourTableNew.addContinuousColourRule(10, 103, 0, 31, 70);
        colourTableNew.addContinuousColourRule(-1, 255, 0, 0, 255);
        return colourTableNew;
    }

    public float[][] getMarkerPixelCoordinates(final XYChart scatterPlotChart, final int maxPopulation, final int maxEducationalInstitutions) {
        float[][] markerPixelCoordinates = new float[20][2];
        float[][] markerAxisCoordinates = initializeMarkerAxisCoordinates(scatterPlotChart.getXData(), scatterPlotChart.getYData());
        float[] pixelPerUnitXY = getPixelsPerUnitXY(maxEducationalInstitutions, maxPopulation);
        for (int i = 0; i < markerPixelCoordinates.length; i++) {
            markerPixelCoordinates[i][0] = 935 + pixelPerUnitXY[0] * markerAxisCoordinates[i][0];
            markerPixelCoordinates[i][1] = 395 - pixelPerUnitXY[1] * markerAxisCoordinates[i][1];
            markerAxisAndPixelCoordinates.add(new Float[]{markerPixelCoordinates[i][0], markerPixelCoordinates[i][1], markerAxisCoordinates[i][0], markerAxisCoordinates[i][1],});
        }
        return markerPixelCoordinates;
    }


    public int[] getMarkersAxisCoordinates(float xCoordinates, float yCoordinates) {
        for (int i = 0; i < markerAxisAndPixelCoordinates.size(); i++) {
            if (xCoordinates == markerAxisAndPixelCoordinates.get(i)[0] && yCoordinates == markerAxisAndPixelCoordinates.get(i)[1])
                return new int[]{markerAxisAndPixelCoordinates.get(i)[2].intValue(), markerAxisAndPixelCoordinates.get(i)[3].intValue()};
        }
        return new int[]{-1, -1};
    }

    private float[][] initializeMarkerAxisCoordinates(final float[] xData, final float[] yData) {
        float[][] markerAxisCoordinates = new float[20][2];
        for (int i = 0; i < 20; i++) {
            markerAxisCoordinates[i][0] = xData[i];
            markerAxisCoordinates[i][1] = yData[i];
        }
        return markerAxisCoordinates;
    }

    private float[] getPixelsPerUnitXY(int maxEducationalInstitutions, int maxPopulation) {
        int[] coordinatePointZero = {928, 380};
        int[] coordinatesMaxYAxis = {935, 65};
        int[] coordinatesMaxXAxis = {1245, 395};
        float pixelPerUnitX = (float) (coordinatesMaxXAxis[0] - coordinatePointZero[0]) / maxEducationalInstitutions;
        float pixelPerUnitY = (float) (coordinatePointZero[1] - coordinatesMaxYAxis[1]) / maxPopulation;

        return new float[]{pixelPerUnitX, pixelPerUnitY};
    }

    public float[][] getPlotMarkersPixelCoordinates(XYChart scatterPlotChart, boolean showSchool, boolean showUniversity, int year) {
        float[][] plotMarkersPixelCoordinates;
        int maxPopulation = this.getPopulationMaxForAxis(year);
        if (showSchool && showUniversity) {
            plotMarkersPixelCoordinates = getMarkerPixelCoordinates(scatterPlotChart, maxPopulation, 20);
        } else {
            plotMarkersPixelCoordinates = getMarkerPixelCoordinates(scatterPlotChart, maxPopulation, 10);
        }
        return plotMarkersPixelCoordinates;
    }

    private int getPopulationMaxForAxis(int year) {
        if (year > 2014) {
            return 7000;
        }
        return 6000;
    }
}