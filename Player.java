/**
 * [Player.java]
 * Description: The class for the player
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 28, 2021
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character {

 // sprite
 private BufferedImage sprite;

 /**
  * laserBeam
  * method to draw the laserBeam
  * @param g, the graphics object, x, the x coordinate of where the laser ends, y, 
  * the y coordinate of where the laser ends, offSetX, how much the x is off by, offSetY, how much the y is off 
  */ 
 public void laserBeam(Graphics g, int x, int y, double offSetX, double offSetY) {
   
  g.setColor(Color.RED); //set color to red
  g.drawLine((int) (this.getX() - offSetX), (int) (this.getY() - offSetY), (int) (x - offSetX),
    (int) (y - offSetY)); //draws the laser beam in red
  g.setColor(Color.BLACK); //set color back to black
 }

 /**
  * draw
  * method to draw the player
  * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
  */
 public void draw(Graphics g, double offSetX, double offSetY) {
  g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);//draws the player
 }

 /**
  * shoot
  * method to allow the player to shoot
  * @param targetX, the x coordinate of the target, targetY, the y coordinate of the tagret, sprite, the sprite for the projectile 
  */
 public void shoot(double targetX, double targetY, BufferedImage sprite) {

  double xDifference = targetX - this.getX(); //calculate the difference in x
  double yDifference = targetY - this.getY(); //calculate the difference in y

  double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)); //calculate hypotenuse to normalize the vector

  double xChange = ((xDifference / hyp) * 30); //calculate how much the x coord will change
  double yChange = ((yDifference / hyp) * 30); //calculate how much the y coord will change

  if (this.getWeapon().getName().equals("shotgun")) { //if the player is using the shotgun
   
   for (int i = 0; i < 8; i++) {
    this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(),
      this.getWeapon().getSize(), "Bullet",sprite, 20, xChange + Math.random() * (-16) + 8, yChange + Math.random() * (-16) + 8)); //add projectiles to arrayList
   }
  } else if (this.getWeapon().getName().equals("smg")) { //if the player is using the smg
   this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet",sprite, 20, xChange + Math.random() * (-2) + 1, yChange + Math.random() * (-4) + 1)); //add projectiles to arrayList

  } else { //if the player is using the pistol
   this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet",sprite, 20, xChange, yChange)); //add projectiles to arrayList
  }
 }

 /**
  * moveProjectile
  * method to move the projectiles
  */
 public void moveProjectile() {

  for (int i = 0; i < this.getProjectilesList().size(); i++) { //loops through arrayList of projectiles

   (getProjectilesList().get(i)).moveDown((getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
   (getProjectilesList().get(i)).moveRight((getProjectilesList().get(i)).getChangeX()); //moves the projectils on the x-axis
 }

}