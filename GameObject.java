import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

abstract class GameObject{

  //coords
  private int x;
  private int y;

  //attributes
  private int width;
  private int height;
  private BufferedImage sprite;
  private String name;
  
  //get x
  public int getX(){
    return this.x;
  }
  
  //get y
  public int getY(){
    return this.y;
  }
  
  //set x
  public void setX(int x){
    this.x = x;
  }
  
  //set y
  public void setY(int y){
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
  
  //make rectangle
  Rectangle collisionRect = new Rectangle(getX(), getY(), getX() + getWidth(), getY()+getHeight());
  
  //constructor
  GameObject(int x, int y, int width, int height, String name){
  //GameObject(int x, int y, int width, int height, BufferedImage sprite, String name){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    //this.sprite = sprite;
    this.name = name;
  }
  
}

