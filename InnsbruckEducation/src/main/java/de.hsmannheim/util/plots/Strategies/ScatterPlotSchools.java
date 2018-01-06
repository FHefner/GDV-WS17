package de.hsmannheim.util.plots.Strategies;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.plots.ScatterPlotAbstract;
import processing.core.PVector;


public class ScatterPlotSchools extends ScatterPlotAbstract {

    public ScatterPlotSchools() {
        //   super("Schulen", "Einwohner zwischen 6 und 19");
        super("Schulen", "Einwohner zwischen 6 und 29");
    }

    @Override
    public void addDistrictToPlot(UrbanDistrict district, Integer year) {
        //  int totalAmountInhabitants = DistrictUtil.getTotalInhabitants6To19(district, year);

        int totalAmountInhabitants = district.getInhabitantsBetween6And29().get(year).get("totalAmountInhabitants");
        colorDataList.add(new PVector(district.getSumSchools(), district.getInhabitantsBetween6And29().get(year).get("totalAmountInhabitants")));
        addToColorDataList(totalAmountInhabitants, district);
    }

    @Override
    protected void addToColorDataList(int totalAmountInhabitants, UrbanDistrict district) {
        // inhabitantsForColor.add((float)(totalAmountInhabitants/12.5));
        inhabitantsForColor.add( (float) totalAmountInhabitants / 300);
        inhabitantsForColorWithName.put( district.getName() , (float) totalAmountInhabitants / 300);
    }
}

