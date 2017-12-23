package de.hsmannheim.util.innsbruckEducation;

import de.fhpotsdam.unfolding.marker.Marker;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.markers.MarkerType;
import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import de.hsmannheim.util.marker.MarkerTypeUtil;

import java.util.List;
import java.util.Map;

public class InnsbruckEducationAppUtil {


    public static boolean isMouseInsideUnfoldingMap(int mouseX, int mouseY) {
        boolean insideXRange = mouseX >= FormConfig.MAP_X_WINDOW_OFFSET && mouseX <= FormConfig.MAP_WIDTH;
        boolean insideYRange = mouseY >= FormConfig.MAP_Y_WINDOW_OFFSET && mouseY <= FormConfig.MAP_HEIGHT;
        return insideXRange && insideYRange;
    }

    public static boolean isMouseInsideSidepanel(int mouseX, int mouseY) {
        boolean insideXRange = mouseX >= (FormConfig.MAP_WIDTH + FormConfig.MAP_X_WINDOW_OFFSET)
                && mouseX <= (FormConfig.MAP_WIDTH + FormConfig.MAP_X_WINDOW_OFFSET + FormConfig.SIDE_PANEL_WIDTH);
        boolean insideYRange = mouseY >= FormConfig.MAP_Y_WINDOW_OFFSET && mouseY <= FormConfig.SIDE_PANEL_HEIGHT;
        return insideXRange && insideYRange;
    }

    public static void setStartingDistrictsAndMarkers(Map<MarkerType, List<Marker>> markers, List<UrbanDistrict> allDistrictsList) {
        DistrictUtil.resetDistrictColors(allDistrictsList);
        MarkerTypeUtil.resetEducationMarkers(markers);
    }
}
