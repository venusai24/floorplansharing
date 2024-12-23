package com.floorplannersharing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class RotationUtility {
    public static void RotateWithContained(CanvasObjectManager objectManager, CanvasObject currentObject, int angle) {
        // if (currentObject == null) return;

        int pivotX = currentObject.x + currentObject.width / 2;
        int pivotY = currentObject.y + currentObject.height / 2;

        for (CanvasObject object: objectManager.getObjects()) {
            if (object != currentObject && currentObject.contains(object)){
                rotateObject(object, pivotX, pivotY, angle);
            }
        }
        rotateObject(currentObject, pivotX, pivotY, angle);
    }


    public static void rotateObject(CanvasObject object, int pivotX, int pivotY, int angle) {
        double objectCenterX = object.getX() + object.getWidth() / 2;
        double objectCenterY = object.getY() + object.getHeight() / 2;

        // apply rotation to image
        if (object instanceof Furniture) {
            ((Furniture) object).rotateImage(angle);
        }
        
        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(angle), pivotX, pivotY);
        
        Point2D rotatedCenter = transform.transform(new Point2D.Double(objectCenterX, objectCenterY), null);
        
        int newX = (int) (rotatedCenter.getX() - object.height / 2);
        int newY = (int) (rotatedCenter.getY() - object.width / 2);
        
        // object.setLocation(newX, newY);
        object.x = newX;
        object.y = newY;
        
        int temp = object.width;
        object.width = object.height;
        object.height = temp;
        
    }
    
}
