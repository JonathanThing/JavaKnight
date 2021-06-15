
/**
 * [Zombie.java]
 * Description: The class for zombies
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 5, 2021
 */ 

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Zombie extends Enemy {

 private long lastHit;
 private Player player;
 
 /**
  * move
  * method to move the zombie by tracking the player
  * @param player, the player, b, the array for the map, a, the arraylist of enemies
  */
 public void move(Player player, Environment[][] b,ArrayList<Enemy> a) {
  int playerX = (int) (player.getX()); //get the x coord of the player
  int playerY = (int) (player.getY()); //get the y coord of the player

  double xDifference = getX() - player.getX(); //calculate difference in x 
  double yDifference = getY() - player.getY(); //calculate difference in y

  double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)); //calculate hypotenuse to help normalize the vector
  
  double xChange = ((xDifference / hyp) * 3); //set the change for x for zombie movement
  double yChange = ((yDifference / hyp) * 3); //set the change for y for zombie movement

  this.moveLeft(xChange); //move left

  if (collision(player, b, a)) { 

   this.moveRight(xChange); //move back to previous position if zombie hits something
  }

  this.moveUp(yChange); //move up

  if (collision(player, b, a)) {

   this.moveDown(yChange); //move back to previous position if zombie hits something

  }
 
   /**
  * collision
  * @param player, the player, a, arrayList of enemies, b, array for map
  * @return true if zombie collides with something, false if it does not
  */
 public boolean collision(Player player, Environment[][] b,ArrayList<Enemy> a) {

  for (int i = 0; i < a.size(); i++) { //loops through array of enemys
   if ((this != a.get(i)) && this.getCollision().intersects(a.get(i).getCollision())) {
    return true; //return true if zombie hits one of the enemies 
   }
  }
  
  if (this.getCollision().intersects(player.getCollision())) { //check if zombie hits player
   if (System.nanoTime()-lastHit >= 1e9) { //start cooldown for attakcs
    player.setHealth(player.getHealth()-10); //take away 10 hp from the player
    lastHit = System.nanoTime(); //get time of hit
   }
   return true; //return true
  }

  for (int i = 0; i < b.length; i++) { //loop through array for map
   for (int j = 0; j < b[0].length; j++) {
    if ((b[i][j] != null) && (this.getCollision().intersects(b[i][j].getCollision()))) {
     return true; //return true if the zombie hits something
    }
   }
  }
  return false; //return false if it doesn't hit anyhting
 }

 //constructor
 Zombie(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
  super(x, y, width, height, name, sprite, health, weapon);
  lastHit = System.nanoTime();
 }

 public void attack(Player player, BufferedImage sprite) {


  
 }


}