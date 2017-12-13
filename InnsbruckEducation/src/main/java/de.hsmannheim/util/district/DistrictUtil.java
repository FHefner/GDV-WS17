package de.hsmannheim.util.district;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.UrbanDistrict;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DistrictUtil {

    private final List<UrbanDistrict> districts;
    private boolean districtSelected = false;

    public DistrictUtil(List<UrbanDistrict> districts) {
        this.districts = districts;
    }

    public static void addSpecificInhabitans(UrbanDistrict district, UrbanDistrict tmpDistrict) {
        for (Map.Entry<String, Integer> mapEntry : district.getInhabitansBetween6And29().entrySet()) {
            if (keyIsInhabitant(mapEntry.getKey()))
                district.getInhabitansBetween6And29().put(mapEntry.getKey(),
                        district.getInhabitansBetween6And29().put(
                                mapEntry.getKey(),
                                mapEntry.getValue() + tmpDistrict.getInhabitansBetween6And29().get(mapEntry.getKey())));
        }

    }

    protected static boolean keyIsInhabitant(String key) {
        return key.contains("amountInhabitants");
    }

    public static Integer calculateInhabitansSum(UrbanDistrict urbanDistrict) {
        return sumAll(urbanDistrict.getInhabitansBetween6And29().values());
    }

    protected static int sumAll(Collection<Integer> values) {
        int result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result;
    }

    public boolean isDistrictSelected() {
        return districtSelected;
    }

    public void checkIfDistrictIsSelected(UnfoldingMap map, int mouseX, int mouseY) {
        districtSelected = false;
        for (UrbanDistrict district : districts) {
            ColoredPolygonMarker districtMarker = district.getMarker();
            if (districtMarker.isInside(map, mouseX, mouseY)) {
                district.setSelected(true);
                districtSelected = true;
            } else
                district.setSelected(false);
        }
    }
}

