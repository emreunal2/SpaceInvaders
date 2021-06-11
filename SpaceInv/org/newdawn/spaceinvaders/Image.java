package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A Image to be displayed on the screen. Note that a Image
 * contains no state information, i.e. its just the image and
 * not the location. This allows us to use a single Image in
 * lots of different places without having to store multiple
 * copies of the image.
 *
 * @author Kevin Glass
 */
public class Image {
    /** The image to be drawn for this Image */
    private BufferedImage image;

    /**
     * Create a new Image based on an image
     *
     * @param image The image that is this Image
     */
    public Image(BufferedImage image) {
        this.image = image;
    }

    /**
     * Get the width of the drawn Image
     *
     * @return The width in pixels of this Image
     */
    public int getWidth() {
        return image.getWidth(null);
    }

    /**
     * Get the height of the drawn Image
     *
     * @return The height in pixels of this Image
     */
    public int getHeight() {
        return image.getHeight(null);
    }

    /**
     * Draw the Image onto the graphics context provided
     *
     * @param g The graphics context on which to draw the Image
     * @param x The x location at which to draw the Image
     * @param y The y location at which to draw the Image
     */
    public void draw(Graphics g,int x,int y) {
        g.drawImage(image,x,y,null);
    }
}