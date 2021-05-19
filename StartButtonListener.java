 
//This is a class that is used to detect a button press

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class StartButtonListener implements ActionListener {  
   JFrame parentFrame;
   StartButtonListener(JFrame parent) { 
     parentFrame = parent;
   }
    public void actionPerformed(ActionEvent event)  {  
      System.out.println("Starting new Game");
      parentFrame.dispose();
      new GameFrameOOP(); //create a new frame after removing the current one
    }
 }