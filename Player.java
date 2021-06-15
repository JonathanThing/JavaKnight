/**
 * [Player.java]
 * Description: The class for the player
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 28, 2021
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character {

 
 /**
  * laserBeam
  * method to draw the laserBeam
  * @param g, the graphics object, x, the x coordinate of where the laser ends, y, 
  * the y coordinate of where the laser ends, offSetX, how much the x is off by, offSetY, how much the y is off 
  */ 
 public void laserBeam(Graphics g, int x, int y, double offSetX, double offSetY) {

  g.setColor(Color.RED);
  g.drawLine((int) (this.getX() - offSetX), (int) (this.getY() - offSetY), (int) (x - offSetX),
    (int) (y - offSetY));
  g.setColor(Color.BLACK);
 }

 /**
  * draw
  * method to draw the player
  * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
  */
 public void draw(Graphics g, double offSetX, double offSetY) {

  g.drawImage(this.getSprite(), (int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), null);
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

  //divide the xChange and yChange by the hypotenuse to normalize the vector to guarentee that it moves at the same speed in any direction
  double xChange = ((xDifference / hyp) * 30); //calculate how much the x coord will change
  double yChange = ((yDifference / hyp) * 30); //calculate how much the x coord will change
 
  if (this.getWeapon().getName().equals("shotgun")) {//if the player is using the shotgun

   for (int i = 0; i < 8; i++) {
    this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(),
      this.getWeapon().getSize(), "Bullet",sprite, 20, xChange + Math.random() * (-16) + 8, yChange + Math.random() * (-16) + 8)); //Fires 8 projectiles with a random variation
   }
  } else if (this.getWeapon().getName().equals("smg")) { //if the player is using the smg
   this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet",sprite, 20, xChange + Math.random() * (-2) + 1, yChange + Math.random() * (-4) + 1)); //Fire bullets that have a slight variation 

  } else { //Player using pistol
   this.getProjectilesList().add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet",sprite, 20, xChange, yChange));
  }
 }

 /**
  * moveProjectile
  * method to move the projectiles
  */
 public void moveProjectile() {

  for (int i = 0; i < this.getProjectilesList().size(); i++) {  //loops through arrayList of projectiles


   (getProjectilesList().get(i)).moveDown((getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
   (getProjectilesList().get(i)).moveRight((getProjectilesList().get(i)).getChangeX());  //moves the projectils on the x-axis


  }

 }

 /**
  * drawPlayerProjectile
  * method to draw the projectiles
  * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
  */
 public void drawPlayerProjectile(Graphics g, double offSetX, double offSetY) {
	 for (int i = 0; i < getProjectilesList().size(); i++) { //loop through arrayList
		   (getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
		  }
 }

 /**
  * removeProjectile
  * method to remove a certain projectile from the arrayList
  * @param i, the index of the projectile to be removed
  */
 public void removeProjectile(int i) {
  getProjectilesList().remove(i); //remove i from the arrayList
 }

 /**
  * movement
  * method to move the player
  * @param up, the boolean for whether or nor the player decides to move up, down, the boolean for whether or nor the player decides to move down,
  * left, the boolean for whether or nor the player decides to move left, right, the boolean for whether or nor the player decides to move right,
  * list, the arrayList for enemies, map, the array of the map for checking collision
  */
 public void movement(boolean up, boolean down, boolean left, boolean right, ArrayList<Enemy> list,
		   Environment[][] map) {

  //variable for how much x and y change
  double xMove = 0; 
  double yMove = 0;

  if (up) {
   yMove += 1; //add 1 to the y change if up is true
  }

  if (down) {
   yMove -= 1; //subtract 1 to the y change if down is true
  }

  if (left) {
   xMove -= 1; //subtract 1 to the x change if left is true
  }

  if (right) {
   xMove += 1; //add 1 to the x change if right is true
  }

  double hyp = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2)); //calculate the hyptoenous to use to normalize the movement vector 

  if (hyp != 0) { //if the player moves

   this.moveRight((xMove / hyp) * 5); //move right

   if (collision(list, map)) { //if player hits the wall

    this.moveLeft((xMove / hyp) * 5); //move back to original position
   }

   this.moveUp((yMove / hyp) * 5); //move up

   if (collision(list, map)) { //if player hits the wall

    this.moveDown((yMove / hyp) * 5); //move back to original position

   }

  }

 }

  /**
  * collision
  * @param a, arrayList of enemies, b, array for map
  * @return true if player collides with something, false if it does not
  */

 public boolean collision(ArrayList<Enemy> a, Environment[][] b) {

	  for (int i = 0; i < a.size(); i++) { //loop through enemy arrayList
	   if (this.getCollision().intersects(a.get(i).getCollision())) {
	    return true; //return true if player collides with an enemy
	   }
	  }

	  for (int i = 0; i < b.length; i++) { //loop through map
	   for (int j = 0; j < b[0].length; j++) {
	    if ((b[i][j] != null) && (!b[i][j].getName().equals("winblock")) && (this.getCollision().intersects(b[i][j].getCollision()))) {
	     return true; //return true if player collides with anything
	    }
	   }
	  }
	  return false; //return false if player doesn't collide with anything at all
	 }

 /**
  * wasHit
  * @param a, the enemy that may hit the player
  * @return true if player gets hit, false if it does not
  */
 public boolean wasHit(Enemy a) {

  for (int i = 0; i < (a.getProjectilesList()).size(); i++) { //loop through projectiles of enemies 

   if (a.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) {

    a.getProjectilesList().remove(i); //remove the projectile that hits you if you get hit

    return true; //return true if player gets hit
   }

  }

  return false; //return false if not hit
 }
 
 /**
  * getAggro
  * @return the range the player was to be within for enemies to aggro
  */
 public Rectangle getAggro() {
  return new Rectangle((int)this.getX() - (32*50)/2, (int) this.getY() - (32*35)/2, 32*50, 32*35);
 }

//constructor
Player(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
super(x, y, width, height, name, sprite, health, weapon);

}

}