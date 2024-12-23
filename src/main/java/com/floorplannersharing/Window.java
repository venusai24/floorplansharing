package com.floorplannersharing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class Window extends CanvasObject{
    // public String type = "Window";
    public String type;

    public Window(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        layer = 2;
        type = "Window";
    }

    @Override
    void draw(Graphics2D g2d) {
        float[] dashPattern = {10, 10}; // 10 pixels on, 10 pixels off
        BasicStroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        
        Stroke before = g2d.getStroke();

        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.white);
        
        // g2d.fill(dashedStroke.createStrokedShape(this));
        g2d.draw(this);

        g2d.setStroke(before);
    }

    @Override
    public boolean contains(int x, int y) {
        Rectangle tempRect = new Rectangle(this.x, this.y, this.width, this.height);
        return tempRect.contains(x,y);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    @Override
    public String getType() {
        return this.type;
    }
    
}
