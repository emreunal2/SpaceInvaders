package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Image {
    // The image to be drawn for this Image
    private BufferedImage image;

    // Create a new image
    public Image(BufferedImage image) {
        this.image = image;
    }

    // Get the width of the drawn Image
    public int getWidth() {
        return image.getWidth(null);
    }

    // Get the height of the drawn Image    
    public int getHeight() {
        return image.getHeight(null);
    }

    // Draw the Image onto the graphics context provided.
    public void draw(Graphics g,int x,int y) {
        g.drawImage(image,x,y,null);
    }

}