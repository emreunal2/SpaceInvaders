package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImageStore {
    // The single instance of this class 
    private static ImageStore single = new ImageStore();

    // Get the single instance of this class

    public static ImageStore get() {
        return single;
    }

    // The cached Image map from reference to Image instance
    private HashMap Images = new HashMap();
   
    // Retrieve a Image from the store.
    public Image getImage(String ref) {
        // If you have already got the Image in the cache
        // then just return the existing version
        if (Images.get(ref) != null) {
            return (Image) Images.get(ref);
        }

        // Otherwise go away and grab the Image from the resource
        // loader
        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(ref);

            if (url == null) {
                fail("Can't find ref: "+ref);
            }

            // Use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load: "+ref);
        }

        // Create an accelerated image of the right size to store our Image in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);

        // Draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage,0,0,null);

        // Create a Image add it the cache then return it
        Image Image = new Image(image);
        Images.put(ref,Image);

        return Image;
    }

    // Utility method to handle resource loading failure.
    

    private void fail(String message) {
        // Code dump the message and exit the game
        System.err.println(message);
        System.exit(0);
    }
}