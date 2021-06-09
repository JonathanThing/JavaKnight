//Graphics &GUI imports
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.FontMetrics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

//Import ArrayList
import java.util.ArrayList;
import java.util.Scanner;

//Import Math
import java.lang.Math;

public class Game extends JFrame {
  
  static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
  static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>();
  
  static double playerX = 120;
  static double playerY = 120;
  
  static int thingX, thingY;
  static Player player; 
  
  /****************** CLASS VARIABLES *******************/
  /** The variables can be accessed across all methods **/
  /******************************************************/
  
  static Environment[][] map;
  static GameAreaPanel gamePanel;
  static Graphics g;
  static int gameState = 0; // 0 = Menu, 1 = Game, 2 = dead
  static boolean up, down, left, right;
  static int offsetMaxX;
  static int offsetMaxY;
  static int worldSizeX;
  static int worldSizeY;
  static double camX;
  static double camY;
  static int offsetMinX = 0;
  static int offsetMinY = 0;
  static Font title = new Font("Serif", Font.PLAIN, 50);
  static Font subTitle = new Font("Serif", Font.PLAIN, 25);
  static Font health = new Font("Serif", Font.PLAIN, 20);
  
  /***************************************************************/
  /** GameFrame - Setups up the Window and Starts displaying it **/
  /***************************************************************/
  
  Game() {
    
    super("My Game");
    
    // Set the frame to full screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Set resolution 1280x780
    this.setSize(1280, 720);
    
    // Prevent resizing of the tab
    this.setResizable(false);
    
    // Create Game panel for rendering
    gamePanel = new GameAreaPanel();
    this.add(new GameAreaPanel());
    
    // Keyboard Listener
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);
    
    // Mouse Listener
    MyMouseListener mouseListener = new MyMouseListener();
    this.addMouseListener(mouseListener);
    
    // Sets the frame in focused
    this.requestFocusInWindow();
    
