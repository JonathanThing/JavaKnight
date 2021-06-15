/**
 * [Enviroment.java]
 * Description: The class for any environmental objects  on the map
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 7, 2021
 */

import java.awt.image.BufferedImage;

public abstract class Environment extends GameObject {

 Environment(double x, double y, int width, int height, String name, BufferedImage sprite) {
  super(x, y, width, height, name, sprite);

 }

 /**
  * enemyHit
  * abstract method to detect if an enemy projectile hits it
  */
  public abstract void enemyHit(Enemy a);
  
  /**
   * playerHit
   * abstract method to detect if a player projectile hits it
   */
  public abstract void playerHit(Player p);
  
  /**
   * playerWin
   * abstract method to detect if a player touches it
   */
  public abstract boolean playerWin(Player p);
 
}
