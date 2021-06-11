package com.company;

public class Main {
    public static void main(String[] args) {
        Game g =new Game();

        // Start the main game loop, note: this method will not
        // return until the game has finished running. Hence we are
        // using the actual main thread to run the game.
        g.gameLoop();

    }

}
