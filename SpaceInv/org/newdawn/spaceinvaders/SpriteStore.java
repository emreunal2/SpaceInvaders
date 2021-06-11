package org.newdawn.spaceinvaders;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteStore {
	// The single instance of this class
	private static SpriteStore single = new SpriteStore();
	
	// Get the single instance of this class 

	public static SpriteStore get() {
		return single;
	}
	
	// The cached sprite map, from reference to sprite instance
	private HashMap sprites = new HashMap();
	
	// Retrieve a sprite from the store

	public Sprite getSprite(String ref) {
		// if we've already got the sprite in the cache
		// then just return the existing version
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		}
		
		// Otherwise go away and grab the sprite from the resource
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
		
		// Create an accelerated image of the right size to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// Draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage,0,0,null);
		
		// Create a sprite add it the cache then return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref,sprite);
		
		return sprite;
	}
	
	// Utility method to handle resource loading failure

	private void fail(String message) {
		// Code dump the message and exit the game
		System.err.println(message);
		System.exit(0);
	}
}