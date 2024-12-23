package com.floorplannersharing;

import java.util.ArrayList;
import javax.swing.JLabel;

// singleton class
// because there should be a single object manager that contains all the canvas objects
public class CanvasObjectManager {
    private ArrayList<CanvasObject> objects;
    private static CanvasObjectManager instance;
    private JLabel statusLabel;
    // keep track of index of first object of each layer
    // Room: 0, Furniture: 1, Door & Window: 3
    private int[] layerIndex = {-1,-1,-1};

    private CanvasObjectManager() {
        objects = new ArrayList<>();
    }

    public void getLabel(JLabel statusLabel){
        this.statusLabel = statusLabel;
    }

    public static CanvasObjectManager getInstance() {
        if (instance == null) {
            instance = new CanvasObjectManager();
        }
        return instance;
    }

    public void addObject(CanvasObject newObject) {
        if (newObject.width == 0 || newObject.height == 0) {
            return;
        }
        if (!(newObject instanceof Door || newObject instanceof Window)) {
            for (CanvasObject object : objects) {
                if (object.intersects(newObject)
                && !(object instanceof Room && newObject instanceof Furniture && object.contains(newObject))
                && !(newObject instanceof Room && object instanceof Furniture && newObject.contains(object))
                && !(newObject instanceof Furniture  && (object instanceof Window ||  object instanceof Door ))
                &&  !(object instanceof Door && newObject.intersects(object) && newObject instanceof Room )
                &&  !(object instanceof Window && newObject.intersects(object) && newObject instanceof Room )
                //&& !(newObject instanceof Furniture && object instanceof Door)
                ) {
                    statusLabel.setText("Intersection ERROR!");
                    //debug code
                    // if ((newObject instanceof Furniture && object instanceof Door && object.contains(newObject))){
                    //     System.out.println("Door intersects");
                    // }
                    return;
                }
                else statusLabel.setText("Status: Ready");
            }

        }

        // switch case can be used here but should it be? 
        if (newObject.layer == 0) {
            // layer0 object at first index
            objects.add(0,newObject);
        } 
        else if (newObject.layer == 2) {
            // layer 2 object at last index
            objects.add(newObject);
            } 
            else {
                if (layerIndex[2] != -1)
                    // if layer2 object present then insert at the position of first of its object
                    objects.add(layerIndex[2], newObject);
                else
                    // layer2 object absent, add layer1 object at end
                    objects.add(newObject);
        }

        for (int i = objects.size() - 1; i >= 0; i--) {
            if(objects.get(i).layer == 0) layerIndex[0] = i;
            if(objects.get(i).layer == 1) layerIndex[1] = i;
            if(objects.get(i).layer == 2) layerIndex[2] = i;
            
        }
        // objects.add(newObject);
    }
    
    public void removeObject(CanvasObject object) {
        objects.remove(object);
        statusLabel.setText("OBJECT DELETED");
        for (int i = objects.size() - 1; i >= 0; i--) {
            if(objects.get(i).layer == 0) layerIndex[0] = i;
            if(objects.get(i).layer == 1) layerIndex[1] = i;
            if(objects.get(i).layer == 2) layerIndex[2] = i;
        }
    }

    public CanvasObject getObjectAt(int x, int y) {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i).contains(x,y)) {
                return objects.get(i);
            }
        }
        
        return null;
    }

    public ArrayList<CanvasObject> getObjects() {
        return objects;
    }

    public void loadObjects(ArrayList<CanvasObject> importedObjects) {
        this.objects = importedObjects;
    }
}
