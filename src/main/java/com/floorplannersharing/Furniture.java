package com.floorplannersharing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

// This will be changed later
// Furniture will render PNGS

public class Furniture extends CanvasObject{
    // public String type = "Furniture";
    public String type;
    int imageIndex;
    private double rotationAngle = 0.0;
    
    public Furniture(int x, int y, int width, int height, int imageIndex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imageIndex = imageIndex;
        layer = 1;
        type = "Furniture";
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Graphics g = (Graphics) g2d;

        BufferedImage image = FurnitureLoader.getInstance().getFurnitureImage(imageIndex);
        if (image == null) {
            System.out.println("Image not loaded");
            return;
        }
        g2d.drawImage(getRotatedImage(image), this.x, this.y, this.width, this.height, null);

    }

    private BufferedImage getRotatedImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        AffineTransform transform = new AffineTransform();
        transform.rotate(rotationAngle, w/2.0, h/2.0);
        g2d.setTransform(transform);

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    @Override
    public boolean contains(int x, int y) {
        Rectangle tempRect = new Rectangle(this.x, this.y, this.width, this.height); // if temp not created then this.contains() calls itself recursively
        return tempRect.contains(x, y);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    @Override
    public String getType() {
        return this.type;
    }

    // temp function, if deleted make make imageIndex public, changes in CanvasObjectTypeAdapter
    public int getImageIndex() {
        return this.imageIndex;
    }

    public void rotateImage(double angle) {
        rotationAngle += Math.toRadians(angle);
        // use this before rectangle rotation
    }    

}
