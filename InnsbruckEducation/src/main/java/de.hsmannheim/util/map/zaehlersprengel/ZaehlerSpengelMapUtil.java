package de.hsmannheim.util.map.zaehlersprengel;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.map.ITableTraverseStrategy;
import processing.data.Table;
import processing.data.TableRow;

import java.util.Map;

public class ZaehlerSpengelMapUtil {


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
