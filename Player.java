import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;

class Player { 
  private int x, y; 
  private int xdirection,ydirection;
  
  private BufferedImage sprite;
  
  
   public Player() { 
     loadSprite();
     this.x=500;
     this.y=768/2;
     this.xdirection=0;
     this.ydirection=0;
   }
   
   public void loadSprite() { 
     try {
     sprite = ImageIO.read(new File("sprite.png"));

     } catch(Exception e) { 
       System.out.println("error loading sprite");};
   }
 
   
   public void draw(Graphics g) { 
     g.drawImage(sprite,x,y,null);
   }
   
   public void update() { 
     this.x=this.x+this.xdirection;
     this.y=this.y+this.ydirection;
   }
   
   public void moveLeft(){
     xdirection=-1;
   }
   public void moveRight(){
     xdirection=1;
   }
   
   public void moveUp(){
     ydirection=-1;
   }
   
   public void moveDown(){
    ydirection=1;
   }
   
   
 }
   