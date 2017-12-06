package de.hsmannheim.util.map.zaehnersprengel;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.map.ITableTraverseStrategy;
import processing.data.Table;
import processing.data.TableRow;

import java.io.Serializable;
import java.util.Map;

public class ZaehnerSpengelMapUtil {


    public static void traverseOverTableAndSetResult(UrbanDistrict district, Table mappingTable, ITableTraverseStrategy strategy) {
        for (TableRow row : mappingTable.rows()) {
            Map result = strategy.setRegionNumberAndName(district, row);
            if(resultIsNotEmpty(result)) {
                district.setRegionNumber((Integer) result.get("BZR"));
                district.setName((String) result.get("Name"));
            }
        }

    }

    private static boolean resultIsNotEmpty(Map result) {
        return !result.isEmpty();
    }
}
