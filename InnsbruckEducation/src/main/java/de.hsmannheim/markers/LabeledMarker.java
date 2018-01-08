package de.hsmannheim.markers;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.hsmannheim.config.FormConfig;
import de.hsmannheim.models.education.AbstractEducationalInstitution;
import de.hsmannheim.util.string.StringUtil;
import processing.core.PFont;
import processing.core.PGraphics;

public class LabeledMarker extends SimplePointMarker {

    protected AbstractEducationalInstitution educationalInstitution;
    private String name;
    protected float size = 15;
    protected int space = 6;
    private boolean showLabel = false;

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    private PFont font;
    private float fontSize = 12;

    /**
     * Basic constructor. Used in MarkerFactory when created on data loading, label will be set afterwards.
     */
    public LabeledMarker(Location location) {
        this(location, null, null, 0);
    }

    public LabeledMarker(AbstractEducationalInstitution educationalInstitution) {
        this(educationalInstitution.getLocation(), educationalInstitution, null, 0);
    }

    public LabeledMarker(Location location, AbstractEducationalInstitution educationalInstitution, PFont font, float size) {
        this.location = location;
        this.educationalInstitution = educationalInstitution;
        this.name = educationalInstitution.getName();
        this.size = size;

        this.font = font;
        if (font != null) {
            this.fontSize = font.getSize();
        }
    }

    private float calculateTextWidth(PGraphics pg, String labelText) {
        String longestString = StringUtil.getLongestStringFromArray(labelText.split("\n"));
        return pg.textWidth(longestString);
    }

    /**
     * Displays this marker's name in a box.
     */
    public void draw(PGraphics pg, float x, float y) {
        pg.pushStyle();
        pg.pushMatrix();
        if (!hidden) {
            pg.translate(0, 0, 1);
        }
        pg.strokeWeight(strokeWeight);
        if (!hidden) {
            pg.fill(highlightColor);
            pg.stroke(highlightStrokeColor);
        } else {
            pg.fill(color);
            pg.stroke(strokeColor);
        }
        if (!hidden) {
            pg.ellipse(x, y, size, size);
        }


        // label
        if (showLabel && !hidden) {
            String labelText = this.name + "\n" + this.educationalInstitution.category.toString() + "\n";
            labelText += this.educationalInstitution.getAddress();
            if (font != null) {
                pg.textFont(font);
            }
            pg.fill(highlightColor);
            pg.strokeWeight(3);
            pg.stroke(highlightStrokeColor);
            int lines = StringUtil.countOccurrencesOfCharInString(labelText, '\n') + 1;
            float boxWidth = (x + strokeWeight / 2 + calculateTextWidth(pg, labelText) + space * 1.5f);
            int xEndPosition = (int) boxWidth;
            if (xEndPosition > FormConfig.MAP_WIDTH) {
                pg.rect(x + strokeWeight / 2 - 150f, y - fontSize + strokeWeight / 2 - space -30f,
                        calculateTextWidth(pg, labelText) + space * 1.5f,
                        (fontSize + space) * lines + 30f);
                pg.fill(255, 255, 255);
                pg.text(labelText, Math.round(x + space * 0.75f + strokeWeight / 2) - 150f,
                        Math.round(y + strokeWeight / 2 - space * 0.75f));
            } else {
                pg.rect(x + strokeWeight / 2, y - fontSize + strokeWeight / 2 - space -30f,
                        calculateTextWidth(pg, labelText) + space * 1.5f,
                        (fontSize + space) * lines + 30f);
                pg.fill(255, 255, 255);
                pg.text(labelText, Math.round(x + space * 0.75f + strokeWeight / 2),
                        Math.round(y + strokeWeight / 2 - space * 0.75f));
            }
        }
        pg.popMatrix();
        pg.popStyle();
    }

    public String getName() {
        return name;
    }
}