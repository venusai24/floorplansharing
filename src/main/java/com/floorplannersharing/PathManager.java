package com.floorplannersharing;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class PathManager {
    private ArrayList<Path2D> paths;
    
    public PathManager() {
        paths = new ArrayList<>();
    }

    public void addPath(Path2D path) {
        paths.add(path);
    }

    public void undoLastPath() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() -1);
        }
    }

    public void clearAllPaths() {
        paths.clear();
    }

    public ArrayList<Path2D> getPaths() {
        return paths;
    }
}
