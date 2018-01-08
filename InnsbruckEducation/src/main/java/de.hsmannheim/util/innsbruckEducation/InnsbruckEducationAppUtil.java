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

    public static float[] getHoveredScatterPlotMarker(int mouseX, int mouseY, float[][] plotMarkersPixelCoordinates) {
        float[] result = new float[]{-1f, -1f};
        for (int markerIndex = 0; markerIndex < plotMarkersPixelCoordinates.length; markerIndex++) {
            if (isInsidePlotMarker(markerIndex, plotMarkersPixelCoordinates, mouseX, mouseY))
                result = new float[]{plotMarkersPixelCoordinates[markerIndex][0], plotMarkersPixelCoordinates[markerIndex][1]};
        }
        return result;
    }

    public static boolean isInsidePlotMarker(int markerIndex, float[][] plotMarkersCoordinates, int mouseX, int mouseY) {
        int plotMarkerXArea;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0) {
                    plotMarkerXArea = (int) (plotMarkersCoordinates[markerIndex][0] + j);
                } else {
                    plotMarkerXArea = (int) (plotMarkersCoordinates[markerIndex][0] - j);
                }
                if (isInsideYAreaPlotMarker(markerIndex, plotMarkersCoordinates, mouseX, mouseY, plotMarkerXArea))
                    return true;
            }
        }
        return false;
    }

    public static boolean isInsideYAreaPlotMarker(int markerIndex, float[][] plotMarkersCoordinates, int mouseX,
                                                  int mouseY, int plotMarkerXArea) {
        int plotMarkerYArea;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0) {
                    plotMarkerYArea = (int) (plotMarkersCoordinates[markerIndex][1] + j);
                } else {
                    plotMarkerYArea = (int) (plotMarkersCoordinates[markerIndex][1] - j);
                }
                if (plotMarkerXArea == mouseX && plotMarkerYArea == mouseY) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setStartingDistrictsAndMarkers
            (Map<MarkerType, List<Marker>> markers, List<UrbanDistrict> allDistrictsList) {
        DistrictUtil.resetDistrictColors(allDistrictsList);
        MarkerTypeUtil.resetEducationMarkers(markers);
    }
}

