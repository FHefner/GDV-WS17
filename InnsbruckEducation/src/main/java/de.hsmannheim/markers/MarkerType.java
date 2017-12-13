package de.hsmannheim.markers;

import de.hsmannheim.InnsbruckEducationApp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum MarkerType {
    SCHOOL_MARKER,
    UNIVERSITY_MARKER,
    DISTRICT_MARKER;


    public final static Map<String, Field> markerTypeToStrategy = new HashMap<String, Field>() {{
        try {
            put(SCHOOL_MARKER.name(), InnsbruckEducationApp.class.getDeclaredField("schools"));
            put(UNIVERSITY_MARKER.name(), InnsbruckEducationApp.class.getDeclaredField("universities"));
            put(DISTRICT_MARKER.name(), InnsbruckEducationApp.class.getDeclaredField("districts"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }};
}
