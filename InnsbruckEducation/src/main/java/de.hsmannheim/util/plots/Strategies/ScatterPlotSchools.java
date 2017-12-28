package de.hsmannheim.util.plots.Strategies;


import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;


public class ScatterPlotSchools extends ScatterPlotAbstract {


    public ScatterPlotSchools() {
        super("Schulen", "Einwohner zwischen 6 und 19");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district, Integer year) {
        int totalAmountInhabitants = DistrictUtil.getTotalInhabitants6To19(district, year);
        colorDataList.add(new PVector(district.getSumSchools(), district.getInhabitantsBetween6And29().get(year).get("totalAmountInhabitants")));
        addToColorDataList(totalAmountInhabitants);
    }

    @Override
    protected void addToColorDataList(int totalAmountInhabitants) {
        inhabitantsForColor.add((float)(totalAmountInhabitants/12.5));
    }
}
