package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;

public class ScatterPlotAll extends ScatterPlotAbstract {

    public ScatterPlotAll() {
        super("Bildungseinrichtungen", "Einwohner zwischen 6 und 29");
    }

    @Override
    protected void addDistrictToPlot(UrbanDistrict district, Integer year) {
        int totalAmountInhabitants = district.getInhabitantsBetween6And29().get(year).get("totalAmountInhabitants");
        colorDataList.add(new PVector(district.getSumEducationalInstitutions(), totalAmountInhabitants));
        addToColorDataList(totalAmountInhabitants);
    }

    @Override
    protected void addToColorDataList(int totalAmountInhabitants) {
        inhabitantsForColor.add((float) totalAmountInhabitants / 300);
    }

}
