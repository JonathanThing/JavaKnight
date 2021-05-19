/** 
 * this template can be used for a start menu
 * for your final project
**/

//Imports
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;

class StartingFrameOOP extends JFrame { 

  JFrame thisFrame;
  
  //Constructor - this runs first
  StartingFrameOOP() { 
    super("Start Screen");
    this.thisFrame = this; //lol  
    
    //configure the window
    this.setSize(400,700);    
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setResizable (false);
    
    //Create a Panel for stuff
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    
    //Create a JButton for the centerPanel
    JButton startButton = new JButton("START");
    startButton.addActionListener(new StartButtonListener(this));
    
     //Create a JButton for the centerPanel
    JLabel startLabel = new JLabel("<HTML><H1>Welcome to a fun Game!</H1></HTML>",JLabel.CENTER);
    
    //Add all panels to the mainPanel according to border layout
    mainPanel.add(startButton,BorderLayout.SOUTH);
    mainPanel.add(startLabel,BorderLayout.CENTER);
    
    //add the main panel to the frame
    this.add(mainPanel);
   
    this.setVisible(true);
    
    this.requestFocusInWindow();
  }
  

  //Main method starts this application
  public static void main(String[] args) { 
    new StartingFrameOOP();
  }
  
}