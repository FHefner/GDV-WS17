package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;

public class ScatterPlotUniversities extends ScatterPlotAbstract {

    public ScatterPlotUniversities() {
        super("Universitäten", "Einwohner zwischen 20 und 29");
    }


    @Override
    public void addDistrictToPlot(UrbanDistrict district, Integer year) {
        int totalAmountInhabitants = DistrictUtil.getTotalInhabitants20To29(district, year);
        colorDataList.add(new PVector(district.getSumUniversities(), totalAmountInhabitants));
        addToColorDataList(totalAmountInhabitants);
    }


    @Override
    protected void addToColorDataList(int totalAmountInhabitants) {
        inhabitantsForColor.add((float)(totalAmountInhabitants/50));
    }
}
