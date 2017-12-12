package de.hsmannheim.util.district;

import de.hsmannheim.models.UrbanDistrict;
import processing.core.PApplet;

public class DistrictColorCalcUtil {

    public static int calcDistrictColor(UrbanDistrict district, PApplet applet) {
        int populationRange = district.getInhabitansBetween6And29().get("totalAmountInhabitants") / 300;
        //  int red = 100 + populationRange *7;
        int alpha = 120 + populationRange * 7;
        //  int green = 92 - populationRange * 4;
        // int blue = 100 + populationRange * 7;

        //                   red   green    blue   alpha);
        return applet.color(0, 0, 255, alpha);

    }


}

//139-5952
//128-250
//20
// 300 = 128
// 600 = 134