
//Graphics &GUI imports
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

public class Game extends JFrame {
  
 int playerX = 50;
 int playerY = 50;
 
 Player player = new Player(playerX,playerY,100,50,"player",100,"sword",50);

 /****************** CLASS VARIABLES *******************/
 /** The variables can be accessed across all methods **/
 /******************************************************/

 static GameAreaPanel gamePanel;
 static Graphics g;
 static int gameState = 0; // 0 = Menu, 1 = Game
 

 /***************************************************************/
 /** GameFrame - Setups up the Window and Starts displaying it **/
 /***************************************************************/

 Game() {
  super("My Game");

  // Set the frame to full screen
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  // Set resolution 1280x780
  this.setSize(1280, 780);

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
  
  
  try {
   Thread.sleep(33); // 16 = 60fps, 33 = 30fps
  } catch (Exception exc) {

  } // delay

 }

 // Rendering
 public void menuRender(Graphics g) {

  Font font = new Font("Serif", Font.PLAIN, 50);
  g.setFont(font);
  g.drawString("Epic Menu\n Click mouse to play game", 10, 60);

 }

 public void gameRender(Graphics g) {
  player.drawSprite(g);
  player.moveProjectile();
  player.drawPlayerProjectile(g);
  
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
         //System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
        
    if (KeyEvent.getKeyText(e.getKeyCode()).equals("W")) {  
      player.moveUp(50);
    } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("S")) {  
      player.moveDown(50);
    } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {  
      player.moveLeft(50);
    } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  
      player.moveRight(50);
    } 
        /*
         if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  //If 'D' is pressed
           
         }
         */  
           
           
           
//         } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
//           System.out.println("YIKES ESCAPE KEY!"); //close frame & quit
//           System.exit(0);
//         } 
       }   
       
       public void keyTyped(KeyEvent e) {  
       }
       public void keyReleased(KeyEvent e) {
       }
 }

 /****** Key Listener *********************************/

 /**************************** Mouse Listener ************************/
 /** This section is where mouse input is handled **/
 /** You may have to add code to respond to mouse clicks **/
 /********************************************************************/
 class MyMouseListener implements MouseListener {

  public void mouseClicked(MouseEvent e) {
   System.out.println("Mouse Clicked");
   System.out.println("X:" + e.getX() + " y:" + e.getY());

   if (gameState == 0) {
    changeState(1);
   } else if (gameState == 1){
     player.shoot(e.getX(), e.getY());
   }

  }

  public void mousePressed(MouseEvent e) {
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
