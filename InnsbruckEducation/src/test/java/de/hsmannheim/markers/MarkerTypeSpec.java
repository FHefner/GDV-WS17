package de.hsmannheim.markers;

import org.junit.Test;

import java.lang.reflect.Field;

public class MarkerTypeSpec {


    @Test
    public void testMarkerTypeList() {
        assert MarkerType.values().length == 3;
    }

    @Test
    public void getMarkerTypeToField() throws Exception {
        assert fieldTypeIsList(MarkerType.DISTRICT_MARKER.name()) &&
                fieldNameEquals(MarkerType.DISTRICT_MARKER.name(), "allDistrictsList");
        assert fieldTypeIsList(MarkerType.SCHOOL_MARKER.name()) &&
                fieldNameEquals(MarkerType.SCHOOL_MARKER.name(), "schools");
        assert fieldTypeIsList(MarkerType.UNIVERSITY_MARKER.name()) &&
                fieldNameEquals(MarkerType.UNIVERSITY_MARKER.name(), "universities");
    }

    private boolean fieldNameEquals(String markerTypeName, String fieldName) {
        return getField(markerTypeName).getName().equals(fieldName);
    }

    protected boolean fieldTypeIsList(String markerTypeName) {
        return getField(markerTypeName).getType().toString().contains("List");
    }

    private Field getField(String markerTypeName) {
        return MarkerType.markerTypeToStrategy.get(markerTypeName);
    }

}