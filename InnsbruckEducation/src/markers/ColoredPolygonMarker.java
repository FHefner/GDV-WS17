package markers;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PGraphics;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PConstants.CLOSE;


public class ColoredPolygonMarker extends SimplePolygonMarker {

    private PApplet applet;
    private List<Integer> colors;

    public ColoredPolygonMarker(List<Location> list, PApplet applet) {
        super(list);
        this.applet = applet;
        this.colors = new ArrayList<>();
        initColors();
    }

    private void initColors() {
        colors.clear();
        Random random = new Random();
        for (int i = 0; i < locations.size(); i++) {
            colors.add(applet.color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        }
    }

    public void draw(PGraphics pg, List<MapPosition> mapPositions) {
        pg.pushStyle();

        pg.strokeWeight(2);
        pg.stroke(0, 200);
        pg.beginShape();
        int i = 0;
        for (MapPosition mapPosition : mapPositions) {
            pg.fill(colors.get(i), 100);
            pg.vertex(mapPosition.x, mapPosition.y);
            i++;
        }
        pg.endShape(CLOSE);

        pg.popStyle();
    }

}
