package com.company;

import java.awt.*;

public abstract class Entity {
    // The current x location of this entity. 
    protected double x;
    // The current y location of this entity. 
    protected double y;
    // The Image that represents this entity.
    protected Image Image;
    // The current speed of this entity horizontally.
    protected double dx;
    // The current speed of this entity vertically.
    protected double dy;
    // The rectangle used for this entity during collisions  resolution 
    private Rectangle me = new Rectangle();
    // The rectangle used for other entities during collision resolution 
    private Rectangle him = new Rectangle();


    // Construct a entity based on a Image image and a location.
    public Entity(String ref,int x,int y) {
        this.Image = ImageStore.get().getImage(ref);
        this.x = x;
        this.y = y;
    }

    // Request that this entity move itself based on a certain ammount of time passing.
    public void move(long delta) {
        // Update the location of the entity based on move speeds.
        x += (delta * dx) / 1500;
        y += (delta * dy) / 500;
    }

    // Set the horizontal speed of this entity.
    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }

    // Set the vertical speed of this entity
    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }

    // Get the horizontal speed of this entity
    public double getHorizontalMovement() {
        return dx;
    }

    // Get the vertical speed of this entity.
    public double getVerticalMovement() {
        return dy;
    }

    // Draw this entity to the graphics context provided.
    public void draw(Graphics g) {
        Image.draw(g,(int) x,(int) y);
    }

    // Do the logic associated with this entity.
    public void doLogic() {
    }

    // Get the x location of this entity
    public int getX() {
        return (int) x;
    }

    // Get the y location of this entity
    public int getY() {
        return (int) y;
    }

    // Check if this entity collised with another.
    public boolean collidesWith(Entity other) {
        me.setBounds((int) x,(int) y,Image.getWidth(),Image.getHeight());
        him.setBounds((int) other.x,(int) other.y,other.Image.getWidth(),other.Image.getHeight());

        return me.intersects(him);
    }

    public abstract void collidedWith(Entity other);
}