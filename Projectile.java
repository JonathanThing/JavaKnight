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
  
  public void draw(Graphics g, double offSetX, double offSetY) {
	  g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
	}
    
  Projectile(double x, double y, int width, int height,String name, BufferedImage sprite, int health, double changeX, double changeY){
  //Projectile(int x, int y, int width, int height, BufferedImage sprite, String name, int changeX, int changeY){
    super(x, y, width, height, name, sprite, health);
    //super(x, y, width,  height, sprite, name);
    this.changeX = changeX;
    this.changeY = changeY;
  }
  
}