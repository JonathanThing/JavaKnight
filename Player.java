import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.util.ArrayList;
import java.lang.Math;

class Player extends Character {

 ArrayList<Projectile> playerProjectiles = new ArrayList<Projectile>();

 // sprite
 private BufferedImage sprite;

 // ammo
 private double ammo;

 // get ammo
 public double getAmmo() {
  return this.ammo;
 }

 // load sprite
 public void loadSprite() {
  try {

  } catch (Exception e) {
   System.out.println("error loading sprite");
  }
  ;
 }

 public void laserBeam(Graphics g, int x, int y, double offSetX, double offSetY) {

  g.setColor(Color.RED);
  g.drawLine((int) (this.getX() - offSetX), (int) (this.getY() - offSetY), (int) (x - offSetX),
    (int) (y - offSetY));
  g.setColor(Color.BLACK);
 }

 // draw
 public void draw(Graphics g, double offSetX, double offSetY) {

  g.drawRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(),
    getHeight());
 }

 // create projectiles
 public void shoot(double targetX, double targetY) {

  double xDifference = targetX - this.getX();
  double yDifference = targetY - this.getY();

  double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

  double xChange = ((xDifference / hyp) * 30);
  double yChange = ((yDifference / hyp) * 30);

  if (this.getWeapon().getName().equals("shotgun")) {
   
   
   
   for (int i = 0; i < 8; i++) {
    playerProjectiles.add(new Projectile(getX(), getY(), this.getWeapon().getSize(),
      this.getWeapon().getSize(), "Bullet", 20, xChange + Math.random() * (-16) + 8, yChange + Math.random() * (-16) + 8));
   }
  } else if (this.getWeapon().getName().equals("smg")) {
   playerProjectiles.add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet", 20, xChange + Math.random() * (-2) + 1, yChange + Math.random() * (-4) + 1));

  } else {
   playerProjectiles.add(new Projectile(getX(), getY(), this.getWeapon().getSize(), this.getWeapon().getSize(),
     "Bullet", 20, xChange, yChange));
  }
 }

 // move projectiles
 public void moveProjectile() {

  for (int i = 0; i < playerProjectiles.size(); i++) {

   (playerProjectiles.get(i)).moveDown((playerProjectiles.get(i)).getChangeY());
   (playerProjectiles.get(i)).moveRight((playerProjectiles.get(i)).getChangeX());

  }

 }

 // draw projectiles
 public void drawPlayerProjectile(Graphics g, double offSetX, double offSetY) {
  for (int i = 0; i < playerProjectiles.size(); i++) {
   (playerProjectiles.get(i)).draw(g, offSetX, offSetY);
  }
 }

 public void removeProjectile(int i) {
  playerProjectiles.remove(i);
 }

 public void movement(boolean up, boolean down, boolean left, boolean right, ArrayList<Enemy> list,
   Environment[][] map) {

  double xMove = 0;
  double yMove = 0;

  if (up) {
   yMove += 1;
  }

  if (down) {
   yMove -= 1;
  }

  if (left) {
   xMove -= 1;
  }

  if (right) {
   xMove += 1;
  }

  double hyp = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2));

  if (hyp != 0) {

   this.moveRight((xMove / hyp) * 5);

   if (collision(list, map)) {

    this.moveLeft((xMove / hyp) * 5);
   }

   this.moveUp((yMove / hyp) * 5);

   if (collision(list, map)) {

    this.moveDown((yMove / hyp) * 5);

   }

  }

 }

 public boolean collision(ArrayList<Enemy> a, Environment[][] b) {

  for (int i = 0; i < a.size(); i++) {
   if (this.getCollision().intersects(a.get(i).getCollision())) {
    return true;
   }
  }

  for (int i = 0; i < b.length; i++) {
   for (int j = 0; j < b[0].length; j++) {
    if ((b[i][j] != null) && (!b[i][j].getName().equals("winblock")) && (this.getCollision().intersects(b[i][j].getCollision()))) {
     return true;
    }
   }
  }
  return false;
 }

 public boolean wasHit(Enemy a) {

  for (int i = 0; i < (a.enemyProjectiles).size(); i++) {

   if (a.enemyProjectiles.get(i).getHitbox().intersects(this.getHitbox())) {

    a.enemyProjectiles.remove(i);

    return true;
   }

  }

  return false;
 }

 // constructor
 Player(double x, double y, int width, int height, String name, double health, Weapon weapon, double ammo) {
  // Player(int x, int y, int width, int height, BufferedImage sprite, String
  // name, double health, String weapon, double ammo){
  super(x, y, width, height, name, health, weapon);
  // super(x, y, width,height, sprite, name, health, weapon);
  this.ammo = ammo;
  loadSprite();
 }

}