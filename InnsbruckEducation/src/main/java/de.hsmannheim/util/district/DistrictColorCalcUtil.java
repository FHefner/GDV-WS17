package de.hsmannheim.util.district;

import de.hsmannheim.models.UrbanDistrict;
import org.gicentre.utils.colour.ColourTable;

public class DistrictColorCalcUtil {

    public static int calcDistrictColor(UrbanDistrict district) {
        ColourTable colourTable = ColourTable.getPresetColourTable(ColourTable.PU_RD, 0, 10);
        int populationRange = district.getInhabitansBetween6And29().get("totalAmountInhabitants") / 300;
        System.out.println("Pop. range: " + populationRange);
        return colourTable.findColour(populationRange);
    }
}