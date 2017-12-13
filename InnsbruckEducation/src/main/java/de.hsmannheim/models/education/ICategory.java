package de.hsmannheim.models.education;

import java.util.Map;

/**
 * Created by mjando on 12.12.17.
 */
public interface ICategory {


    public String name();

    public Map<String, String> getCategoryNameToString();

    public Map<String, int[]> getSchoolCategoryNameToColor();

}
