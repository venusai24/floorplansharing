package com.floorplannersharing;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class CanvasObject extends Rectangle{
    public int layer; // added for render ordering
    // total 3 layers
    abstract void draw(Graphics2D g2d); // get g2d component and display it
    public abstract boolean contains(int x, int y); // check if the selection point is inside the object
    public abstract void move(int deltaX, int deltaY); // move object by give deltas
    public abstract String getType();
    // +90 degree
    public void rotate() {
        int center_x = (int)this.getCenterX();
        int center_y = (int)this.getCenterY();
        int x_new = (this.y - center_y) + center_x;
        int y_new = (this.x - center_x) + center_y;
        this.x = x_new;
        // this.y = Math.min(y_new, y_new-this.width);
        this.y = y_new;
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }
}
