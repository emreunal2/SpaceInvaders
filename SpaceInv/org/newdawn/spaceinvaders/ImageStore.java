package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * A resource manager for Images in the game. Its often quite important
 * how and where you get your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 * <p>
 * [singleton]
 * <p>
 * @author Kevin Glass
 */
public class ImageStore {
    /** The single instance of this class */
    private static ImageStore single = new ImageStore();

    /**
     * Get the single instance of this class
     *
     * @return The single instance of this class
     */
    public static ImageStore get() {
        return single;
    }

    /** The cached Image map, from reference to Image instance */
    private HashMap Images = new HashMap();

    /**
     * Retrieve a Image from the store
     *
     * @param ref The reference to the image to use for the Image
     * @return A Image instance containing an accelerate image of the request reference
     */
    public Image getImage(String ref) {
        // if we've already got the Image in the cache
        // then just return the existing version
        if (Images.get(ref) != null) {
            return (Image) Images.get(ref);
        }

        // otherwise, go away and grab the Image from the resource
        // loader
        BufferedImage sourceImage = null;

        try {
            // The ClassLoader.getResource() ensures we get the Image
            // from the appropriate place, this helps with deploying the game
            // with things like webstart. You could equally do a file look
            // up here.
            URL url = this.getClass().getClassLoader().getResource(ref);

            if (url == null) {
                fail("Can't find ref: "+ref);
            }

            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load: "+ref);
        }

        // create an accelerated image of the right size to store our Image in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);

        // draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage,0,0,null);

        // create a Image, add it the cache then return it
        Image Image = new Image(image);
        Images.put(ref,Image);

        return Image;
    }

    /**
     * Utility method to handle resource loading failure
     *
     * @param message The message to display on failure
     */
    private void fail(String message) {
        // we're pretty dramatic here, if a resource isn't available
        // we dump the message and exit the game
        System.err.println(message);
        System.exit(0);
    }
}