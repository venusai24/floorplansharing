package com.floorplannersharing;


public abstract class DrawingTool {
    protected CanvasObject currentObject;

    public abstract void startDrawing(int x, int y);

    public abstract void continueDrawing(int x, int y);

    public abstract void finishDrawing();

    // return the current shape / line
    public CanvasObject getCurrentObject() {
        return currentObject;
    }
    
}
