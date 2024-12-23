package com.floorplannersharing;

public class FurnitureTool extends DrawingTool {

    private int startX;
    private int startY;
    private int imageIndex = 0;
    // default image set to be Kitchen Sink

    @Override
    public void startDrawing(int x, int y) {
        currentObject = new Furniture(x, y, 0, 0, imageIndex);
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

    public void setImageIndex(int index) {
        this.imageIndex = index;
    }
    
}
