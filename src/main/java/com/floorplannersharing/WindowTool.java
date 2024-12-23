package com.floorplannersharing;

public class WindowTool extends DrawingTool {

    private int startX;
    private int startY;

    @Override
    public void startDrawing(int x, int y) {
        currentObject = new Window(x, y, 0, 0);
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
        if (!borderCheck() && currentObject != null) 
            CanvasObjectManager.getInstance().removeObject(currentObject);
        currentObject = null;
    }

    private boolean borderCheck() {
        int count = 0;
        for (CanvasObject object : CanvasObjectManager.getInstance().getObjects()) {
            if (currentObject.intersects(object) && currentObject != object) {
                if (object instanceof Room) {
                    count += 1;
                } else if (object instanceof  Door || object instanceof Window) {
                    return false;
                }
            }
        }
        return count == 1;
    }
    
}
