package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic.
 *
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 *
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. aliens killed, played died) and will take
 * appropriate game actions.
 *
 * @author Kevin Glass
 */
public class Game extends Canvas {
    /** The strategy that allows us to use accelerate page flipping */
    private BufferStrategy strategy;
    /** True if the game is currently "running", i.e. the game loop is looping */
    private boolean gameRunning = true;
    /** The list of all the entities that exist in our game */
    private ArrayList entities = new ArrayList();
    /** The list of entities that need to be removed from the game this loop */
    private ArrayList removeList = new ArrayList();
    /** The entity representing the player */
    private Entity ship;
    /** The speed at which the player's ship should move (pixels/sec) */
    private double moveSpeed = 300;
    /** The time at which last fired a shot */
    private long lastFire = 0;
    /** The interval between our players shot (ms) */
    private int firingInterval = 500;
    /** The number of aliens left on the screen */
    private int alienCount;
    private int alienCount2;
    private int alienCount3;
    long lastLoopTime;

    /** The message to display which waiting for a key press */
    private String message = "";
    /** True if we're holding up game play until a key has been pressed */
    private boolean waitingForKeyPress = true;
    /** True if the left cursor key is currently pressed */
    private boolean leftPressed = false;
    /** True if the right cursor key is currently pressed */
    private boolean rightPressed = false;
    /** True if we are firing */
    private boolean firePressed = false;
    /** True if game logic needs to be applied this loop, normally as a result of a game event */
    private boolean logicRequiredThisLoop = false;

    /**
     * Construct our game and set it running.
     */
    public Game() {
        // create a frame to contain our game
        JFrame frame = new JFrame("Space Invaders 101");

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        // setup our canvas size and put it into the content of the frame
        setBounds(0,0,800,600);
        panel.add(this);

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        // setIgnoreRepaint(true);                                                                                      *

        // finally make the window visible
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialise the entities in our game so there's something
        // to see at startup
        initEntities();
    }

    /**
     * Start a fresh game, this should clear out any old data and
     * create a new set.
     */
    private void startGame() {
        // clear out any existing entities and initialise a new set
        entities.clear();
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
    }

    /**
     * Initialise the starting state of the entities (ship and aliens). Each
     * entity will be added to the overall list of entities in the game.
     */
    private void initEntities() {
        // create the player ship and place it roughly in the center of the screen
        ship = new ShipEntity(this,"ship.gif",370,550);
        entities.add(ship);

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount = 0;
        for (int row=0;row<2;row++) {
            for (int x=0;x<3;x++) {
                Entity alien = new AlienEntity(this,"alien.gif",100+(x*50),(50)+row*30);
                entities.add(alien);
                alienCount++;
            }
        }
    }

    private void initLevel2() {

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount2 = 1;
        for (int row=0;row<2;row++) {
            for (int x=0;x<1;x++) {
                Entity alien2 = new AlienEntity(this,"hotdog.png",100+(x*50),(50)+row*30);
                alien2.setHorizontalMovement(alien2.getHorizontalMovement() * 2.50);
                entities.add(alien2);
                alienCount2++;
            }
        }
    }

    private void initLevel3() {

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount3 = 1;
        for (int row=0;row<2;row++) {
            for (int x=0;x<2;x++) {
                Entity alien3 = new AlienEntity(this,"Hamburger.png",100+(x*50),(50)+row*30);
                alien3.setHorizontalMovement(alien3.getHorizontalMovement() * 3.50);
                entities.add(alien3);
                alienCount3++;
            }
        }
    }


    /**
     * Notification from a game entity that the logic of the game
     * should be run at the next opportunity (normally as a result of some
     * game event)
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    /**
     * Remove an entity from the game. The entity removed will
     * no longer move or be drawn.
     *
     * @param entity The entity that should be removed
     */
    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    /**
     * Notification that the player has died.
     */
    public void notifyDeath() {
        message = "You are wasted, want to try again?";
        waitingForKeyPress = true;
    }

    /**
     * Notification that the player has won since all the aliens
     * are dead.
     */

    public void notifyWin() {
        message = "You killed all of them, You Won!";
        waitingForKeyPress = true;
    }

