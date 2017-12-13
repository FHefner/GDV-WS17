package de.hsmannheim.models.education.university;

import de.hsmannheim.models.education.ICategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mjando on 12.12.17.
 */
public enum UniversityBasedCategory implements ICategory {

    DEFAULT;

    protected static Map<String, String> universityCategoryNameToString = new HashMap<String, String>() {{
        put(DEFAULT.name(), "Universität");
    }};

    protected static Map<String, int[]> schoolCategoryNameToColor = new HashMap<String, int[]>() {{
        put(DEFAULT.name(), new int[]{161, 0, 255});
    }};

    @Override
    public Map<String, String> getCategoryNameToString() {
        return universityCategoryNameToString;
    }

    public Map<String, int[]> getSchoolCategoryNameToColor(){return schoolCategoryNameToColor;}
}
