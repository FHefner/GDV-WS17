package de.hsmannheim.util.district;

import de.hsmannheim.models.UrbanDistrict;
import org.gicentre.utils.colour.ColourTable;

public class DistrictColorCalcUtil {

    public static int calcDistrictColor(UrbanDistrict district, Integer year) {
        ColourTable colourTable = ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
        int populationRange = district.getInhabitantsBetween6And29().get(year).get("totalAmountInhabitants") / 300;
        return colourTable.findColour(populationRange);
    }
}