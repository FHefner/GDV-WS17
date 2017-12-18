package de.hsmannheim.util.district;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.util.marker.MarkerScreenLocationUtil;
import processing.core.PApplet;

import java.util.ArrayList;
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

    public static UrbanDistrict addSpecificInhabitants(UrbanDistrict district, UrbanDistrict tmpDistrict) {
        for (Map.Entry<String, Integer> mapEntry : district.getInhabitantsBetween6And29().entrySet()) {
            if (keyIsInhabitant(mapEntry.getKey())) {
                district.getInhabitantsBetween6And29().put(
                        mapEntry.getKey(),
                        (mapEntry.getValue() + tmpDistrict.getInhabitantsBetween6And29().get(mapEntry.getKey()))
                );
            }
        }
        return district;
    }

    protected static boolean keyIsInhabitant(String key) {
        return key.contains("amountInhabitants");
    }

    public static Integer calculateInhabitantsSum(UrbanDistrict urbanDistrict) {
        return sumAll(urbanDistrict.getInhabitantsBetween6And29().values());
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
        allDistrictsList.set(districtIndex, addSpecificInhabitants(allDistrictsList.get(districtIndex), tmpDistrict));
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
            district.setColor(DistrictColorCalcUtil.calcDistrictColor(district));
            district.createPolygonMarker();
        }
    }

    public static Integer calculateInhabitantsSum6to29(UrbanDistrict district) {
        int sum = 0;
        for (String key : district.getInhabitantsBetween6And29().keySet()) {
            sum += district.getInhabitantsBetween6And29().get(key);
        }
        return sum;
    }

    public static Object calculateInhabitantsSum6to19(UrbanDistrict district) {
        int sum = 0;
        for (String key : district.getInhabitantsBetween6And19().keySet()) {
            sum += district.getInhabitantsBetween6And19().get(key);
        }
        return sum;
    }

    private static List<AbstractEducationalInstitution> getDistrictEdus(UnfoldingMap map, UrbanDistrict district, List<AbstractEducationalInstitution> institutions) {
        List<AbstractEducationalInstitution> institutionList = new ArrayList<>();
        for (AbstractEducationalInstitution institution : institutions) {
            float markerXPosition = MarkerScreenLocationUtil.getScreenXPositionFromMarker(map, institution.getMarker());
            float markerYPosition = MarkerScreenLocationUtil.getScreenYPositionFromMarker(map, institution.getMarker());
            if (district.getMarker().isInside(map, markerXPosition, markerYPosition)) {
                institutionList.add(institution);
            }
        }
        return institutionList;
    }

    public static List<UrbanDistrict> addEducationalInstitutionsToDistricts(UnfoldingMap map, List<AbstractEducationalInstitution> schools, List<AbstractEducationalInstitution> universities, List<UrbanDistrict> allDistrictsList) {
        List<AbstractEducationalInstitution> schoolsAndUniversities = new ArrayList<>(schools);
        schoolsAndUniversities.addAll(universities);
        List<UrbanDistrict> modifiedDistricts = new ArrayList<>();
        for (UrbanDistrict district : allDistrictsList) {
            district.getSchools().addAll(getDistrictEdus(map, district, schoolsAndUniversities));
            modifiedDistricts.add(district);
        }
        return modifiedDistricts;
    }
}



