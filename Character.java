
/**
 * [Character.java]
 * Description: The class for any entities that have a weapon
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 27, 2021
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Character extends Entity {

  //arrayList of projectiles that the character shoots
 private ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();

 // Weapon
 private Weapon weapon;

 //Constructor 
 Character(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
  super(x, y, width, height, name, sprite, health);
  this.weapon = weapon;

 }

 /**
  * getWeapon
  * @return this.weapon, the type of weapon that is being used
  */
 public Weapon getWeapon() {
  return this.weapon;
 }
 
 /**
  * setWeapon
  * method to set the type of weapon that the characters use
  * @param weapon, the type of weapon that is being set
  */
 public void setWeapon(Weapon weapon) {
  this.weapon = weapon;
 }

  /**
  * getProjectilesList
  * @return projectilesList, the arrayList of projectiles
  */
 public ArrayList<Projectile> getProjectilesList() {
  return projectilesList;
 }
}