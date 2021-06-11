package com.company;

public class AlienEntity extends Entity {
    // The speed at which the alient moves horizontally.
    private double moveSpeed = 75;
    // The game in which the entity exists.
    private Game game;

    // Create a new alien entity.
    public AlienEntity(Game game,String ref,int x,int y) {
        super(ref,x,y);

        this.game = game;
        dx = -moveSpeed;
    }

    // Request that this alien moved based on time elapsed.

    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of
        // the screen and are moving right, request a logic update
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }

        // Proceed with normal move
        super.move(delta);
    }


     // Update the game logic related to aliens
    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;

        // if you have reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            game.notifyDeath();
        }
    }

    // Notification that this alien has collided with another entity
    public void collidedWith(Entity other) {

    }
}
