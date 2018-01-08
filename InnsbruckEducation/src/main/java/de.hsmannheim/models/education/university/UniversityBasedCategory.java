package de.hsmannheim.models.education.university;

import de.hsmannheim.models.education.ICategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mjando on 12.12.17.
 */
public enum UniversityBasedCategory implements ICategory {

    UNIVERSITY, UAS;

    protected static Map<String, String> universityCategoryNameToString = new HashMap<String, String>() {{
        put(UNIVERSITY.name(), "Universität");
        put(UAS.name(), "Hochschule");
    }};

    protected static Map<String, int[]> schoolCategoryNameToColor = new HashMap<String, int[]>() {{
        put(UNIVERSITY.name(), new int[]{56, 121, 226});
        put(UAS.name(), new int[]{117, 157, 221});
    }};

    @Override
    public Map<String, String> getCategoryNameToString() {
        return universityCategoryNameToString;
    }

    public Map<String, int[]> getSchoolCategoryNameToColor(){return schoolCategoryNameToColor;}

    @Override
    public String toString() {
        return universityCategoryNameToString.get(this.name());
    }
}
