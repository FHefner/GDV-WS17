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
            Map<String, Serializable> result = strategy.setRegionNumberAndName(district, row);
            district.setRegionNumber((Integer) result.get("BZR"));
            district.setName((String) result.get("Name"));
        }

    }
}
