package de.hsmannheim.util.district;

import de.hsmannheim.models.UrbanDistrict;
import processing.core.PApplet;

public class DistrictColorCalcUtil {

    public static int calcDistrictColor(UrbanDistrict district, PApplet applet) {
        int populationRange = district.getInhabitantsBetween6And29().get("totalAmountInhabitants") / 300;
        int alpha = 120 + populationRange * 7;
        return applet.color(0, 0, 255, alpha);
    }
}