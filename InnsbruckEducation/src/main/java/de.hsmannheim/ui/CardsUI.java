package de.hsmannheim.ui;

import processing.core.PApplet;

public class CardsUI {

    private PApplet applet;

    //Colors
    private Integer c_very_dark;
    private Integer c_dark;
    private Integer c_mid;
    private Integer c_light;

    private Integer c_primary;
    private Integer c_hover;


    private Integer c_text_color;
    private Integer top_right;


    //Click Options
    private boolean clicked = false;
    private boolean canClick = true;

    //For text Input/Edit
    private String bufferText = null;
    private boolean doneText = false;

    //Default sizes
    private int s_big = 200;
    private int s_height = 30;
    private int s_med = 100;
    private int s_small = 50;

    public PApplet getApplet() {
        return applet;
    }

    //For Cards
    int card_h = 0;
    int card_w = 0;
    private int card_x = 0;
    private int card_y = 0;

    public CardsUI(PApplet applet) {
        super();
        this.applet = applet;
        this.uiDark();
    }

    public void uiDark() {
        c_very_dark = this.applet.color(36, 37, 46);
        c_dark = this.applet.color(29, 33, 44);
        c_mid = this.applet.color(44, 58, 71);
        c_light = this.applet.color(51, 64, 80);
        c_hover = this.applet.color(32, 155, 160);
        c_text_color = this.applet.color(255);
        c_primary= this.applet.color(33, 115, 139);
        top_right = this.applet.color(255);
    }

    public void uiLight() {
        c_very_dark = this.applet.color(100);
        c_dark = this.applet.color(150);
        c_mid = this.applet.color(200);
        c_light = this.applet.color(250);
        c_hover = this.applet.color(32, 155, 160);
        c_text_color = this.applet.color(10);
        c_primary= this.applet.color(33, 115, 139);
        top_right = this.applet.color(255);
    }

    //Basic Text Button
    public boolean Button(String text, int x, int y, int w, int h) {

        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w &&
                this.applet.mouseY >= y && this.applet.mouseY <= y+h) {
            this.applet.fill(c_hover);
            this.applet.rect(x, y, w, h);
            this.applet.fill(c_text_color);
            this.applet.textSize(15);
            this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
            this.applet.text(text, x, y, w, h);
            if (clicked && canClick) {
                this.applet.fill(c_light);
                this.applet.rect(x, y, w, h);
                this.applet.text(text, x, y, w, h);
                canClick = false;
                return true;
            }
        } else {
            this.applet.fill(c_light);
            this.applet.rect(x, y, w, h);
            this.applet.fill(c_text_color);
            this.applet.textSize(15);
            this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
            this.applet.text(text, x, y, w, h);
            return false;
        }

