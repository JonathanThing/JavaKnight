import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;
class HealthPack extends Pickups{
  
  private Rectangle healthPack;
  
  HealthPack(int x, int y, int width, int height, String name){
    super(x, y, width, height, name);
  }
  
  public void drawItem(Graphics g){
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
  }
  
}