    /**
     * Notification that an alien has been killed
     */
    public void notifyAlienKilled() {
        // reduce the alien count, if there are none left, the player has won!
        alienCount--;

       if(alienCount == 0) {
           initLevel2();

       }
        alienCount2--;
        if(alienCount2==0) {
            initLevel3();
        }
        alienCount3--;
        if(alienCount3==0) {
            notifyWin();
        }

        // if there are still some aliens left then they all need to get faster, so
        // speed up all the existing aliens
        for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }

    }

    /**
     * Attempt to fire a shot from the player. Its called "try"
     * since we must first check that the player can fire at this
     * point, i.e. has he/she waited long enough between shots
     */
    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this,"shot.gif",ship.getX()+10,ship.getY()-30);
        entities.add(shot);
    }

    /**
     * The main game loop. This loop is running during all game
     * play as is responsible for the following activities:
     * <p>
     * - Working out the speed of the game loop to update moves
     * - Moving the game entities
     * - Drawing the screen contents (entities, text)
     * - Updating game events
     * - Checking Input
     * <p>
     */

    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // keep looping round til the game ends
        while (gameRunning) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long distance = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // Get hold of a graphics context for the accelerated
            // surface and blank it out
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);

            // cycle round asking each entity to move itself
            if (!waitingForKeyPress) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = (Entity) entities.get(i);

                    entity.move(distance);
                }
            }

            // cycle round drawing all the entities we have in the game
            for (int i=0;i<entities.size();i++) {
                Entity entity = (Entity) entities.get(i);

                entity.draw(g);
            }

            // brute force collisions, compare every entity against
            // every other entity. If any of them collide notify
            // both entities that the collision has occurred
            for (int p=0;p<entities.size();p++) {
                for (int s=p+1;s<entities.size();s++) {
                    Entity me = (Entity) entities.get(p);
                    Entity him = (Entity) entities.get(s);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            }

            // remove any entity that has been marked for clear up
            entities.removeAll(removeList);
            removeList.clear();

            // if a game event has indicated that game logic should
            // be resolved, cycle round every entity requesting that
            // their personal logic should be considered.
            if (logicRequiredThisLoop) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                }

                logicRequiredThisLoop = false;
            }

            // if we're waiting for an "any key" press then draw the
            // current message
            if (waitingForKeyPress) {
                g.setColor(Color.white);
                g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
                g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
            }

            // finally, we've completed drawing so clear up the graphics
            // and flip the buffer over
            g.dispose();
            strategy.show();

            // resolve the movement of the ship. First assume the ship
            // isn't moving. If either cursor key is pressed then
            // update the movement appropriately
            ship.setHorizontalMovement(0);

            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            }

            // if we're pressing fire, attempt to fire
            if (firePressed) {
                tryToFire();
            }

            // finally pause for a bit. Note: this should run us at about
            // 100 fps but on windows this might vary each loop due to
            // a bad implementation of timer
            //try { Thread.sleep(10); } catch (Exception e) {}
        }
    }

    /**
     * A class to handle keyboard input from the user. The class
     * handles both dynamic input during game play, i.e. left/right
     * and shoot, and more static type input (i.e. press any key to
     * continue)
     *
     * This has been implemented as an inner class more through
     * habit then anything else. Its perfectly normal to implement
     * this as separate class if slight less convenient.
     *
     * @author Kevin Glass
     */
    private class KeyInputHandler extends KeyAdapter {
        /** The number of key presses we've had while waiting for an "any key" press */
        private int pressCount = 1;

        /**
         * Notification from AWT that a key has been pressed. Note that
         * a key being pressed is equal to being pushed down but *NOT*
         * released. That's where keyTyped() comes in.
         *
         * @param e The details of the key that was pressed
         */
        public void keyPressed(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "press"
            if (waitingForKeyPress) {
                return;
            }


            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }
        }

        /**
         * Notification from AWT that a key has been released.
         *
         * @param e The details of the key that was released
         */
        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "released"
            if (waitingForKeyPress) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }

        /**
         * Notification from AWT that a key has been typed. Note that
         * typing a key means to both press and then release it.
         *
         * @param e The details of the key that was typed.
         */
        public void keyTyped(KeyEvent e) {
            // if we're waiting for a "any key" type then
            // check if we've received any recently. We may
            // have had a keyType() event from the user releasing
            // the shoot or move keys, hence the use of the "pressCount"
            // counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now received our key typed
                    // event we can mark it as such and start
                    // our new game
                    waitingForKeyPress = false;
                    startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            // if we hit escape, then quit the game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }
}

/**
 * The entry point into the game. We'll simply create an
 * instance of class which will start the display and game
 * loop.
 *
 * @param argv The arguments that are passed into our game
 */