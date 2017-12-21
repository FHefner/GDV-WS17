package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;

public class ScatterPlotUniversities extends ScatterPlotAbstract {

    public ScatterPlotUniversities() {
        super("Universitäten", "Einwohner zwischen 20 und 29");
    }


    @Override
    public void addDistrictToPlot(UrbanDistrict district) {
        int totalAmountInhabitants = district.getInhabitantsBetween20And29().get("totalAmountInhabitants");
        colorDataList.add(new PVector(district.getSumUniversities(), district.getInhabitantsBetween20And29().get("totalAmountInhabitants")));
        addToColorDataList(totalAmountInhabitants);
    }


    @Override
    protected void addToColorDataList(int totalAmountInhabitants) {
        inhabitantsForColor.add((float)(totalAmountInhabitants/50));
    }
}
