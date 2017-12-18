package de.hsmannheim.util.district;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.UrbanDistrict;
import processing.core.PApplet;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DistrictUtil {

    private final List<UrbanDistrict> allDistrictsList;
    private boolean districtSelected = false;
    private boolean districtAlreadyExisting;

    public DistrictUtil(List<UrbanDistrict> districts) {
        this.allDistrictsList = districts;
    }

    public static UrbanDistrict addSpecificInhabitans(UrbanDistrict district, UrbanDistrict tmpDistrict) {
        for (Map.Entry<String, Integer> mapEntry : district.getInhabitansBetween6And29().entrySet()) {
            if (keyIsInhabitant(mapEntry.getKey())) {
                district.getInhabitansBetween6And29().put(
                        mapEntry.getKey(),
                        (mapEntry.getValue() + tmpDistrict.getInhabitansBetween6And29().get(mapEntry.getKey()))
                );
            }
        }
        return district;
    }

    protected static boolean keyIsInhabitant(String key) {
        return key.contains("amountInhabitants");
    }

    public static Integer calculateInhabitantsSum(UrbanDistrict urbanDistrict) {
        return sumAll(urbanDistrict.getInhabitansBetween6And29().values());
    }

    protected static int sumAll(Collection<Integer> values) {
        int result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result;
    }

    public static void resetDistrictColors(List<UrbanDistrict> allDistrictsList) {
        for (UrbanDistrict district : allDistrictsList)
            district.getMarker().resetColor();
    }

    public boolean isDistrictSelected() {
        return districtSelected;
    }

    public void checkIfDistrictIsSelected(UnfoldingMap map, int mouseX, int mouseY) {
        districtSelected = false;
        for (UrbanDistrict district : allDistrictsList) {
            ColoredPolygonMarker districtMarker = district.getMarker();
            if (districtMarker.isInside(map, mouseX, mouseY)) {
                district.setSelected(true);
                districtSelected = true;
            } else
                district.setSelected(false);
        }
    }

    public void setDistrictInhabitantsInformation(UrbanDistrict tmpDistrict) {
        districtAlreadyExisting = false;
        int districtIndex = 0;
        while (!districtAlreadyExisting && districtIndex < allDistrictsList.size()) {
            if (checkIfSameDistrict(allDistrictsList.get(districtIndex), tmpDistrict))
                setInhabitants(districtIndex, tmpDistrict);
            districtIndex += 1;
        }
        mapDuplicatedAreasInAllDistrictsList(tmpDistrict);
    }

    private void setInhabitants(int districtIndex, UrbanDistrict tmpDistrict) {
        allDistrictsList.set(districtIndex, addSpecificInhabitans(allDistrictsList.get(districtIndex), tmpDistrict));
        districtAlreadyExisting = true;
    }

    private boolean checkIfSameDistrict(UrbanDistrict urbanDistrict1, UrbanDistrict urbanDistrict2) {
        return urbanDistrict1.getRegionNumber() == urbanDistrict2.getRegionNumber();
    }

    private void mapDuplicatedAreasInAllDistrictsList(UrbanDistrict tmpDistrict) {
        if (!districtAlreadyExisting)
            allDistrictsList.add(tmpDistrict);
    }

    public void setDistrictColorsBasedOnPopulation(PApplet papplet) {
        for (UrbanDistrict district : allDistrictsList) {
            district.calculateTotalInhabitants();
            district.setColor(DistrictColorCalcUtil.calcDistrictColor(district, papplet));
            district.createPolygonMarker();
        }
    }
}



