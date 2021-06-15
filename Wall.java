/**
 * [Wall.java]
 * Description: The class for walls
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 9, 2021
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Wall extends Environment {
  
//constructor
  Wall(double x, double y, String name, BufferedImage sprite) {
    super(x, y, 32, 32, name, sprite);
    
  }
  
  /**
   * draw
   * method to draw the wall
   * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null); //draw the wall
    
  }
  
  /**
   * enemyHit
   * method to detect if an enemy projectile hits it
   * @param a, the enemy that can possibly hit the wall
   */
  public void enemyHit(Enemy a) {
    
    for (int i = 0; i < (a.getProjectilesList()).size(); i++) { //loop through enemy projectiles
      
      if (a.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) {
        
        a.getProjectilesList().remove(i); //remove the projectile from the arrayList if the wall gets hit
        
      }
      
    }
  }
  
  /**
   * playerHit
   * method to detect if a player projectile hits it
   * @param p, the player
   */
  public void playerHit(Player p) {
    
    for (int i = 0; i < (p.getProjectilesList()).size(); i++) { //loop through player projectiles
      
      if (p.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) { 
        
        p.getProjectilesList().remove(i); //remove projectile if it hits the wall
        
      }
      
    }
  }
  
  /**
   * playerWin
   * method to detect if player touches the win block (win be overriden in WinBlock)
   * @param p, the player
   */
  public boolean playerWin(Player p){
    return true;
  }
}