package de.hsmannheim.util.innsbruckEducation;

import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.marker.MarkerTypeUtil;

import java.util.List;
import java.util.Map;

public class InnsbruckEducationAppUtil {


    public static void setStartingDistrictsAndMarkers(Map<MarkerType, List<Marker>> markers, List<UrbanDistrict> allDistrictsList) {
        DistrictUtil.resetDistrictColors(allDistrictsList);
        MarkerTypeUtil.resetEducationMarkers(markers);
    }
}
