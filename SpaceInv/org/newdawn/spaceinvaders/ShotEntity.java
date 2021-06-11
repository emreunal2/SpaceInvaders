package com.company;

public class ShotEntity extends Entity {
    // The vertical speed at which the players shot moves
    private double moveSpeed = -1300;

    // The game in which this entity exists
    private Game game;

    // True if this shot has been used.
    private boolean used = false;

    // Create a new shot from the player
    public ShotEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);

        this.game = game;

        dy = moveSpeed;
    }

    
    // Request that this shot moved based on time elapsed
    public void move(long delta) {
        // Proceed with normal move
        super.move(delta);

        // If we shot off the screen remove THIS entity
        if (y < 0) {
            game.removeEntity(this);
        }
    }

    // Notification that this shot has collided with another entity
    public void collidedWith(Entity other) {
        // Prevents double kills if you have already hit something,
        if (used) {
            return;
        }

        // If you have hit an alien kill it.
        if (other instanceof AlienEntity) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // Notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }
    }
}
