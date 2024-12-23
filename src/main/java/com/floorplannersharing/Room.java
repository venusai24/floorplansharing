package com.floorplannersharing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Room extends CanvasObject{
    private Color color;
    // public String type = "Room";
    public String type;
    
    public Room(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        layer = 0; // newly added
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        type = "Room";
    }

    @Override
    public void draw(Graphics2D g2d) {
        // for transparency
        // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2d.setColor(this.color);
        g2d.fill(this); // to color inside of rectangle
        // remove above part later

        // reset composite to default
        // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        g2d.setColor(Color.BLACK);
        g2d.draw(this);
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

    public Color getColor() {
        // return (new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue()));
        return this.color;
    }

    @Override
    public String getType() {
        return this.type;
    }

    // extra function, may delete later
    // if deleted: make alpha public, make changes in CanvasOjbectTypeAdapter
    
}
