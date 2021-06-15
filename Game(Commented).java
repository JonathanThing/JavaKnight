//Graphics &GUI imports
import java.io.File;
import java.io.IOException;
import java.awt.FontMetrics;
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

//Import ArrayList
import java.util.ArrayList;

//Import Scanner
import java.util.Scanner;

//Import Math
import java.lang.Math;

public class Game extends JFrame {
  
  //Class Variables
  static ArrayList<Enemy> enemyList = new ArrayList<Enemy>(); //Array List of Enemies
  static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>(); //Array List of Healthpacks
  static Player player; //Player Variable
  static Weapon[] weapons; //Array of Weapons
  static ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>(); //Array list of Sprites (more efficient)
  static Environment[][] map; //2d Map Array
  static GameAreaPanel gamePanel;
  static Graphics g;
  static int gameState = 0; //gamestate variable, 0 = menu, 1 = game, 2 = lose, 3 win, 4 = map editor
  static String mapName; //name of the map
  
  //Used For The Camera
  static int playerX, playerY;
  static boolean up, down, left, right;
  static boolean shooting;
  static long lastShot;
  static int mouseX, mouseY;
  
  static int offsetXMax, offsetYMax;
  static int offsetXMin, offsetYMin;
  static int worldSizeX, worldSizeY;
  static double offsetX, offsetY;
  
  //Fonts
  static Font title = new Font("Serif", Font.PLAIN, 50);
  static Font subTitle = new Font("Serif", Font.PLAIN, 25);
  static Font health = new Font("Serif", Font.PLAIN, 20);
  
  //Background Image
  static Image img = Toolkit.getDefaultToolkit().getImage("images/background.jpg");
  
  
  //GameFrame
  Game() {
    
    super("My Game");
    
    //Set the frame to full screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //Set resolution 1280x780
    this.setSize(1280, 720);
    
    //Prevent resizing of the tab
    this.setResizable(false);
    
    //Create Game panel for rendering
    gamePanel = new GameAreaPanel();
    this.add(new GameAreaPanel());
    
    //Keyboard Listener
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);
    
    //Mouse Listener
    MyMouseListener mouseListener = new MyMouseListener();
    this.addMouseListener(mouseListener);
    
    MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
    this.addMouseMotionListener(mouseMotionListener);
    
    //Sets the frame in focused
    this.requestFocusInWindow();
    
