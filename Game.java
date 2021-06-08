
//Graphics &GUI imports
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

//Import ArrayList
import java.util.ArrayList;

//Import Math
import java.lang.Math;

public class Game extends JFrame {
  
  static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
  static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>();
  
  static double playerX = 50;
  static double playerY = 50;
  
  static int thingX, thingY;
  
  static Player player = new Player(playerX, playerY, 50, 50, "player", 100, "sword", 50);
  
  static Font title = new Font("Serif", Font.PLAIN, 50);
  static Font subTitle = new Font("Serif", Font.PLAIN, 25);
  static Font health = new Font("Serif", Font.PLAIN, 20);
  
  /****************** CLASS VARIABLES *******************/
  /** The variables can be accessed across all methods **/
  /******************************************************/
  
  static GameAreaPanel gamePanel;
  static Graphics g;
  static int gameState = 0; // 0 = Menu, 1 = Game
  static boolean up, down, left, right;
  
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
  public static void main(String[] args) {
    
    spawnMobs();
    
    System.out.println("?>?");
    
    EventQueue.invokeLater(() -> {
      Game gameInstance = new Game();
      gameInstance.setVisible(true);
    });
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
      
      else if (gameState == 2){
        
        endTick();
        
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
        endRender(g);
      }
    }
  }
  
  /****** End of paintComponent *********************************/
  
  public void menuInit() {
    player.setHealth(100);
  }
  
  public void gameInit() {
    player.setHealth(100);
    
    if (enemyList.size() != 0){
      enemyList.clear();
      spawnMobs();
    }
    else if (enemyList.size() == 0){
      spawnMobs();
    }
    
  }
  
  public static void spawnMobs() {
    enemyList.add(new Enemy(200, 400, 50, 50, "Enemy", 100, "idk", player));
    enemyList.add(new Enemy(400, 400, 50, 50, "Enemy", 100, "idk", player));
    enemyList.add(new Enemy(200, 200, 50, 50, "Enemy", 100, "idk", player));
    enemyList.add(new Enemy(600, 300, 50, 50, "Enemy", 100, "idk", player));
  }
  
  public void endInit(){
    
  }
  
  // Updating ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  //Menu update
  public void menuTick() {

  }
  
  public void endTick(){
    
  }
  
  
  //Game update
  public void gameTick() {
    
    if(player.getHealth() < 0){
      changeState(2);
    }
    
    player.movement(up, down, left, right, enemyList);
    
    player.moveProjectile();
    
    // Enemy
    for (int i = 0; i < enemyList.size(); i++) {
      
      (enemyList.get(i)).moveProjectile();
      
      if (player.wasHit(enemyList.get(i))) {
        player.setHealth(player.getHealth()-5);
        break;
      }
      
      if ((enemyList.get(i)).getHit(player)) {
//    player.removeProjectile(i); //WARNING DOES NOT WORK WILL NEED TO CREATE ID FOR EACH BULLET TO KNOW WHEN IT GET IT
        enemyList.get(i).setHealth(enemyList.get(i).getHealth() - 5);
        break;
      }
      
      if ((enemyList.get(i)).getHealth() == 0) {
        enemyList.remove(i);
        break;
      }
      
      if (getRandomNumber(1, 50) == 1) {
        (enemyList.get(i)).shoot(player);
        break;
      }
      
    }
    
    // HealthPack
    for (int i = 0; i < healthPacks.size(); i++) {
      
      if ((healthPacks.get(i)).checkCollision(healthPacks.get(i), player)) {
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
  
  //Rendering the Menu
  public void menuRender(Graphics g) {
    
    g.setFont(title);
    drawCenteredString("Java Knight", 1280, 720, g);
    
  }
  
  public void endRender(Graphics g){
    
    g.setFont(title);
    drawCenteredString("Game Over", 1280, 720, g);
    
    g.setFont(subTitle);
    drawCenteredString("Click Anywhere To Try Again", 1280, 800, g);
    
  }
  
  //Rendering the Game
  public void gameRender(Graphics g) {
    
    g.setFont(health);
    g.drawString("Health: " + player.getHealth(), 10, 30);
    
    for (int i = 0; i < enemyList.size(); i++) {
      g.drawString("Eny Health: " + enemyList.get(i).getHealth(), 10, 90 + i*25);
    }
    
    player.drawSprite(g);
    
    player.drawPlayerProjectile(g);
    for (int j = 0; j < healthPacks.size(); j++) {
      healthPacks.get(j).drawItem(g);
    }
    
    for (int i = 0; i < enemyList.size(); i++) {
      
      (enemyList.get(i)).drawEnemy(g);
      (enemyList.get(i)).drawEnemyProjectile(g);
      
    }
  }
  
  // Method to change the states of the game and intialize the things needed.
  public void changeState(int a) {
    
    gameState = a;
    
    if (gameState == 0) {
      menuInit();
    } else if (gameState == 1) {
      gameInit();
    } else if (gameState == 2){
      endInit();
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
      
      if (e.getKeyChar() == 'e') {
        healthPacks.add(new HealthPack(500, 500, 50, 50, "HP"));
        System.out.print("E Is Typed");
      }
      
      if (e.getKeyChar() == 'p') {
        System.exit(0);
      }
      
      if (e.getKeyChar() == 'l') {
        player.setHealth(player.getHealth() - 50);
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
      
      if (gameState == 0) {
        changeState(1);
        System.out.print("changed");
      } else if (gameState == 1) {
        player.shoot(e.getX() - 7, e.getY() - 30); // Mouse offset cause it is very clown lol
      } else if (gameState == 2){
        changeState(0);
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