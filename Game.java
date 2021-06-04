
//Graphics &GUI imports
import java.awt.image.BufferStrategy;
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

//Import Math
import java.lang.Math;

public class Game extends JFrame {

 static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
 static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>();

 static double playerX = 50;
 static double playerY = 50;
 
 static int thingX, thingY;

 static Player player = new Player(playerX, playerY, 50, 50, "player", 100, "sword", 50);

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
  this.setSize(1280, 780);

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

  enemyList.add(new Enemy(200, 400, 50, 50, "Enemy", 100, "idk", player));

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

   }
  }
 }

 /****** End of paintComponent *********************************/

 public void menuInit() {

 }

 public void gameInit() {

 }

 // Updating
 public void menuTick() {

 }

 public void gameTick() {
  
  double xMove = 0;
  double yMove = 0;

  if (up) {
   yMove += 1;
  }

  if (down) {
   yMove -= 1;
  }

  if (left) {
   xMove -= 1;
  }

  if (right) {
   xMove += 1;
  }

  for(int i = 0; i < healthPacks.size(); i++){
    if ((healthPacks.get(i).getX() >= player.getX()-player.getWidth()) && (healthPacks.get(i).getX() <= player.getX()+player.getWidth()) && 
        (healthPacks.get(i).getY() >= player.getY()-player.getHeight()) && (healthPacks.get(i).getY() <= player.getY()+player.getHeight())){
      healthPacks.remove(i);
      player.setHealth(player.getHealth() + 100);
      this.repaint();
    }
  }   
  
  double hype = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2));
  
  if (hype != 0) {
  
   player.moveRight(xMove/hype*10);
  
   player.moveUp(yMove/hype*10);
  
  }
  
  try {
   Thread.sleep(16); // 16 = 60fps, 33 = 30fps
  } catch (Exception exc) {

  } // delay

 }

 // Rendering
 public void menuRender(Graphics g) {

  Font title = new Font("Serif", Font.PLAIN, 50);
  g.setFont(title);
  g.drawString("Epic Menu\n Click mouse to play game", 10, 60);

 }

 public int getRandomNumber(int min, int max) {
  return (int) ((Math.random() * (max - min)) + min);
 }

 public void gameRender(Graphics g) {
  
  Font health = new Font("Serif", Font.PLAIN, 20);
  g.setFont(health);
  g.drawString("Health: " + player.getHealth(), 10, 30);
  
  for (int i = 0; i < enemyList.size(); i++) {
    g.drawString("Eny Health: " + enemyList.get(i).getHealth(), 10, 90);
  }
  
  player.drawSprite(g);
  player.moveProjectile();
  player.drawPlayerProjectile(g);

  
  for (int i = 0; i < enemyList.size(); i++) {

   (enemyList.get(i)).drawEnemy(g);
   (enemyList.get(i)).moveProjectile();
   (enemyList.get(i)).drawEnemyProjectile(g);
   (enemyList.get(i)).changeHealth();
   //(enemyList.get(i)).getHit(player);
   
   if((enemyList.get(i)).getHit(player) == true){
     player.removeProjectile(i);
   }
   
   if((enemyList.get(i)).getHealth() == 0){
     enemyList.remove(i);
   }
   
  for(int j = 0; j < healthPacks.size(); j++){
    healthPacks.get(j).drawItem(g);
  }

   if (getRandomNumber(1, 50) == 1) {
    (enemyList.get(i)).shoot(player);
   }

  }

 }

 // Method to change the states of the game and intialize the things needed.
 public void changeState(int a) {

  gameState = a;

  if (gameState == 0) {
   menuInit();
  } else if (gameState == 1) {
   gameInit();
  }

 }

 /***************************** Key Listener ************************/
 /** This section is where keyboard input is handled **/
 /** You will add code to respond to key presses **/
 /*******************************************************************/
 class MyKeyListener implements KeyListener {

  public void keyPressed(KeyEvent e) {
   // System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));

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
   /*
    * if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) { //If 'D' is pressed
    * 
    * }
    */

//         } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
//           System.out.println("YIKES ESCAPE KEY!"); //close frame & quit
//           System.exit(0);
//         } 
  }

  public void keyTyped(KeyEvent e) {
    
   if(e.getKeyChar() == 'e'){
     healthPacks.add(new HealthPack(500, 500, 50, 50, "HP"));
     System.out.print("E Is Typed");
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

   
   System.out.println("Mouse Clicked");
   System.out.println("X:" + e.getX() + " y:" + e.getY());

   if (gameState == 0) {
    changeState(1);
   } else if (gameState == 1) {
    player.shoot(e.getX() - 7, e.getY()- 30);
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