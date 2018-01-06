package de.hsmannheim.util.plots;

import de.hsmannheim.util.plots.Strategies.ScatterPlotAll;
import de.hsmannheim.util.plots.Strategies.ScatterPlotEmpty;
import de.hsmannheim.util.plots.Strategies.ScatterPlotSchools;
import de.hsmannheim.util.plots.Strategies.ScatterPlotUniversities;
import org.gicentre.utils.colour.ColourTable;

public class ScatterPlotUtil {
    public static ColourTable colorTable=ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
    public static float[] colorDataWithHighlightedDistrict;
    public static boolean districtIsHighlightedInScatterPlot = false;


    public static void resetColorTable(){
        colorTable= ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
    }

    public static void setSpecificColorTable(){
        colorTable=createSpecificColorTable();
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

}