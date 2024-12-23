package com.floorplannersharing;

public class SelectTool extends DrawingTool{
    private CanvasObject selectedObject;
    private int lastX, lastY;

    @Override
    public void startDrawing(int x, int y) {
        selectedObject = CanvasObjectManager.getInstance().getObjectAt(x,y);
        lastX = x;
        lastY = y;
    }

    // change names of these functions / make new class regarding this
    // if SketchPanel is free from dependency of SelectTool on DrawingTool
    // then definitely make new class with well defined function names
    @Override
    public void continueDrawing(int x, int y) {
        if (selectedObject != null) {
            int deltaX = x - lastX;
            int deltaY = y - lastY;
            selectedObject.move(deltaX, deltaY);
            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void finishDrawing() {
        selectedObject = null;
    }

    @Override
    public CanvasObject getCurrentObject() {
        return selectedObject;
    }
    
    public void deleteSelectedObject() {
        if (selectedObject != null) {
            CanvasObjectManager.getInstance().removeObject(selectedObject);
            selectedObject = null;
        }
    }
    
}
