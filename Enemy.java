import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Enemy extends Character {

 public ArrayList<Projectile> enemyProjectiles;

 private Player player;
 
 public void draw(Graphics g, double offSetX, double offSetY) {

  g.setColor(Color.RED);
  g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
 }

 public boolean getHit(Player player) {

  for (int i = 0; i < (player.playerProjectiles).size(); i++) {

   if (player.playerProjectiles.get(i).getHitbox().intersects(this.getHitbox())) {

    player.playerProjectiles.remove(i);

    return true;
   }

  }

  return false;
 }


 public void shoot(Player player) {
  int playerX = (int) (player.getX());
  int playerY = (int) (player.getY());

  double xDifference = getX() - player.getX();
  double yDifference = getY() - player.getY();

  double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

  double xChange = ((xDifference / hyp)*10);
  double yChange = ((yDifference / hyp)*10);

  enemyProjectiles.add(new Projectile(getX(), getY(), 25, 25, "Bullet", 20, xChange, yChange));
 }

 public void moveProjectile() {

  for (int i = 0; i < this.enemyProjectiles.size(); i++) {

   (this.enemyProjectiles.get(i)).moveUp((this.enemyProjectiles.get(i)).getChangeY());
   (this.enemyProjectiles.get(i)).moveLeft((this.enemyProjectiles.get(i)).getChangeX());

  }

 }

 public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
  for (int i = 0; i < enemyProjectiles.size(); i++) {
   (enemyProjectiles.get(i)).draw(g, offSetX, offSetY);
  }
 }

 
 Enemy(double x, double y, int width, int height, String name, double health, String weapon, Player player) {

  super(x, y, width, height, name, health, weapon);
  
  enemyProjectiles = new ArrayList<Projectile>();
   
 }

}