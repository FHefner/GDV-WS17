package de.hsmannheim.util.district;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.util.marker.MarkerScreenLocationUtil;
import processing.data.Table;
import processing.data.TableRow;

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

    public static UrbanDistrict addSpecificInhabitants(UrbanDistrict district, UrbanDistrict tmpDistrict, int year) {
        for (Map.Entry<String, Integer> mapEntry : district.getInhabitantsBetween6And29().get(year).entrySet()) {
            if (keyIsInhabitant(mapEntry.getKey())) {
                district.getInhabitantsBetween6And29().get(year).put(
                        mapEntry.getKey(),
                        (mapEntry.getValue() + tmpDistrict.getInhabitantsBetween6And29().get(year).get(mapEntry.getKey()))
                );
            }
        }
        return district;
    }

    protected static boolean keyIsInhabitant(String key) {
        return key.contains("amountInhabitants");
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

    public static Integer calculateInhabitantsSum6to29(UrbanDistrict urbanDistrict, Integer year) {
        return sumAll(urbanDistrict.getInhabitantsBetween6And29().get(year).values());
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

    public static List<UrbanDistrict> addEducationalInstitutionsToDistricts(UnfoldingMap map, List<AbstractEducationalInstitution> schools,
                                                                            List<AbstractEducationalInstitution> universities, List<UrbanDistrict> allDistrictsList, int year) {
        List<AbstractEducationalInstitution> schoolsAndUniversities = new ArrayList<>(schools);
        schoolsAndUniversities.addAll(universities);
        List<UrbanDistrict> modifiedDistricts = new ArrayList<>();
        for (UrbanDistrict district : allDistrictsList) {
            district.getUniversities().addAll(getDistrictEdus(map, district, universities));
            district.getSchools().addAll(getDistrictEdus(map, district, schools));
            modifiedDistricts.add(district);
        }
        return modifiedDistricts;
    }

    public static int getTotalInhabitants20To29(UrbanDistrict district, Integer year) {
        return district.getInhabitantsBetween6And29().get(year).get("amountInhabitants20To24") +
                district.getInhabitantsBetween6And29().get(year).get("amountInhabitants25To29");
    }

    public static int getTotalInhabitants6To19(UrbanDistrict district, Integer year) {
        return district.getInhabitantsBetween6And29().get(year).get("amountInhabitants6To9") +
                district.getInhabitantsBetween6And29().get(year).get("amountInhabitants10To14") +
                district.getInhabitantsBetween6And29().get(year).get("amountInhabitants15To19");
    }

    public static boolean isYearInMap(Map<Integer, Map<String, Integer>> inhabitantsBetween6And29, Integer year) {
        boolean result = false;
        for (Map.Entry<Integer, Map<String, Integer>> entries : inhabitantsBetween6And29.entrySet()) {
            if (entries.getKey().equals(year))
                result = true;
        }
        return result;
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

    public void setDistrictInhabitantsInformation(UrbanDistrict tmpDistrict, Table table, Integer currentZSPR) {

        for (TableRow row : table.rows()) {
            if (row.getInt("ZSPR") == currentZSPR) {
                districtAlreadyExisting = false;
                int districtIndex = 0;
                while (!districtAlreadyExisting && districtIndex < allDistrictsList.size()) {
                    if (checkIfSameDistrict(allDistrictsList.get(districtIndex), tmpDistrict))
                        setInhabitants(districtIndex, tmpDistrict, row.getInt("year"));
                    districtIndex += 1;
                }
                mapDuplicatedAreasInAllDistrictsList(tmpDistrict);
            }
        }


    }

    private void setInhabitants(int districtIndex, UrbanDistrict tmpDistrict, int year) {
        allDistrictsList.set(districtIndex, addSpecificInhabitants(allDistrictsList.get(districtIndex), tmpDistrict, year));
        districtAlreadyExisting = true;
    }

    private boolean checkIfSameDistrict(UrbanDistrict urbanDistrict1, UrbanDistrict urbanDistrict2) {
        return urbanDistrict1.getRegionNumber() == urbanDistrict2.getRegionNumber();
    }

    private void mapDuplicatedAreasInAllDistrictsList(UrbanDistrict tmpDistrict) {
        if (!districtAlreadyExisting)
            allDistrictsList.add(tmpDistrict);
    }

    public void setDistrictColorsBasedOnPopulation(Integer year) {
        for (UrbanDistrict district : allDistrictsList) {
            district.calculateTotalInhabitantsForGivenYear(year);
            district.setColor(DistrictColorCalcUtil.calcDistrictColor(district, year));
            district.createPolygonMarker();
        }
    }
}



