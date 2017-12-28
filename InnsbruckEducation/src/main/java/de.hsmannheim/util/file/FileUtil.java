package de.hsmannheim.util.file;

import processing.data.TableRow;

public class FileUtil {
    public static Integer getYearFromRow(TableRow row) {
        return Integer.valueOf(row.getString("year").trim());
    }
}
