import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character{
  
  static ArrayList<Projectile> playerProjectiles = new ArrayList<Projectile>();

  //sprite
  private BufferedImage sprite;
  
  //ammo
  private double ammo;

  //get ammo
  public double getAmmo(){
    return this.ammo;
  }
  
  //load sprite
  public void loadSprite() { 
    try {
      sprite = ImageIO.read(new File("src/images/sprite.png"));
      
    } catch(Exception e) { 
      System.out.println("error loading sprite");};
  }
  
  //draw
  public void drawSprite(Graphics g) { 
    g.drawImage(sprite, (int) getX(), (int) getY(),null);
  }

  //create projectiles
  public void shoot(double targetX, double targetY){
    double xDifference = targetX - getX();
    double yDifference = targetY - getY();
    
    double xChange = Math.round(xDifference/10);
    double yChange = Math.round(yDifference/10);
    
    playerProjectiles.add(new Projectile(getX(), getY(), 50, 50, "Bullet", 20, xChange, yChange));
  }

  //move projectiles    
  public void moveProjectile(){
    
    for (int i = 0; i < playerProjectiles.size(); i++){
      
      (playerProjectiles.get(i)).moveDown((playerProjectiles.get(i)).getChangeY());
      (playerProjectiles.get(i)).moveRight((playerProjectiles.get(i)).getChangeX());
      
    }
    
  }
  
  //draw projectiles
  public void drawPlayerProjectile(Graphics g){
    for (int i = 0; i < playerProjectiles.size(); i++){
      (playerProjectiles.get(i)).drawProjectile(g);
    }
  }
  
  //constructor
  Player(int x, int y, int width, int height, String name, double health, String weapon, double ammo){
  //Player(int x, int y, int width, int height, BufferedImage sprite, String name, double health, String weapon, double ammo){
    super(x, y, width,height, name, health, weapon);
    //super(x, y, width,height, sprite, name, health, weapon);
    this.ammo = ammo;
    loadSprite();
  }

}
