import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;

class HealthPack extends Pickups {

 private Rectangle healthPack;

 HealthPack(int x, int y, int width, int height, String name) {
  super(x, y, width, height, name);
 }

 public void drawItem(Graphics g) {
  g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
 }

 public boolean checkCollision(HealthPack a, Player p) {
  if ((a.getX() >= p.getX() - p.getWidth()) && (a.getX() <= p.getX() + p.getWidth())
    && (a.getY() >= p.getY() - p.getHeight()) && (a.getY() <= p.getY() + p.getHeight())) {
   p.setHealth(p.getHealth() + 100);
   return true;
  }
  
  return false;

 }

}