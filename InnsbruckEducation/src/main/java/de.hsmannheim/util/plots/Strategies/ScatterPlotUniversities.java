package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;
import java.util.Random;

public class ScatterPlotUniversities extends ScatterPlotAbstract {

    private static final Random r = new Random();

    public ScatterPlotUniversities() {
        super("Universitäten", "Einwohner zwischen 20 und 29");
    }

    @Override
    public void addDistrcitToPlot(UrbanDistrict district) {
        series.add(r.nextDouble(), (double) DistrictUtil.calculateInhabitantsSum20to29(district));
    }
}
