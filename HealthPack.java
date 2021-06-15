/**
 * [HealthPack.java]
 * Description: The class for healthpacks
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 5, 2021
 */

import java.awt.image.BufferedImage;
import java.awt.Graphics;

class HealthPack extends GameObject {

 HealthPack(int x, int y, int width, int height, String name, BufferedImage sprite) {
  super(x, y, width, height, name, sprite);
 }

 /**
  * draw
  * method to draw the healthpack
  * @param g the graphics object, offSetX how much the x is off by, offSetY how much the y is off by
  */
 public void draw(Graphics g, double offSetX, double offSetY) {

  g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);
 }

 /**
  * checkCollision
  * method to check for collision
  * @param p, the player object that the packs will intersect with
  * @return true if hit, false if not hit
  */
 public boolean checkCollision(Player p) {
  if (this.getCollision().intersects(p.getCollision())) {
   return true; //return true if player hits it
  }
  
  return false; //return false if not

 }

}