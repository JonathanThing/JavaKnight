/**
 * [Projectile.java]
 * Description: The class for projectiles 
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 7, 2021
 */

import java.awt.image.BufferedImage; 
import java.awt.Graphics;

class Projectile extends Entity{
  
	// Variables
  private double changeX;
  private double changeY;
  
  /**
   * getChangeX
   * method to get the ChangeX of the bullet
   * @return this.changeX, as a double
   */
  public double getChangeX(){
    return this.changeX;
  }
  
  /**
   * getChangeY
   * method to get the ChangeY of the bullet
   * @return this.changeY, as a double
   */
  public double getChangeY(){
    return this.changeY;
  }
  
  /**
   * draw
   * method to draw the player
   * @param g the graphics object, offSetX how much the x is off by, offSetY how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
	  g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
	}
    
  
  //Constructor
  Projectile(double x, double y, int width, int height,String name, BufferedImage sprite, int health, double changeX, double changeY){
    super(x, y, width, height, name, sprite, health);
    this.changeX = changeX;
    this.changeY = changeY;
  }
  
}