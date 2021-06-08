import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Read extends Items {
  
  private int ammoValue;
  
  public int getAmmoValue(){
    return this.ammoValue;
  }
  
  public void drawAmmo(Graphics g){
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
  }
  
  Read(int x, int y, int width, int height, String name, int ammoValue){
    super(x, y, width,  height, name);
    this.ammoValue = ammoValue;
  }
  
}