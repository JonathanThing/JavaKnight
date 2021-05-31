import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Projectile extends Entity{
  
  private int changeX;
  private int changeY;
  
  public int getChangeX(){
    return this.changeX;
  }
  
  public int getChangeY(){
    return this.changeY;
  }
  
  public void drawProjectile(Graphics g){
    g.fillRect(getX(),getY(), getWidth(), getHeight());
  }
    
  Projectile(int x, int y, int width, int height,String name, int health, int changeX, int changeY){
  //Projectile(int x, int y, int width, int height, BufferedImage sprite, String name, int changeX, int changeY){
    super(x, y, width, height, name, health);
    //super(x, y, width,  height, sprite, name);
    this.changeX = changeX;
    this.changeY = changeY;
  }
  
}