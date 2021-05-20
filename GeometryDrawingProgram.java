import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Scanner;

class GeometryDrawingProgram {
    static JFrame frame;
    
    public static void main(String[] args) {  
        GeometryScreen gs = new GeometryScreen();
        Scanner input = new Scanner(System.in);
        //Create a menu here to allow user to add new shapes to the scene
        //Adding a shape means adding an object to an arraylist of objects that contain
        //all items displayed on the screen

        do {
        //menu options: List Shapes, Add shape, Remove shape, Translate Shape, Translate Drawing, Quit.
            frame.repaint(); //update the screen
        }while(true);
    
    }
  
  //This is an inner class - it has access to the static variables defined above
    public static class GeometryScreen {

        //constructor - do no modify
        GeometryScreen() {
        frame = new JFrame("Geometry Drawing Program 1.0");
        
        //Create a new "custom" panel for graphics based on the inner class below
        JPanel graphicsPanel = new GraphicsPanel();
        
        //Add the panel and the frame to the window
        frame.getContentPane().add(graphicsPanel);
        
        // Set the frame to full screen 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520,540);
        frame.setUndecorated(false);  //Set to true to remove title bar
        frame.setVisible(true);
        
     } 
    
//This is an inner class which contains all the details about drawing to screen.
    public static class GraphicsPanel extends JPanel {
      

     //The method that draws to the screen - you will need to add.modify code here
        public void paintComponent(Graphics g) {          
          setDoubleBuffered(true);   
          g.setColor(Color.BLACK);
          //draw the X/Y Axis
          g.drawLine(250, 0, 250, 500);
          g.drawLine(0, 250, 500, 250);
          
          //Below is a demonstration of how to use graphics commands to draw on to the screen
          //Check the Java Graphics class
          //Note - These coordinates are not based on the x/y axis
                
          g.drawOval(50,100, 25,50);        
          Color myCol = new Color(200,120,50); // You can creat your only colors based on RGB
          g.setColor(myCol);
          g.drawRect(300,200,50,100);
          
          //You will need to draw each of the Shapes contained in your Arraylist here
          //This screen will update everytime the menu loop completes

      }
    }
  }
}