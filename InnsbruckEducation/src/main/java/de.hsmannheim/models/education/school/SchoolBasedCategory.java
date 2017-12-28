package de.hsmannheim.models.education.school;

import de.hsmannheim.models.education.ICategory;

import java.util.HashMap;
import java.util.Map;

public enum SchoolBasedCategory implements ICategory {
    HIGHER_EDUCATION,
    PRIMARY,
    SPECIAL;

    protected static Map<String, String> schoolCategoryNameToString = new HashMap<String, String>() {{
        put(HIGHER_EDUCATION.name(), "Höhere Bildungseinrichtung");
        put(PRIMARY.name(), "Grundschule");
        put(SPECIAL.name(), "Sonderschule");
    }};

    protected static Map<String, int[]> schoolCategoryNameToColor = new HashMap<String, int[]>() {{
        put(HIGHER_EDUCATION.name(), new int[]{115, 134, 52});
        put(PRIMARY.name(), new int[]{156,194,34});
        put(SPECIAL.name(), new int[]{192,247,12});
    }};

    public Map<String, String> getCategoryNameToString() {
        return schoolCategoryNameToString;
    }

    public Map<String, int[]> getSchoolCategoryNameToColor(){ return schoolCategoryNameToColor;}

    @Override
    public String toString() {
        return schoolCategoryNameToString.get(this.name());
    }


}
