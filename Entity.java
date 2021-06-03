import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Entity extends GameObject implements Moveable{

  //Health 
  private double health;

  //get health
  public double getHealth(){
    return this.health;
  }
  
  public void setHealth(double health){
	   this.health = health;
  }
  
  //Move Left
  public void moveLeft(double n) {
    this.setX(this.getX()-n);
  }

  //Move Right
  public void moveRight(double n) {
    this.setX(this.getX()+n);
  }
  
  //Move Up
  public void moveUp(double n) {
    this.setY(this.getY()-n);
  }
  
  //Move Down
  public void moveDown(double n) {
    this.setY(this.getY()+n);
  }
  
  //Constructor
  Entity(double x, double y, int width, int height, String name, double health){
  //Entity(int x, int y, int width, int height, BufferedImage sprite, String name, double health){
    super(x, y, width,  height, name);
    //super(x, y, width,  height, sprite, name);
    this.health = health;
  }

}