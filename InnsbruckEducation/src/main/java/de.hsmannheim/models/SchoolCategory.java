package de.hsmannheim.models;

import java.util.HashMap;
import java.util.Map;

public enum SchoolCategory {
    HIGHER_EDUCATION,
    PRIMARY,
    SPECIAL;

    Map<String, String> schholCategoryNameToString = new HashMap<String, String>() {{
        put(HIGHER_EDUCATION.name(), "Höhere Bildungseinrichtung");
        put(PRIMARY.name(), "Grundschule");
        put(SPECIAL.name(), "Sonderschule");
    }};
    @Override
    public String toString() {
        return schholCategoryNameToString.get(this.name());
    }
}