    //Thread
    Thread t = new Thread(new Runnable() {
      public void run() {
        animate();
      }
    }); 
    t.start(); //Start the Game Loop
    
  }
  
  //Main Method
  public static void main(String[] args) throws Exception {
    
    //Putting the sprites into a BufferedImage arraylist to save resources and allow easier spirite access
    sprites.add(ImageIO.read(new File("images/stonebrick.png"))); // 0
    sprites.add(ImageIO.read(new File("images/mossystone.png"))); // 1
    sprites.add(ImageIO.read(new File("images/oakwood.png"))); // 2
    sprites.add(ImageIO.read(new File("images/player.png"))); // 3
    sprites.add(ImageIO.read(new File("images/skeleton.png"))); // 4
    sprites.add(ImageIO.read(new File("images/zombie.png"))); // 5
    sprites.add(ImageIO.read(new File("images/witherskeleton.png"))); // 6
    sprites.add(ImageIO.read(new File("images/healthpack.png"))); // 7
    sprites.add(ImageIO.read(new File("images/pistol.png"))); // 8
    sprites.add(ImageIO.read(new File("images/smg.png"))); // 9
    sprites.add(ImageIO.read(new File("images/shotgun.png"))); // 10
    
    //Creates the weapons array
    weapons = new Weapon[6];
    
    //Player Weapons
    weapons[0] = new Weapon(0, 0, 26, 26, "pistol", sprites.get(8), 30, 0.4, 10);
    weapons[1] = new Weapon(0, 0, 32, 32, "smg", sprites.get(9), 10, 0.1, 10);
    weapons[2] = new Weapon(0, 0, 58, 20, "shotgun", sprites.get(10), 15, 1, 10);
    
    //Enemy Weapons
    weapons[3] = new Weapon(0, 0, 10, 10, "bow", sprites.get(0), 15, 0.8, 10);
    weapons[4] = new Weapon(0, 0, 10, 10, "buckshot", sprites.get(0), 10, 1, 10);
    
    //Initializes Map
    mapInit();
    
    //Thread Stuff 
    EventQueue.invokeLater(() ->
                           {
      Game gameInstance = new Game();
      gameInstance.setVisible(true);
    });
  }
  
  
  /**
   * mapInit
   * This method initializes the map
   */
  public static void mapInit() {
    
    enemyList.clear(); //Clears the enemies to prevent overlapping mobs on game restart
    char[][] tempMap = null; //Initializes map variable
    
    //Try and Catch for getting the map file
    try {
      tempMap = getMap("maps/map1"); //Assigns temp map to the first map
    } catch (Exception e) {
      System.out.println("Error: Please Verify Your Files"); 
    }
    
    map = new Environment[tempMap.length][tempMap[0].length]; //Creates a map variable with the same length as tempMap
    
    //Creates the worldSize
    worldSizeX = 32 * tempMap[0].length + 16; //32 is the length of one block, and 16 is an offset
    worldSizeY = 32 * tempMap.length + 32;
    
    //Loops through the 2d tempMap array
    for (int i = 0; i < tempMap.length; i++) {
      
      for (int j = 0; j < tempMap[0].length; j++) {
        
        switch (tempMap[i][j]) {
          
          case 'w': //If the letter is 'w' create a wall object
            if (Math.random() >= 0.75) { //There is a 25% random chance that a wall will be mossy
            map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(1));
          } else {
            map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(0));
          }
          break;
          
          case 'b': //If the letter is 'b' create a win block
            map[i][j] = new WinBlock((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "winblock", sprites.get(0));
            break;
            
          case 's': //If the letter is 's' create a skeleton
            enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "skeleton",
                                       sprites.get(4), 100, weapons[3]));
            break;
            
          case 'S': //If the letter is 'S' create an elite skeleton
            enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "eliteSkeleton",
                                       sprites.get(6), 200, weapons[4]));
            break;
            
          case 'h': //If the letter is 'h' add a health pacl
            healthPacks.add(
                            new HealthPack((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "HP", sprites.get(7)));
            break;
            
          case 'p': //If the letter 'p' add a player 
            player = new Player((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "player", sprites.get(3),
                                100, weapons[0]);
            playerX = (int) j * 32 + 32 / 2;
            playerY = (int) i * 32 + 23 / 2;
            break;
            
          case 'z': //If the letter is 'z' create a zombie
            enemyList.add(new Zombie((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "zombie",
                                     sprites.get(5), 100, weapons[1]));
            break;
            
        }
        
      }
      
    }
    
  }
  
  /**
   * getMap
   * This method assigns the map files to a 2d char array
   * @param file The file path for the map
   * @return Our 2d char array map 
   */
  public static char[][] getMap(String file) throws Exception {
    
    File myFile = new File(file); //Creates a new file based on the specified location
    
    //Output to message to the user via the console
    System.out.println("Attempting to read data from file.");
    
    //Create a Scanner and associate it with the file
    Scanner input = new Scanner(myFile);
    
    //These variables control row and col length for our map
    int x = input.nextInt();
    int y = input.nextInt();
    
    String line = ""; //Initializes tje line variable
    input.nextLine(); //Moves to the next line
    
    char[][] map = new char[y][x];
    
    for (int i = 0; i < y; i++) { //Loops through the file (rows)
      
      line = input.nextLine(); //Goes to the next line every time the row changes
      
      for (int j = 0; j < x; j++) { //Loops through the column
        
        map[i][j] = line.charAt(j); //Assigns the chars in the file to map[][] 
        
      }
      
    }
    
    return map; //return map
    
  }
  
  /**
   * animate
   * Repaints the screen and calls methods based on the game state
   */
  public void animate() {
    
    while (true) { //Loop forever
      
      if (gameState == 1) { //If in game perform a game tick
        
        gameTick();
        
      }
      
      else if (gameState == 5){ //If in map editor, perform a map editor tick
      }
      
      this.repaint(); // update the screen
      
    }
    
  }
  
  /** Creates and initializes a graphics panel
    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
    * @version 1.0
    * @since May 31st
    */
  private class GameAreaPanel extends JPanel {
    
    /**
     * paintComponent
     * This method initializes the map
     * @param Graphics g (used to draw to screen)
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g); //Required
      setDoubleBuffered(true); //Allows us to use double buffered drawings
      
      //Calls the render methods based on gamestate
      if (gameState == 0) { //If state == menu, render menu
        menuRender(g);
        
      } else if (gameState == 1) { //If state == game, render game
        gameRender(g);
        
      } else if (gameState == 2) { //If state == death, render death
        deathRender(g);
        
      } else if (gameState == 3) { //If state == win, render win
        winRender(g);
      }
      
    }
    
  }
  
  /**
   * menuInit
   * This method initializes the menu
   */
  public void menuInit() {
    
    //Resets the player location
    player.setX(playerX); 
    player.setY(playerY);
    
    //Resets the player health
    player.setHealth(100);
    
    //Changes the cursor from a crosshair to the default
    setCursor(Cursor.getDefaultCursor());
  }
  
  /**
   * winInit
   * This method initializes the death screen
   */
  public void winInit() {
    //Changes the cursor from a crosshair to the default
    setCursor(Cursor.getDefaultCursor());
  }
  
  /**
   * deathInit
   * This method initializes the death screen
   */
  public void deathInit() {
    //Changes the cursor from a crosshair to the default
    setCursor(Cursor.getDefaultCursor());
  }
  
  /**
   * gameInit
   * This method initializes the game
   */
  public void gameInit() {
    
    mapInit(); //Initializes map
    lastShot = System.nanoTime(); //Finds the delay between shots (cooldown time between shots)
    
    player.setHealth(100); //Resets health
    
    //Camera
    offsetXMax = worldSizeX - 1280; //Max offset = worldsizeX - resolutionX
    offsetYMax = worldSizeY - 720; //Max offset = worldsizeY - resolutionY
    offsetXMin = 0; //Min x offset
    offsetYMin = 0; //Min y offset
    
    //Sets the cursor to a crosshair
    setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("images/crosshair.png").getImage(),
                                                             new Point(16, 16), "crosshair"));
  }
  
  /**
   * gameTick
   * This method triggers 60x second (60fps) and controls basic game mechanics
   */
  public void gameTick() {
    
    
    //If health equal to or less than 0, trigger the loss screen
    if (player.getHealth() <= 0) {
      changeState(2);
    }
    
    offsetX = player.getX() - 1280 / 2;
    offsetY = player.getY() - 720 / 2;
    
    if (offsetX > offsetXMax) {
      offsetX = offsetXMax;
      
    } else if (offsetX < offsetXMin) {
      offsetX = offsetXMin;
    }
    
    if (offsetY > offsetYMax) {
      offsetY = offsetYMax;
      
    } else if (offsetY < offsetYMin) {
      offsetY = offsetYMin;
    }
    
    if (shooting && (System.nanoTime() - lastShot >= 1e9 * player.getWeapon().getFireRate())) {
      player.shoot(mouseX  + offsetX, mouseY + offsetY, sprites.get(0));
      lastShot = System.nanoTime();
    }
    
    player.movement(up, down, left, right, enemyList, map);
    
    player.moveProjectile();
    
    // Enviroment collisons
    for (int i = 0; i < map.length; i++) { //Loops through the map array
      
      for (int j = 0; j < map[0].length; j++) { //Loops through the map array
        
        if (map[i][j] != null) { //Only runs when map != null (avoids an error)
          
          if (map[i][j].getName().equals("wall")) { //Checks if the players projectile has hit a wall, deletes if it has
            map[i][j].playerHit(player);
            
          } else if (map[i][j].getName().equals("winblock")) { //Checks for a win block
            
            if (map[i][j].playerWin(player)) { //If the player is on the win block, trigger the win state
              changeState(3);
            }
            
          }
        }
        
      }
    }
    
    //Enemy
    for (int i = 0; i < enemyList.size(); i++) { //Loops through the enemy arraylist
      
      if (!enemyList.get(i).getName().equals("zombie")) { //If the enemy isn't a zombie
        ((Skeleton) enemyList.get(i)).moveProjectile(); //Move the enemy projectile
      }
      
      //Checks if an enemy bullet has hit a wall
      for (int a = 0; a < map.length; a++) { //Loops through the map
        
        for (int b = 0; b < map[0].length; b++) { //Loops through the map
          
          if (map[a][b] != null) { 
            
            if (map[a][b].getName().equals("wall")) { //If the enemy bullet hits a wall, delete it
              map[a][b].enemyHit(enemyList.get(i));
            }
            
          }
          
        }
      }
      
      //Player
      if (player.wasHit(enemyList.get(i))) { //If player was hit
        player.setHealth(player.getHealth() - enemyList.get(i).getWeapon().getDamage()); //Set player health to playerHealth - enemy damage
      }
      
      enemyList.get(i).getHit(player); //Checks if enemy was hit by player
      
      if ((enemyList.get(i)).getHealth() <= 0) { //If enemy health == 0, delete the enemy
        enemyList.remove(i);
        break;
      }
      
      if (player.getAggro().intersects(enemyList.get(i).getCollision())) { //Checks if the player is in enemy radius
        
        if (!enemyList.get(i).getName().equals("zombie")) { //If the player is in radius of a skeleton or mega skeleton, it will shoot the player
          (enemyList.get(i)).attack(player);
          
        } else { //If the player is in radius of a zombie, it will go towards the player
          ((Zombie) enemyList.get(i)).move(player, map, enemyList);
        }
        
      }
      
    }
    
    //HealthPack
    for (int i = 0; i < healthPacks.size(); i++) { //Loops through health pack arraylist
      
      if ((healthPacks.get(i)).checkCollision(player)) { //Checks if the player collides with a healthpack
        
        if (player.getHealth() >= 50) { //If your health is >50, set health to 100 (health packs heal 50, and 100 hp is the max), prevents going over max hp
          player.setHealth(100);
          
        } else { //Else if health < 50, heal by 50 
          player.setHealth(player.getHealth() + 50);
        }
        
        healthPacks.remove(i); //Remove the healthpack that was collided with
        break;
      }
      
    }
    
    //Makes it so the game ticks at a certain rate (60 = 60fps)
    try {
      Thread.sleep(16); //16 = 60fps, 33 = 30fps
    } catch (Exception exc) {
    } 
    
  }
  
  /**
   * menuRender
   * This method renders the menu
   * @param g Used to render graphics
   */
  public void menuRender(Graphics g) {
    
    g.drawImage(img, 0, 0, null); //Draws the background image
    
    g.setColor(Color.BLACK); //Sets colour to black
    
    //Draws 4 rects (one for each button)
    g.drawRect(640 - 50, 320 + 20, 100, 40);
    g.drawRect(640 - 50, 370 + 20, 100, 40);
    g.drawRect(640 - 50, 420 + 20, 100, 40);
    g.drawRect(640 - 50, 420 + 20, 100, 40);
    
    g.setColor(Color.GREEN); //Sets colour to green
    g.setFont(title); //Sets the font to title
    drawCenteredString("Java Knight", 1280, 720, g); //Writes the game name in the centre
    
    g.setFont(subTitle); //Sets the font to subtitle
    
    //Draws string for each of the options
    drawCenteredString("Play", 1280, 820, g);
    drawCenteredString("Play 2", 1280, 920, g);
    drawCenteredString("Play 3", 1280, 1020, g);
    drawCenteredString("Quit", 1280, 1120, g);
  }
  
  /**
   * deathRender
   * This method renders a screen when you die
   * @param g Used to render graphics
   */
  public void deathRender(Graphics g) {
    
    g.setFont(title); //Sets the font to title
    drawCenteredString("Game Over", 1280, 720, g); //Draws a large 'game over!' text (centered in the middle)
    
    g.setFont(subTitle); //Sets the font to subtitle
    
    //Draws the boxes and string for options
    drawCenteredString("Menu", 1280, 820, g);
    g.drawRect(640 - 50, 320 + 20, 100, 40);
    
    drawCenteredString("Quit", 1280, 920, g);
    g.drawRect(640 - 50, 370 + 20, 100, 40);
    
  }
  
  /**
   * deathRender
   * This method renders a screen when you win
   * @param g Used to render graphics
   */
  public void winRender(Graphics g) {
    
    g.setFont(title); //Sets the font to title
    drawCenteredString("You Win!!!", 1280, 720, g); //Draws a large win text (centered in the middle)
    
    g.setFont(subTitle); //Sets the font to subtitle
    
    //Draws the boxes and string for options
    drawCenteredString("Menu", 1280, 820, g);
    g.drawRect(640 - 50, 320 + 20, 100, 40);
    
    drawCenteredString("Quit", 1280, 920, g);
    g.drawRect(640 - 50, 370 + 20, 100, 40);
    
  }
  
  /**
   * gameRender
   * This method renders the screen when you play
   * @param g Used to render graphics
   */
  public void gameRender(Graphics g) {
    
    for (int i = 0; i < map.length; i++) { //Loops through the map
      
      for (int j = 0; j < map[0].length; j++) { //Loops through the map
        
        if (map[i][j] == null) { //If the map is null (no information/floor of map) draw a floor tile
          g.drawImage(sprites.get(2), (int) (j * 32 - offsetX), (int) (i * 32 - offsetY), null); //Draws sprite 2 (floor), at the map location - offsets
          
        } else { //If it's not null (floor tile) we call the .draw method to draw player, enemy etc
          map[i][j].draw(g, offsetX, offsetY);
        }
        
      }
    }
    
    //Draws a laserbea, at the crosshair
    player.laserBeam(g, (int) (mouseX + offsetX), (int) (mouseY + offsetY), offsetX, offsetY);
    
    //Draws the player
    player.draw(g, offsetX, offsetY);
    
    //Draws the playr projectile
    player.drawPlayerProjectile(g, offsetX, offsetY);
    
    //Loops through the health pack arraylist
    for (int j = 0; j < healthPacks.size(); j++) {
      healthPacks.get(j).draw(g, offsetX, offsetY); //Draws the healthpacks
    }
    
    //Loops through the enemy arraylist
    for (int i = 0; i < enemyList.size(); i++) {
      (enemyList.get(i)).draw(g, offsetX, offsetY); //Draws the enemy
      
      if (!enemyList.get(i).getName().equals("zombie")) { //If the enemy isn't a zombie, draw its projectile
        ((Skeleton) enemyList.get(i)).drawEnemyProjectile(g, offsetX, offsetY);
      }
      
    }
    
    //Draws the health above the player
    g.setColor(Color.BLACK); //Sets colour to black
    g.fillRect((int) (player.getX() - offsetX - 13), (int) (player.getY() - offsetY - 35), 32, 17); //Draws the box in which health amount is contained in
    
    g.setFont(health); //Changes to the health font
    g.setColor(Color.RED); //Sets colour to red
    g.drawString("" + (int) player.getHealth(), (int) (player.getX() - offsetX - 13), //Draws the health string at player location - offset etc
                 (int) (player.getY() - offsetY - 20));
    
    //Draws the player weapon beside the crosshair
    g.drawImage(player.getWeapon().getSprite(), (int) (mouseX - player.getWeapon().getWidth() / 2 ),
                (int) (mouseY - player.getWeapon().getHeight() / 2 - 30 ), null);
    
  }
  
  /**
   * changeState
   * This method changes the game state
   * @param state The state specified by the player
   */
  public void changeState(int state) {
    
    gameState = state; //sets the class variable state to the state specified by the player
    
    if (gameState == 0) { //If state == menu, initialize menu
      menuInit();
      
    } else if ((gameState == 1) || (gameState == 4) || (gameState == 5)) { //If state == level 1/level 2/level 3, initialize game
      gameInit();
      
    } else if (gameState == 2) { //If state == death, initialize the death/loss screen
      deathInit();
      
    } else if (gameState == 3) { ////If state == win, initialize the win screen
      winInit(); 
    }
    
  }
  
  /**
   * drawCenteredString
   * This method draws centered string in java swing, based on specified text, width, and height
   * @param text The text to be drawn
   * @param width The width of the screen or rect that you would like to centre text in
   * @param height The height of the screen or rect that you would like to centre text in
   * @param g Draws to the screen
   */
  public void drawCenteredString(String text, int width, int height, Graphics g) {
    FontMetrics fontMetrics = g.getFontMetrics(); //Initializes the font metrics varialbe (contains info about text)
    
    int x = (width - fontMetrics.stringWidth(text)) / 2; //The x of the center = width - font width
    int y = (fontMetrics.getAscent() + (height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2) - 50; //The y of the center = ascent + (height - (ascent + descent) )
    
    g.drawString(text, x, y); //Draws the string at the calculated location
  }
  
  /** Listens and reacts based on keyboard input
    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
    * @version 1.0
    * @since May 31st
    */
  class MyKeyListener implements KeyListener {
    
    
    /**
     * keyTyped
     * This method does stuff when certain keys are types
     * @param e A keyEvent
     */
    public void keyTyped(KeyEvent e) {
      
      /** 
       ////////////////////////////////////////////
       This section was used for debugging purposes
       ////////////////////////////////////////////
       
       if (e.getKeyChar() == 'm') { //Changes to menu when m is pressed
       changeState(0);
       }
       
       if (e.getKeyChar() == 'n') { //Changes to game when n is pressed
       changeState(1);
       }
       
       if (e.getKeyChar() == 'b') { //Changes to death screen when b is pressed
       changeState(2);
       }
       
       if (e.getKeyChar() == 'l') { //Kills the player when l is types
       player.setHealth(player.getHealth() - player.getHealth());
       }
       */
      
      if (e.getKeyChar() == '1') { //If the player hits '1' switch to the pistol/revolver
        player.setWeapon(weapons[0]);
        
      } else if (e.getKeyChar() == '2') { //If the player hits '2' switch to the SMG/uzi
        player.setWeapon(weapons[1]);
        
      } else if (e.getKeyChar() == '3') { //If the player hits '3' switch to the shotgun
        player.setWeapon(weapons[2]);
      }
      
    }
    
    /**
     * keyTyped
     * This method does stuff when certain keys are pressed
     * @param e A keyEvent
     */
    public void keyPressed(KeyEvent e) {
      
      if (e.getKeyCode() == 'W') { //If the player hits/holds 'W', move up until they stop
        up = true;
      }
      
      if (e.getKeyCode() == 'S') { //If the player hits/holds 'S', move down until they stop
        down = true;
      }
      
      if (e.getKeyCode() == 'A') { //If the player hits/holds 'A', move left until they stop
        left = true;
      }
      
      if (e.getKeyCode() == 'D') { //If the player hits/holds 'D', move right until they stop
        right = true;
      }
      
    }
    
    /**
     * keyTyped
     * This method does stuff when certain keys are released
     * @param e A keyEvent
     */
    public void keyReleased(KeyEvent e) {
      
      if (e.getKeyCode() == 'W') { //If the player lets go of 'W', stop moving up
        up = false;
      }
      
      if (e.getKeyCode() == 'S') { //If the player lets go of 'S', stop moving down
        down = false;
      }
      
      if (e.getKeyCode() == 'A') { //If the player lets go of 'A', stop moving left
        left = false;
      }
      
      if (e.getKeyCode() == 'D') { //If the player lets go of 'D', stop moving right
        right = false;
      }
      
    }
  }
  
  //----------------
  //End of Key Input
  //----------------
  
  
  /** Listens and reacts based on mouse motion
    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
    * @version 1.0
    * @since May 31st
    */
  class MyMouseMotionListener implements MouseMotionListener {
    
    /**
     * mouseDragged
     * This method does stuff when you drag the mouse
     * @param e A mouse event
     */
    public void mouseDragged(MouseEvent e) { //Move the mouse by the offset amount, creates smoother dragging 
      mouseX = e.getX() - 7;
      mouseY = e.getY() - 30;
    }
    
    /**
     * mouseMoved
     * This method does stuff when you move the mouse
     * @param e A mouse event
     */
    public void mouseMoved(MouseEvent e) { //Move the mouse by the offset amount, creates smoother motions
      mouseX = e.getX() - 7;
      mouseY = e.getY() - 30;
      
    }
    
  }
  
  //----------------------
  //End of Motion Listener
  //----------------------
  
  /** Listens and reacts based on mouse clicks
    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
    * @version 1.0
    * @since May 31st
    */
  class MyMouseListener implements MouseListener {
    
    
    /**
     * mouseClicked
     * This method does stuff when you click the mouse
     * @param e A mouse event
     */
    public void mouseClicked(MouseEvent e) {
      
    }
    
    /**
     * mousePressed
     * This method does stuff when you press the mouse
     * @param e A mouse event
     */
    public void mousePressed(MouseEvent e) {
      
      //Menu Buttons
      if ((gameState == 0) && (e.getX() >= 590) && (e.getX() <= 690)) { //If state == menu and your mouse is within the x range of the buttons
        
        if ((e.getY() >= 370) && (e.getY() <= 410)) { //If you click on map 1, set map to map 1 and change state to 1 (game)
          mapName = "map1";
          changeState(1);
          
          
        } else if ((e.getY() >= 410) && (e.getY() <= 450)) { //If you click on map 2, set map to map 2 and change state to 1 (game)
          mapName = "map2";
          changeState(1);
          
          
        } else if ((e.getY() >= 450) && (e.getY() <= 490)) { //If you click on map3, set map to map 3 and change state to 1 (game)
          mapName = "map3";
          changeState(1);
          
        } else if ((e.getY() >= 490) && (e.getY() <= 540)) { //If you click on the quit button, exit the program
          System.exit(0);
        }
        
      } else if (gameState == 1) { //If you're in game and hold down the mouse button, start shooting
        shooting = true;
        
        
        //Death Screen buttons
        
      }else if ((gameState == 2) && (e.getX() >= 590) && (e.getX() <= 690)) { //If state == death and your mouse is within the x range of the buttons
        
        if ( (e.getY() >= 340) && (e.getY() <= 410) ) { //If you click on the menu button, change state to menu
          changeState(0);
          
        }else if ( (e.getY() >= 370) && (e.getY() <= 460) ) { //If you click on the quit button, exit the game
          System.exit(0);
        }
        
        //Win Screen buttons
        
      }else if ((gameState == 2) && (e.getX() >= 590) && (e.getX() <= 690)) { //If state == win and your mouse is within the x range of the buttons
        
        if ( (e.getY() >= 340) && (e.getY() <= 410) ) { //If you click on the menu button, change state to menu
          changeState(0);
          
        }else if ( (e.getY() >= 370) && (e.getY() <= 460) ) { //If you click on the quit button, exit the game
          System.exit(0);
        }
        
      }
      
    }
    
    /**
     * mouseReleased
     * This method does stuff when you release the mouse
     * @param e A mouse event
     */
    public void mouseReleased(MouseEvent e) { //If you were previously holding down the mouse button during states 1, 4 or 5 (games), stop shooting
      if (gameState == 1) {
        shooting = false;
      }
      
    }
    
    /**
     * mouseEntered
     * This method does stuff when you move the mouse onto the window
     * @param e A mouse event
     */
    public void mouseEntered(MouseEvent e) {
      
    }
    
    /**
     * mouseExited
     * This method does stuff when you move the mouse off the window
     * @param e A mouse event
     */
    public void mouseExited(MouseEvent e) {
      
    }
    
    //----------------------
    //End of MouseListener
    //----------------------
    
  }
}