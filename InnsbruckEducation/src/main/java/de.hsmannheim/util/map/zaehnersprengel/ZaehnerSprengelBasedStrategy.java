package de.hsmannheim.util.map.zaehnersprengel;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.map.ITableTraverseStrategy;
import processing.data.TableRow;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZaehnerSprengelBasedStrategy implements ITableTraverseStrategy {

    final static String ZSPRL_TEXT = "ZSPRL";

    @Override
    public Map<String, Serializable> setRegionNumberAndName(UrbanDistrict district, TableRow row) {
        String zsprlTextFromRow = row.getString(ZSPRL_TEXT);
        if (zsprlIsGivenAsRange(zsprlTextFromRow))
            return setZaehnerSprengelBasedOnRange(district, row, zsprlTextFromRow);
         else
           return  setZaehnerSprengelBasedOnBezirk(district, row , zsprlTextFromRow);
    }

    protected Map<String, Serializable> setZaehnerSprengelBasedOnBezirk(UrbanDistrict district, TableRow row, String zsprlTextFromRow) {
        Map<String, Serializable> result = new LinkedHashMap<>();
        if (Integer.valueOf(zsprlTextFromRow) == district.getZaehlerSprengel()) {
            putBzrAndNameOnMap(row);
        }
        return result;
    }

    protected Map<String, Serializable> setZaehnerSprengelBasedOnRange(UrbanDistrict district, TableRow row, String zsprlTextFromRow) {
        int startValue = Integer.valueOf(zsprlTextFromRow.split("-")[0]);
        int endValue = Integer.valueOf(zsprlTextFromRow.split("-")[1]);
        return traverseOverZsprlRange(district, row, startValue, endValue);
    }

    private Map<String, Serializable> traverseOverZsprlRange(UrbanDistrict district, TableRow row, int startValue, int endValue) {
        Map<String, java.io.Serializable> result = new LinkedHashMap<>();
        for (int i = startValue; i < (endValue + 1); i++) {
            if (i == district.getZaehlerSprengel()) {
                result = putBzrAndNameOnMap(row);
            }
        }
        return result;
    }

    private Map<String, Serializable> putBzrAndNameOnMap(TableRow row) {
        Map<String, Serializable> result = new LinkedHashMap<>();
        result.put("BZR",row.getInt("BZR"));
        result.put("Name", row.getString("Name"));
        return result;
    }

    protected boolean zsprlIsGivenAsRange(String zsprlText) {
        return zsprlText.contains("-");
    }
}
