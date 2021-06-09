import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

class Ammo extends Items {
  
  private int ammoValue;
  
  public int getAmmoValue(){
    return this.ammoValue;
  }
  
  public void drawAmmo(Graphics g){
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
  }
  
  Ammo(int x, int y, int width, int height, String name, int ammoValue){
    super(x, y, width,  height, name);
    this.ammoValue = ammoValue;
  }

@Override
public void draw(Graphics g, double offSetX, double offSetY) {
 // TODO Auto-generated method stub
 
}
  
}