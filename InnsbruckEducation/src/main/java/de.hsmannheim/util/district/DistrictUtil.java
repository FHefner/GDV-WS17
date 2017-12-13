package de.hsmannheim.util.district;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.hsmannheim.markers.ColoredPolygonMarker;
import de.hsmannheim.models.UrbanDistrict;

import java.util.List;

public class DistrictUtil {


    private final List<UrbanDistrict> districts;

    public boolean isDistrictSelected() {
        return districtSelected;
    }

    private boolean districtSelected=false;

    public DistrictUtil(List<UrbanDistrict> districts) {
        this.districts=districts;
    }

    public void checkIfDistrictIsSelected(UnfoldingMap map, int mouseX, int mouseY) {
        districtSelected=false;
        for (UrbanDistrict district: districts){
            ColoredPolygonMarker districtMarker = district.getMarker();
            if (districtMarker.isInside(map, mouseX, mouseY)){
                district.setSelected(true);
                districtSelected=true;
            }
            else{
                district.setSelected(false);
            }

        }
    }

}