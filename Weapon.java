import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Weapon extends Items{

  //damage this weapon deals
  private double damage;

  //rate at which this weapon can be used
  private double fireRate;

  //range of the weapon
  private double range;

  //type of ammo
  String ammoType;
  
  public double getDamage(){
    return this.damage;
  }
  
  public double getFireRate(){
    return this.fireRate;
  }
  
  public double getRange(){
    return this.range;
  }
  
  public String getAmmoType(){
    return this.ammoType;
  }
  
  public void drawWeapon(Graphics g){
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
  }

  //constructor 
  Weapon(int x, int y, int width, int height,String name, double damage, double fireRate, double range, String ammoType){
    super(x, y, width,  height, name);
    this.damage = damage;
    this.fireRate = fireRate;
    this.range = range;
    this.ammoType = ammoType;
  }

}