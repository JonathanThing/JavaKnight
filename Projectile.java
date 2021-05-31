import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Projectile extends Entity{
  
  private double changeX;
  private double changeY;
  
  public double getChangeX(){
    return this.changeX;
  }
  
  public double getChangeY(){
    return this.changeY;
  }
  
  public void drawProjectile(Graphics g){
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
  }
    
  Projectile(double x, double y, int width, int height,String name, int health, double changeX, double changeY){
  //Projectile(int x, int y, int width, int height, BufferedImage sprite, String name, int changeX, int changeY){
    super(x, y, width, height, name, health);
    //super(x, y, width,  height, sprite, name);
    this.changeX = changeX;
    this.changeY = changeY;
  }
  
}