package com.company;


public class ShipEntity extends Entity {
        // The game in which the ship exists.
    private Game game;

    
      // Create a new entity to represent the players ship.
    public ShipEntity(Game game,String ref,int x,int y) {
        super(ref,x,y);

        this.game = game;
    }


    // Request that the ship move itself.
    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            return;
        }

        if ((dx > 0) && (x > 750)) {
            return;
        }

        super.move(delta);
    }

    
    public void collidedWith(Entity other) {
        // if its an alien notify the game that the player
        // is dead.
        if (other instanceof AlienEntity) {
            game.notifyDeath();
        }
    }
}
