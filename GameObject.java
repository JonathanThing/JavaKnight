/**
 * [GameObject.java]
 * Description: The base class for any object in the game 
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 27, 2021
 */

import java.awt.image.BufferedImage; 
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class GameObject{

  //coords
  private double x;
  private double y;

  //attributes
  private int width;
  private int height;
  private BufferedImage sprite;
  private String name;
  
  /**
   * getX
   * @return this.x, the x-value of the object
   */
  public double getX(){
    return this.x;
  }
  
  /**
   * getY
   * @return this.y, the y-value of the object
   */
  public double getY(){
    return this.y;
  }
  
  /**
   * setX
   * method to set the x-value of the object
   * @param x, the value that the x-value will be set to
   */
  public void setX(double x){
    this.x = x;
  }
  
  /**
   * setY
   * method to set the y-valur of the object
   * @param y, the value that the y-value will be set to
   */
  public void setY(double y){
    this.y = y;
  }
  
  /**
   * getWidth
   * @return this.width, the width of the object
   */
  public int getWidth(){
    return this.width;
  }

  /**
   * getHeight
   * @return this.height, the height of the object
   */
  public int getHeight(){
    return this.height;
  }
  
  /**
   * getName
   * @return this.name, the name or type of the object
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * getSprite
   * @return this.sprite, the sprite for the object
   */
  public BufferedImage getSprite(){
    return this.sprite;
  }
  
  /**
   * getCollision
   * @return a rectangle that serves as the collision box for objects
   */
  public Rectangle getCollision() {
   return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }
  
  /**
   * getHitbox
   * @return a rectangle that serves as the hitbox box for entities 
   */
  public Rectangle getHitbox() {
   return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }

  /**
   * draw
   * abstract method to draw the object
   */
  public abstract void draw (Graphics g,  double offSetX, double offSetY);
  

  //constructor
  GameObject(double x, double y, int width, int height, String name, BufferedImage sprite){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.sprite = sprite;
    this.name = name;
  }
  
}