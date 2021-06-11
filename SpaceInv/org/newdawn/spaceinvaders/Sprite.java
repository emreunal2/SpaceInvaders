package org.newdawn.spaceinvaders;

import java.awt.Graphics;
import java.awt.Image;


public class Sprite {
	// The image to be drawn for this sprite. 
	private Image image;
	
	
	public Sprite(Image image) {
		this.image = image;
	}
	
	
	// Get the width of the drawn sprite.	
	public int getWidth() {
		return image.getWidth(null);
	}

	// Get the height of the drawn sprite.
	public int getHeight() {
		return image.getHeight(null);
	}
	
	//  Draw image. 
	public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,null);
	}
}