        return false;
    }

    //Basic Text Button
    boolean Button(String text, int x, int y) {
        return Button(text, x, y, s_med, s_height);
    }

    //Basic Text Button
    boolean Button(String text, int x, int y, String t) {
        return Button(text, x, y, s_med, s_height, t);
    }

    //X and Y are the position of the point of the triangle
    public void Tooltip(String text, int x, int y) {
        this.applet.textSize(25);
        int w = (int)applet.textWidth(text);
        int h = 50;
        int tw = 14; //triangle width
        int th = 15; //triangle height
        applet.noStroke();
        //Shadow
        this.applet.fill(0, 0, 0, 15);
        this.applet.rect(5+x-w/2, 5+y-th-h, w, h, 2);
        this.applet.triangle(5+x-tw/2, 5+y-th, 5+x, 5+y, 5+x+tw/2, 5+y-th);
        //Color
        this.applet.fill(c_very_dark);
        this.applet.rect(x-w/2, y-th-h, w, h, 2);
        this.applet.triangle(x-tw/2, y-th, x, y, x+tw/2, y-th);
        //Text
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
        this.applet.text(text, x-w/2, y-th-h, w, h);
        //this.applet.triangle(
    }

    //Button With Tooltip
    boolean Button(String text, int x, int y, int w, int h, String tooltip) {
        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w &&
                this.applet.mouseY >= y && this.applet.mouseY <= y+h) {
            Tooltip(tooltip, x+w/2, y-1);
            this.applet.fill(c_hover);
            this.applet.rect(x, y, w, h);
            this.applet.fill(c_text_color);
            this.applet.textSize(15);
            this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
            this.applet.text(text, x, y, w, h);
            if (clicked && canClick) {
                this.applet.fill(c_light);
                this.applet.rect(x, y, w, h);
                this.applet.text(text, x, y, w, h);
                canClick = false;
                return true;
            }
        } else {
            this.applet.fill(c_light);
            this.applet.rect(x, y, w, h);
            this.applet.fill(c_text_color);
            this.applet.textSize(15);
            this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
            this.applet.text(text, x, y, w, h);
            return false;
        }

        return false;
    }


    public void mousePressed() {
        clicked = true;
    }

    public void mouseReleased() {
        clicked = false;
        canClick  = true;
    }

    private void Edittext(String txt) {
        bufferText = txt;
    }


    //c_mid
    public void beginCard(String card_title, int x, int y, int w, int h) {

        this.applet.noStroke();
        //Shadow
        this.applet.fill(0, 0, 0, 15);
        this.applet.rect(x+5, y+5, w, h);
        this.applet.fill(c_light);
        this.applet.rect(x, y, w, 40, 2, 2, 0, 0);
        this.applet.textSize(15);
        this.applet.textAlign(this.applet.CENTER, this.applet.CENTER);
        this.applet.fill(c_text_color);
        this.applet.text(card_title, x, y, w, 40);
        this.applet.fill(c_mid);

        this.applet.rect(x, y+40, w, h-40, 0, 0, 2, 2);

        card_h = h-40;
        card_w = w;
        card_x = x;
        card_y = y+40;
        //uiLight();
    }

    public void beginCard(int x, int y, int w, int h) {
        this.applet.noStroke();
        this.applet.fill(c_mid);

        this.applet.rect(x, y, w, h);

        card_h = h;
        card_w = w;
        card_x = x;
        card_y = y;
        //uiDark();
    }

    public void endCard() {
        card_h = 0;
        card_w = 0;
        card_y = 0;
        card_x = 0;
    }

    //Toggle
    public boolean Toggle(boolean value, int x, int y, int w, int h) {
        this.applet.fill(c_dark);
        this.applet.stroke(c_light);
        this.applet.rect(x, y, w, h, h/2);
        int pos = 0;
        if (value)
            pos = w-h;
        //Hover
        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w
                && this.applet.mouseY >= y && this.applet.mouseY <= y+h && this.applet.mousePressed)
        {

            this.applet.noStroke();

            this.applet.fill(this.applet.red(c_hover), this.applet.green(c_hover), this.applet.blue(c_hover), 100);
            this.applet.ellipse(x+h/2+pos, y+h/2, h-2, h-2);
            this.applet.fill(c_hover);
            this.applet.ellipse(x+h/2+pos, y+h/2, h-8, h-8);
            this.applet.noStroke();
            if (clicked && canClick) {
                value = !value;
                canClick = false;
                return value;
            }
        }
        //Normal
        else {
            if (value)
                this.applet.fill(c_hover);
            else
                this.applet.fill(c_light);
            this.applet.stroke(c_light);
            this.applet.ellipse(x+h/2+pos, y+h/2, h-8, h-8);
        }


        return value;
    }

    public boolean Toggle(boolean value, int x, int y) {
        return Toggle(value, x, y, 60, 30);
    }

    public boolean Toggle(String label, boolean value, int x, int y) {
        this.applet.textSize(15);
        float tw = this.applet.textWidth(label);
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.LEFT, this.applet.CENTER);
        this.applet.text(label, x, y, tw, 30);
        return Toggle(value, x + (int) tw + 20, y);
    }

    //Toggle
    public boolean RadioButton(boolean value, int x, int y, int w, int h) {
        this.applet.fill(c_dark);
        this.applet.stroke(c_light);
        this.applet.rect(x, y, w, h, h/2);
        int pos = 0;
        if (value)
            pos = w-h;
        //Hover
        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w && this.applet.mouseY >= y && this.applet.mouseY <= y+h)
        {
            this.applet.fill(c_light);
            this.applet.stroke(c_hover);
            this.applet.ellipse(1+x+h/2+pos, y+h/2, h-2, h-2);
            this.applet.noStroke();
            if (clicked && canClick) {
                value = !value;
                canClick = false;
                return value;
            }
        }
        //Normal
        else {


            this.applet.fill(c_light);
            this.applet.stroke(c_light);
            this.applet.ellipse(x+h/2+pos, y+h/2, h-8, h-8);
        }


        return value;
    }

    public boolean Toggle(String text, boolean value, int x, int y, int w, int h) {
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.LEFT, this.applet.CENTER);
        this.applet.text(text, x, y, w, h);
        int pos_x = (int)this.applet.textWidth(text);
        return Toggle(value, x+10+pos_x, y, 60, 30);
    }

    //Basic Slider from 0f to 1f
    public float Slider(float min, float max, float value, int x, int y, int w, int h) {
        this.applet.noStroke();
        this.applet.fill(c_light);
        this.applet.rect(x, y+h/2, w, 4, 2);
        float pos = this.applet.map(value, min, max, 0, w);
        this.applet.fill(c_hover);
        this.applet.rect(x, y+h/2, pos, 4, 2);

        //Hover
        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w && this.applet.mouseY >= y && this.applet.mouseY <= y+h)
        {
            this.applet.fill(c_hover);
            if (this.applet.mousePressed) {
                pos = this.applet.mouseX;
                value = this.applet.map(pos, x, x+w, min, max);
                this.applet.fill(this.applet.red(c_hover), this.applet.green(c_hover), this.applet.blue(c_hover), 100);
                this.applet.ellipse(pos, y+h/2, h, h);
                this.applet.fill(c_hover);
                this.applet.ellipse(pos, y+h/2, h-8, h-8);
            } else {
                this.applet.fill(this.applet.red(c_hover), this.applet.green(c_hover), this.applet.blue(c_hover), 50);
                this.applet.ellipse(pos+x, y+h/2, h, h);
                this.applet.fill(c_hover);
                this.applet.ellipse(pos+x, y+h/2, h-8, h-8);
            }
        }
        //Normal
        else {
            this.applet.noStroke();
            this.applet.fill(c_hover);
            this.applet.ellipse(pos+x, y+h/2, h-8, h-8);
        }

        return value;
    }

    //Basic Slider with Tooltip
    public float Slider(float min, float max, float value, int x, int y, int w, int h, char tooltip) {
        this.applet.noStroke();
        this.applet.fill(c_light);
        this.applet.rect(x, y+h/2, w, 4, 2);
        float pos = this.applet.map(value, min, max, 0, w);
        this.applet.fill(c_hover);
        this.applet.rect(x, y+h/2, pos, 4, 2);

        //Hover
        if (this.applet.mouseX >= x && this.applet.mouseX <= x+w && this.applet.mouseY >= y && this.applet.mouseY <= y+h)
        {

            this.applet.fill(c_hover);
            if (this.applet.mousePressed) {
                this.applet.stroke(c_hover);
                pos = this.applet.mouseX;
                value = this.applet.map(pos, x, x+w, min, max);
                this.applet.fill(this.applet.red(c_hover), this.applet.green(c_hover), this.applet.blue(c_hover), 100);
                this.applet.ellipse(pos, y+h/2, h, h);
                this.applet.fill(c_hover);
                this.applet.ellipse(pos, y+h/2, h-8, h-8);

                //Tooltip
                if (tooltip=='%') {
                    String s = (int)(value*100)+"%";
                    Tooltip(s, (int)(pos), y);
                } else if (tooltip=='#') {
                    String s = this.applet.str((int)value);
                    Tooltip(s, (int)(pos), y);
                }
            } else {
                this.applet.fill(this.applet.red(c_hover), this.applet.green(c_hover), this.applet.blue(c_hover), 50);
                this.applet.ellipse(pos+x, y+h/2, h, h);
                this.applet.fill(c_hover);
                this.applet.ellipse(pos+x, y+h/2, h-8, h-8);
            }
        }
        //Normal
        else {
            this.applet.noStroke();
            this.applet.fill(c_hover);
            this.applet.ellipse(pos+x, y+h/2, h-8, h-8);
        }

        return value;
    }

    public void Label(String label, int x, int y) {
        this.applet.textSize(15);
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.LEFT, this.applet.CENTER);
        this.applet.text(label, x, y);
    }

    public int Slider(String label, float min, float max, float value, int x, int y, int w, int h) {
        int w2 = 0;
        this.applet.textSize(15);
        float tw = this.applet.textWidth(label);
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.LEFT, this.applet.CENTER);
        this.applet.text(label, x, y, tw, h);
        w2 = (int)(w-tw-15);
        return (int) Slider(min, max, value, (int)(tw+x+15), y, w2, h);
    }

    public float Slider(String label, float min, float max, float value, int x, int y, int w, int h, char tooltip) {
        int w2 = 0;
        this.applet.textSize(15);
        float tw = this.applet.textWidth(label);
        this.applet.textSize(15);
        this.applet.fill(255);
        this.applet.textAlign(this.applet.LEFT, this.applet.CENTER);
        this.applet.text(label, x, y, tw, h);
        w2 = (int)(w-tw-15);
        return Slider(min, max, value, (int)(tw+x+15), y, w2, h, tooltip);
    }
}
