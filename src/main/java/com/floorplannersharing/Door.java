package com.floorplannersharing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Door extends CanvasObject {
    // public String type = "Door";
    public String type;
    
    public Door(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        layer = 2;
        type = "Door";
    }

    @Override
    void draw(Graphics2D g2d) {
        g2d.setColor(Color.gray);
        g2d.draw(this);
    }

    @Override
    public boolean contains(int x, int y) {
        // this is temporary, may not even be required in the future
        // delte if not being used
        Rectangle tempRect = new Rectangle(this.x, this.y, this.width, this.height);
        return tempRect.contains(x,y);
        // if there is new logic that comes below
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
