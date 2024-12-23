package com.floorplannersharing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileManager {
    CanvasObjectManager objectManager;
    SketchApp sketchApp; // this is required to display File Chooser over the whole app
    SketchPanel sketchPanel; // required to repaint canvas after importing

    public FileManager(SketchApp sketchApp, SketchPanel sketchPanel) {
        objectManager = CanvasObjectManager.getInstance();
        this.sketchApp = sketchApp;
        this.sketchPanel = sketchPanel;
        
    }
    
    public void saveToFile() {
        Gson json = new GsonBuilder()
        .registerTypeAdapterFactory(CanvasObjectAdapterFactory.getAdapterFactory())
        .registerTypeAdapter(Color.class, new ColorTypeAdapter())
        .setPrettyPrinting()
        .create();
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save canvas as JSON");
        int userSelection = fileChooser.showSaveDialog(sketchApp);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                System.out.println(objectManager.getObjects()); // debug code
                json.toJson(objectManager.getObjects(), writer);
                JOptionPane.showMessageDialog(sketchApp, "Canvas data exported to " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFromFile() {
        Gson json = new GsonBuilder()
        .registerTypeAdapter(CanvasObject.class, new CanvasObjectTypeAdapter())
        .registerTypeAdapter(Color.class, new ColorTypeAdapter())
        .create();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import canvas from JSON");
        int userSelection = fileChooser.showOpenDialog(sketchApp); // need to change this?

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileReader reader = new FileReader(file)) {
                ArrayList<CanvasObject> importedObjects = json.fromJson(reader, new TypeToken<ArrayList<CanvasObject>>() {}.getType());
                // System.out.println(importedObjects.size()); // debug
                System.out.println(importedObjects); // debug
                objectManager.loadObjects(importedObjects);
                sketchPanel.repaint();
                JOptionPane.showMessageDialog(sketchApp, "Canvas data imported from " + file.getName() + " successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
