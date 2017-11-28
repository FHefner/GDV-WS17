package markers;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImageMarker extends AbstractMarker{

    PImage img;

    public ImageMarker(Location location, PImage img) {
        super(location);
        this.img = img;
    }

    @Override
    public void draw(PGraphics pGraphics, float x, float y) {
        pGraphics.pushStyle();
        pGraphics.imageMode(PConstants.CORNER);
        pGraphics.image(img, x - 11, y - 37);
        pGraphics.popStyle();
    }

    @Override
    protected boolean isInside(float checkX, float checkY, float x, float y) {
        return checkX > x && checkX < (x + img.width) && checkY > y && checkY < (y+ img.height);
    }
}
