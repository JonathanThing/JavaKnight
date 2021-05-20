/**
 * This template can be used as reference or a starting point
 * for your final summative project
 * @author Mangat
 **/

//Graphics &GUI imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;

class GameFrameOOP extends JFrame { 
  
  //Game Screen
  static GameAreaPanelOOP gamePanel;
  
  GameFrameOOP() { 
    super("My Game");  
    
    // Set the frame to full screen 
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //resolution 
    this.setSize(1280,720);
    // this.setUndecorated(true);  //Set to true to remove title bar
    //frame.setResizable(false);
    
    //Set up the game panel (where we put our graphics)
    gamePanel = new GameAreaPanelOOP();
    this.add(new GameAreaPanelOOP());
    
    this.setFocusable(false);  //we will focus on the JPanel
    this.setVisible(true);    
  } 

}




