 /**
 * [Skeleton.java]
 * Description: The class for skeletons
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, June 5, 2021
 */  

import java.awt.image.BufferedImage;
import java.awt.Graphics;


class Skeleton extends Enemy {

	  //variable
	  private long lastShot;
	
	  /**
   * shoot
   * method to allow the skeleton to shoot
   * @param player, the player, sprite, the sprite of the projectile 
   */
  public void shoot(Player player) {
    
    double xDifference = getX() - player.getX(); //calculate difference in x 
    double yDifference = getY() - player.getY(); //calculate difference in y
    
    double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)); //calculate hypotenuse to normalize the vector
    
    double xChange = ((xDifference / hyp)*7); //set the change in x for projectiles
    double yChange = ((yDifference / hyp)*7); //set the change in y for projectiles
    
    
    if (this.getName().equals("eliteSkeleton")) { //check if the skeleton is an elite skeleton (boss mob)
      for (int i = 0 ; i < 4; i++) {
        this.getProjectilesList().add(new Projectile(getX(), getY(), 20, 20, "Bullet", getSprite(), 20, xChange+Math.random()*-2 + 1, yChange+Math.random()*-2 + 1));
      } //shoot 4 bullets
    } else {
      this.getProjectilesList().add(new Projectile(getX(), getY(), 20, 20, "Bullet", getSprite(), 20, xChange, yChange)); //only shoot 1 if the skeleton is normal
      
    }
  }

  /**
   * moveProjectile
   * method that moves projectiles
   */
  public void moveProjectile() {
    
    for (int i = 0; i < this.getProjectilesList().size(); i++) { //loops through arrayList of projectiles
      
      (this.getProjectilesList().get(i)).moveUp((this.getProjectilesList().get(i)).getChangeY()); //moves the projectils on the y-axis
      (this.getProjectilesList().get(i)).moveLeft((this.getProjectilesList().get(i)).getChangeX()); //moves the projectils on the x-axis
      
    }
    
  }

  /**
   * drawEnemyProjectile
   * method to draw the projectiles
   * @param g, the graphics object, offSetX, how much the x is off by, offSetY, how much the y is off by
   */
  public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
    for (int i = 0; i < this.getProjectilesList().size(); i++) { //loop through arrayList
      (this.getProjectilesList().get(i)).draw(g, offSetX, offSetY); //draws the projectile
    }
  }
 
//constructor
 Skeleton(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
  super(x, y, width, height, name, sprite, health, weapon);
  lastShot = System.nanoTime();
 }

 /**
  * shoot
  * method to tells the skeleton when to shoot
  * @param player, the player, sprite, the sprite of the projectile 
  */
 public void attack(Player player) {
  
  if (System.nanoTime()-lastShot >= 1e9 * this.getWeapon().getFireRate()) { //Check if enough time has past for the weapon to be ready to fire again
   this.shoot(player); 
   lastShot = System.nanoTime();
  }
 }

}