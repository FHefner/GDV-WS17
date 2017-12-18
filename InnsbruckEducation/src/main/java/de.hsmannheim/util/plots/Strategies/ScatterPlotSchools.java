package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;

import java.util.Random;

public class ScatterPlotSchools extends ScatterPlotAbstract {

    private static final Random r = new Random();

    public ScatterPlotSchools() {
        super("Schulen", "Einwohner zwischen 6 und 19");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district) {
        series.add(r.nextDouble(), (double) DistrictUtil.calculateInhabitantsSum6to19(district));
    }
}
