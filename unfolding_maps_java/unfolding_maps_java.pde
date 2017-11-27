import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;  
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.marker.*;
import java.util.*;

UnfoldingMap map;
Table schoolData;

Location innsbruckLocation = new Location(47.264874, 11.395907);
List<Marker> schoolLocations;



void loadSchools() {
  String schulCSV = "data/hoeher_bildende_schulen.csv";
  schoolData = loadTable(schulCSV, "header");
  println(schoolData.getRowCount() + " Zeilen geladen!");
  schoolLocations = new ArrayList<Marker>();
 
  for (TableRow row : schoolData.rows()) {
    SimplePointMarker tmpMarker = new SimplePointMarker(new Location(row.getFloat("Lat"), row.getFloat("Lon")));
    tmpMarker.setDiameter(30);
    tmpMarker.setColor(90);
    tmpMarker.setStrokeColor(90);
    schoolLocations.add(tmpMarker);
  }
}



void setup() {
  size(800, 600);
  map = new UnfoldingMap(this, new OpenStreetMap.OpenStreetMapProvider());
  MapUtils.createDefaultEventDispatcher(this, map);
  loadSchools();
  map.addMarkers(schoolLocations);
}

void draw() {
  map.draw();
  Location location = map.getLocation(mouseX, mouseY);
  map.zoomAndPanTo(8, innsbruckLocation);
  // fill(0);
  text("Breite: " + location.getLat() + " || Länge: " + location.getLon(), mouseX, mouseY);
}