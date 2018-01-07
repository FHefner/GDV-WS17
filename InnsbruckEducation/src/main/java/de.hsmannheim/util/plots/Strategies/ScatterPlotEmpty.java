package de.hsmannheim.util.plots.Strategies;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;

public class ScatterPlotEmpty extends ScatterPlotAbstract {
    public ScatterPlotEmpty() {
        super("Keine Bildungseinrichtungen ausgewählt!", "Keine Daten zu zeigen!");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district, Integer year) {

    }

    @Override
    protected void addToColorDataList(int totalAmountInhabitants, UrbanDistrict district) {

    }
}
