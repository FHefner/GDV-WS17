package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.plots.ScatterPlotAbstract;

import java.util.Random;

public class ScatterPlotUniversities extends ScatterPlotAbstract {

    private static final Random r = new Random();

    public ScatterPlotUniversities() {
        super("Universitäten", "Einwohner zwischen 20 und 29");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district) {
        series.add(r.nextDouble(), (double) district.getInhabitantsBetween6And29().get("totalAmountInhabitants"));
    }
}
