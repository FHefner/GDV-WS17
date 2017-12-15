package de.hsmannheim.models;

import de.hsmannheim.models.education.school.SchoolBasedCategory;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by mjando on 12.12.17.
 */
public class SchoolBasedCategorySpec {

    public static int[] HIGHER_EDUCATION_COLOR = new int[]{115, 134, 52};
    public static int[] PRIMARY_EDUCATION_COLOR = new int[]{156, 194, 34};
    public static int[] SPECIAL_EDUCATION_COLOR = new int[]{192, 247, 12};


    @Test
    public void testSchoolBasedCategoryList() {
        assert SchoolBasedCategory.values().length == 3;
    }

    @Test
    public void getCategoryNameToString() throws Exception {
        assert SchoolBasedCategory.HIGHER_EDUCATION.name().equals("Höhere Bildungseinrichtung") &&
                SchoolBasedCategory.PRIMARY.name().equals("Grundschule") &&
                SchoolBasedCategory.SPECIAL.name().equals("Sonderschule");
    }

    @Test
    public void testColorToSchoolBasedCategory() {
        assert checkColorByGivenEnum(SchoolBasedCategory.HIGHER_EDUCATION, HIGHER_EDUCATION_COLOR);
        assert checkColorByGivenEnum(SchoolBasedCategory.PRIMARY, PRIMARY_EDUCATION_COLOR);
        assert checkColorByGivenEnum(SchoolBasedCategory.SPECIAL, SPECIAL_EDUCATION_COLOR);
    }

    protected boolean checkColorByGivenEnum(SchoolBasedCategory educationEnum, int[] educationColor) {
        return Arrays.toString(educationEnum.getSchoolCategoryNameToColor().get(
                educationEnum.name())).equals(Arrays.toString(educationColor));
    }

}