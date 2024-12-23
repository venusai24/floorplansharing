package com.floorplannersharing;

import java.awt.Color;

public class RoomTool extends DrawingTool {

    private int startX;
    private int startY;
    private Color color = Color.green; // set as default

    @Override
    public void startDrawing(int x, int y) {
        currentObject = new Room(x, y, 0, 0, color);
        startX = x;
        startY = y;
    }

    @Override
    public void continueDrawing(int x, int y) {
        if (currentObject != null) {
            currentObject.width = Math.abs(x - startX);
            currentObject.height = Math.abs(y - startY);
            currentObject.x = Math.min(x, startX);
            currentObject.y = Math.min(y, startY);
        }
    }

    @Override
    public void finishDrawing() {
        currentObject = null;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
