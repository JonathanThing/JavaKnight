import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Character extends Entity{
  
  //Weapon
  private Weapon weapon;
  
  Character(double x, double y, int width, int height, String name, double health, Weapon weapon){
    //Character(int x, int y, int width, int height, BufferedImage sprite, String name, double health, String weapon){
    super(x, y, width, height, name, health);
    this.weapon = weapon;
    //super(x, y, width, height, sprite, name, health);
    
  }
  
  public Weapon getWeapon() {
    return this.weapon;
  }
  
  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }
  
}