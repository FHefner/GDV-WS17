package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;

import java.util.Random;

public class ScatterPlotAll extends ScatterPlotAbstract {

    private static final Random r = new Random();

    public ScatterPlotAll() {
        super("Bildungseinrichtungen", "Einwohner zwischen 6 und 29");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district) {
        series.add(r.nextDouble(), (double) DistrictUtil.calculateInhabitantsSum6to29(district));
    }

}
