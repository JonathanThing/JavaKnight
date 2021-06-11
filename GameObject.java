import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
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
  
  //get x
  public double getX(){
    return this.x;
  }
  
  //get y
  public double getY(){
    return this.y;
  }
  
  //set x
  public void setX(double x){
    this.x = x;
  }
  
  //set y
  public void setY(double y){
    this.y = y;
  }

  //get width
  public int getWidth(){
    return this.width;
  }

  //get height
  public int getHeight(){
    return this.height;
  }
  
  //get name
  public String getName(){
    return this.name;
  }
  
  //get sprite
  public BufferedImage getSprite(){
    return this.sprite;
  }
  
  public Rectangle getCollision() {
	  return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }
  
  public Rectangle getHitbox() {
	  return new Rectangle((int)this.x - this.width/2, (int) this.y - this.height/2, this.width, this.height);
  }

  public abstract void draw (Graphics g,  double offSetX, double offSetY);
  

  //constructor
  GameObject(double x, double y, int width, int height, String name, BufferedImage sprite){
  //GameObject(int x, int y, int width, int height, BufferedImage sprite, String name){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.sprite = sprite;
    this.name = name;
  }
  
}