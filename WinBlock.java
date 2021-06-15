/**
 * [WinBlock.java]
 * Description: The class for the block you need to touch to win
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 10, 2021
 */
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class WinBlock extends Environment {
  
  //constructor 
  WinBlock(double x, double y, String name, BufferedImage sprite) {
  super(x, y, 32, 32, name, sprite);
    
  }
  
  /**
   * draw
   * method to draw the healthpack
   * @param g the graphics object, offSetX how much the x is off by, offSetY how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    g.setColor(Color.YELLOW);
    g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight()); //draw a yellow rectangle for the winblock
  }
  
  /**
   * enemyHit
   * method to detect if an enemy projectile hits it
   * @param a, the enemy that can possibly hit the wall
   */
  public void enemyHit(Enemy a) {

  }
  
  /**
   * playerHit
   * method to detect if a player projectile hits it
   * @param p, the player
   */
  public void playerHit(Player p) {

  }
  
    /**
   * playerWin
   * @return true if hit, false if not
   * @param p, the player
   */
  public boolean playerWin(Player p){
    if (p.getCollision().intersects(this.getHitbox())) {
      return true; 
    }
    
    return false;
  }
  
}