    // Thread
    Thread t = new Thread(new Runnable() {
      public void run() {
        animate();
      }
    }); // start the gameLoop
    t.start();
    
  }
  
  /************************ End of GameFrame ***********************/
  
  /********************* Main Method ************************/
  public static void main(String[] args) throws Exception {
    
    mapInit();
    
    System.out.println("?>?");
    
    EventQueue.invokeLater(() ->
                           
                           {
      Game gameInstance = new Game();
      gameInstance.setVisible(true);
    });
  }
  
  public static void mapInit(){
    enemyList.clear();
    char [][] temp = null;
    
    try{
    temp = getMap("maps/map1");
    }catch(Exception e){
    }
    
    map = new Environment[temp.length][temp[0].length];
    
    worldSizeX = 64 * temp[0].length;
    worldSizeY = 64 * temp.length;
    
    for (int i = 0; i < temp.length; i++) {
      for (int j = 0; j < temp[0].length; j++) {
        switch (temp[i][j]) {
          case 'w':
            map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall");
            break;
            
          case 'e':
            enemyList.add(new Enemy((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, 25, 25, "Enemy", 100, "idk",
                                    player));
            break;
            
          case 'h':
            healthPacks.add(new HealthPack((int) j * 32 + 32 / 2, (int) i * 32 + 23 / 2, 25, 25, "HP"));
            break;
            
          case 'p':
            player = new Player((int) j * 32 + 32 / 2, (int) i * 32 + 23 / 2, 25, 25, "player", 100, "sword", 50);
            break;
        }
      }
    }
  }
  
  public static char[][] getMap(String file) throws Exception {
    
    File myFile = new File(file);
    
    // output to message to the user via the console
    System.out.println("Attempting to read data from file.");
    
    // Create a Scanner and associate it with the file
    Scanner input = new Scanner(myFile);
    
    int x = input.nextInt();
    int y = input.nextInt();
    String thing = "";
    
    input.nextLine();
    
    char[][] a = new char[y][x];
    
    for (int i = 0; i < y; i++) {
      thing = input.nextLine();
      for (int j = 0; j < x; j++) {
        a[i][j] = thing.charAt(j);
        
      }
      
    }
    
    return a;
    
  }
  
  /****** end of Main *********************************/
  
  /********************* Animate - Gameloop ************************/
  /******* This section is where the games state is updated. *******/
  /*****************************************************************/
  public void animate() {
    
    // Intialize functions
    
    while (true) {
      
      if (gameState == 0) {
        
        menuTick();
        
      } else if (gameState == 1) { // Game State
        
        gameTick();
        
      }
      
      this.repaint(); // update the screen
    }
    
  }
  
  /****** End of Animate *********************************/
  
  // Inner class - JPanel
  private class GameAreaPanel extends JPanel {
    
    /************************** PaintComponenet ************************/
    /** This section is where the screen is drawn **/
    /*******************************************************************/
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g); // required
      setDoubleBuffered(true);
      
      // Call render methods
      if (gameState == 0) {
        menuRender(g);
        
      } else if (gameState == 1) { // Game State
        gameRender(g);
        
      } else if (gameState == 2) {
        deathRender(g);
      }
    }
  }
  
  /****** End of paintComponent *********************************/
  
  public void menuInit() {
    
  }
  
  public void deathInit(){
  }
  
  public void gameInit() {
    player.setHealth(100);
    mapInit();
    
    offsetMaxX = worldSizeX - 1280;
    offsetMaxY = worldSizeY - 720;
    offsetMinX = 0;
    offsetMinY = 0;
  }
  
  // Updating ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  // Menu update
  public void menuTick() {
    
  }
  
  //death update
  public void deathTick(){
  }
  
  // Game update
  public void gameTick() {
    
    if(player.getHealth() <= 0){
      changeState(2);
    }
    
    camX = player.getX() - 1280 / 2;
    camY = player.getY() - 720/ 2;
    
    if (camX > offsetMaxX) {
      camX = offsetMaxX;
    }else if (camX < offsetMinX) {
      camX = offsetMinX;
    } 
    if (camY > offsetMaxY){
      camY = offsetMaxY;
    }else if (camY < offsetMinY){
      camY = offsetMinY;
    }
    
    player.movement(up, down, left, right, enemyList, map);
    
    player.moveProjectile();
    
    // Enemy
    for (int i = 0; i < enemyList.size(); i++) {
      
      (enemyList.get(i)).moveProjectile();
      
      if (player.wasHit(enemyList.get(i))) {
        player.setHealth(player.getHealth() - 5);
      }
      
      if ((enemyList.get(i)).getHit(player)) {
//    player.removeProjectile(i); //WARNING DOES NOT WORK WILL NEED TO CREATE ID FOR EACH BULLET TO KNOW WHEN IT GET IT
        enemyList.get(i).setHealth(enemyList.get(i).getHealth() - 10);
      }
      
      if ((enemyList.get(i)).getHealth() == 0) {
        enemyList.remove(i);
        break;
      }
      
      if (getRandomNumber(1, 50) == 1) {
        (enemyList.get(i)).shoot(player);
      }
      
    }
    
    // HealthPack
    for (int i = 0; i < healthPacks.size(); i++) {
      
      if ((healthPacks.get(i)).checkCollision(player)) {
        player.setHealth(player.getHealth() + 100);
        healthPacks.remove(i);
        break;
      }
      
    }
    
    try {
      Thread.sleep(16); // 16 = 60fps, 33 = 30fps
    } catch (Exception exc) {
      
    } // delay
    
  }
  
  // Rendering ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  // Rendering the Menu
  public void menuRender(Graphics g) {
    
    g.setFont(title);
    drawCenteredString("Java Knight", 1280, 720, g);
    
    g.setFont(subTitle);
    drawCenteredString("Play", 1280, 820, g);
    g.drawRect(640-50,320+20,100,40); 
    
    drawCenteredString("Quit", 1280, 920, g);
    g.drawRect(640-50,370+20,100,40); 
    
  }
  
  //rendering when you die
  public void deathRender(Graphics g){
    
    g.setFont(title);
    drawCenteredString("Game Over", 1280, 720, g);
    
    g.setFont(subTitle);
    drawCenteredString("Menu", 1280, 820, g);
    g.drawRect(640-50,320+20,100,40); 
    
    drawCenteredString("Quit", 1280, 920, g);
    g.drawRect(640-50,370+20,100,40); 
    
  }
  
  // Rendering the Game
  public void gameRender(Graphics g) {
    
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (map[i][j] == null) {
          
        } else {
          map[i][j].draw(g, camX, camY);
        }
      }
    }
    
    player.draw(g, camX, camY);
    
    player.drawPlayerProjectile(g, camX, camY);
    for (int j = 0; j < healthPacks.size(); j++) {
      healthPacks.get(j).draw(g, camX, camY);
    }
    
    for (int i = 0; i < enemyList.size(); i++) {
      
      (enemyList.get(i)).draw(g, camX, camY);
      (enemyList.get(i)).drawEnemyProjectile(g, camX, camY);
      
    }
    
    g.setFont(health);
    
    g.setColor(Color.RED);
    g.drawString("Health: " + player.getHealth(), 10, 30);
    
    for (int i = 0; i < enemyList.size(); i++) {
      g.drawString("Eny Health: " + enemyList.get(i).getHealth(), 10, 90 + i * 25);
    }
    
  }
  
  // Method to change the states of the game and intialize the things needed.
  public void changeState(int a) {
    
    gameState = a;
    
    if (gameState == 0) {
      menuInit();
    } else if (gameState == 1) {
      gameInit();
    }else if (gameState == 2){
      deathInit();
    }
    
  }
  
  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
  
  public void drawCenteredString(String text, int width, int height, Graphics g) {
    FontMetrics fontMetrics = g.getFontMetrics();
    int x = (width - fontMetrics.stringWidth(text)) / 2;
    int y = (fontMetrics.getAscent() + (height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2) - 50;
    g.drawString(text, x, y);
  }
  
  /***************************** Key Listener ************************/
  /** This section is where keyboard input is handled **/
  /** You will add code to respond to key presses **/
  /*******************************************************************/
  class MyKeyListener implements KeyListener {
    
    public void keyTyped(KeyEvent e) {
      
      if (e.getKeyChar() == 'm') {
        changeState(0);
      }
      
      if (e.getKeyChar() == 'n') {
        changeState(1);
      }
      
      if (e.getKeyChar() == 'b') {
        changeState(2);
      }
      
      if (e.getKeyChar() == 'l') {
        player.setHealth(player.getHealth() - player.getHealth());
      }
      
    }
    
    public void keyPressed(KeyEvent e) {
      
      if (e.getKeyCode() == 'W') {
        up = true;
      }
      if (e.getKeyCode() == 'S') {
        down = true;
      }
      if (e.getKeyCode() == 'A') {
        left = true;
      }
      if (e.getKeyCode() == 'D') {
        right = true;
      }
    }
    
    public void keyReleased(KeyEvent e) {
      
      if (e.getKeyCode() == 'W') {
        up = false;
      }
      if (e.getKeyCode() == 'S') {
        down = false;
      }
      if (e.getKeyCode() == 'A') {
        left = false;
      }
      if (e.getKeyCode() == 'D') {
        right = false;
      }
    }
  }
  
  /****** Key Listener *********************************/
  
  /**************************** Mouse Listener ************************/
  /** This section is where mouse input is handled **/
  /** You may have to add code to respond to mouse clicks **/
  /********************************************************************/
  class MyMouseListener implements MouseListener {
    
    public void mouseClicked(MouseEvent e) {
      
    }
    
    public void mousePressed(MouseEvent e) {
      
      // menu buttons
      if((gameState == 0) && (e.getX() >= 590) && (e.getX() <= 690) && (e.getY() >= 340) && (e.getY() <= 410)){
        changeState(1);
      } else if((gameState == 0) && (e.getX() >= 590) && (e.getX() <= 690) && (e.getY() >= 370) && (e.getY() <= 460)){
        System.exit(0);
      // shoot 
      } else if (gameState == 1) {
        player.shoot(e.getX() - 7 + camX, e.getY() - 30 + camY); // Mouse offset cause it is very clown lol
      // death screen buttons
      } else if((gameState == 2) && (e.getX() >= 590) && (e.getX() <= 690) && (e.getY() >= 340) && (e.getY() <= 410)){
        changeState(0);
      } else if((gameState == 2) && (e.getX() >= 590) && (e.getX() <= 690) && (e.getY() >= 370) && (e.getY() <= 460)){
        System.exit(0);
      }
     
    }  
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
  }
  
  /****** Mouse Listener *********************************/
}