package com.floorplannersharing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class FurnitureLoader {
    private static FurnitureLoader instance;

    // BufferedImage kitchenSink;
    // BufferedImage shower;
    // BufferedImage stove;
    // BufferedImage toilet;
    // BufferedImage washbasin;
    ArrayList<BufferedImage> furnitureImages;
    
    // NOTE : also add BED, couch, dining Table, Almira !!

    private FurnitureLoader() {
        BufferedImage bed = loadImage("res/bed_color.png");
        BufferedImage diningTable = loadImage("res/diningTable_color.png");
        BufferedImage kitchenSink = loadImage("res/kitchenSink_color.png");
        BufferedImage stove = loadImage("res/stove_color.png");
        BufferedImage shower = loadImage("res/shower_color.png");
        BufferedImage toilet = loadImage("res/toilet_color.png");
        BufferedImage washbasin = loadImage("res/washbasin_color.png");
        

        furnitureImages = new ArrayList<>();
        
        furnitureImages.add(bed); // index = 0
        furnitureImages.add(diningTable); // index = 1
        furnitureImages.add(kitchenSink); // index = 2
        furnitureImages.add(stove); // index = 3
        furnitureImages.add(shower); // index = 4
        furnitureImages.add(toilet); // index = 5
        furnitureImages.add(washbasin); // index= 6

    }

    public static FurnitureLoader getInstance() {
        if (instance == null) {
            instance = new FurnitureLoader();
        }
        return instance;
    }

    private BufferedImage loadImage(String fileName) {
        try {
            String absolutePath = new File(fileName).getAbsolutePath();
            File  inputStream = new File(absolutePath);
            return ImageIO.read(inputStream);
            
        } catch (IOException e) {
            System.out.println("File not found: " + fileName);;
        }
        return null;
    }

    public BufferedImage getFurnitureImage(int index) {
        return this.furnitureImages.get(index);
    }

}
