import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Weapon extends GameObject {

 private int size;
 // damage this weapon deals
 private double damage;

 // rate at which this weapon can be used
 private double fireRate;

 public double getDamage() {
  return this.damage;
 }

 public double getFireRate() {
  return this.fireRate;
 }

 public int getSize() {
  return this.size;
 }

 public void drawWeapon(Graphics g) {
  g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
 }

 // constructor
 Weapon(int x, int y, int width, int height, String name, BufferedImage sprite, double damage, double fireRate, int size) {
  super(x, y, width, height, name, sprite);
  this.damage = damage;
  this.fireRate = fireRate;
  this.size = size;
 }

 public void draw(Graphics g, double offSetX, double offSetY) {

 }

}