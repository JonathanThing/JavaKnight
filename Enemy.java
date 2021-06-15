/**
 * [Enemy.java]
 * Description: The class for enemies
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 28, 2021
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

abstract class Enemy extends Character {
  
  //player that enemy is tracking
  private Player player;
  
  /**
   * draw
   * method to draw the enemy
   * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
   */
  public void draw(Graphics g, double offSetX, double offSetY) {
    
    g.setColor(Color.RED);
    g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null); //draws the enemy
  }
  
  /**
   * getHit
   * method to detect if a player projectile hits the enemy and reduces health as well if hit
   * @param player, the player
   */
  public void getHit(Player player) {
    
    for (int i = 0; i < (player.getProjectilesList()).size(); i++) { //loops through the arrayList of player projectiles
      
      if (player.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) {
        
        this.setHealth(this.getHealth() - player.getWeapon().getDamage()); //decrease health of enemy if hit
        player.getProjectilesList().remove(i); //remove bullet that hits 
      }
      
    }
    
  }
  
  /**
   * attack
   * abstract method to attack the player
   * @param player, the player, sprite, the sprite of the enemy
   */
  public abstract void attack(Player player,BufferedImage sprite);
  
  
//constructor 
  Enemy(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
    super(x, y, width, height, name, sprite, health, weapon);
    
  }
  
}