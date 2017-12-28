package de.hsmannheim.markers;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.List;
import java.util.Random;

import static processing.core.PConstants.CLOSE;


public class ColoredPolygonMarker extends SimplePolygonMarker {

    private PApplet applet;
    private Integer polygonColor;
    private Integer initialColor;

    public Integer getInitialColor() {
        return initialColor;
    }

    public Integer getPolygonColor() {
        return polygonColor;
    }

    public void setPolygonColor(Integer color) {
        this.polygonColor = color;
    }

    public void resetColor() {
        this.polygonColor = this.initialColor;
    }

    public ColoredPolygonMarker(PApplet applet, List<Location> list, Integer color) {
        super(list);
        this.applet = applet;
        this.polygonColor = color;
        this.initialColor = color;
    }

    public ColoredPolygonMarker(PApplet applet, List<Location> list) {
        super(list);
        this.applet = applet;
        generateRandomColor();
        this.initialColor = this.polygonColor;
    }

    private void generateRandomColor() {
        Random random = new Random();
        polygonColor = applet.color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public void draw(PGraphics pg, List<MapPosition> mapPositions) {
        pg.pushStyle();

        pg.strokeWeight(2);
        pg.stroke(0, 200);
        pg.beginShape();
        for (MapPosition mapPosition : mapPositions) {
            pg.fill(polygonColor, 100);
            pg.vertex(mapPosition.x, mapPosition.y);
        }
        pg.endShape(CLOSE);

        pg.popStyle();
    }

}
