package de.hsmannheim.util.map;

import de.hsmannheim.models.UrbanDistrict;
import processing.data.TableRow;

import java.util.Map;

public interface ITableTraverseStrategy {
    Map setRegionNumberAndName(UrbanDistrict district, TableRow row);
}
