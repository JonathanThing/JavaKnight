import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.util.ArrayList;
import java.lang.Math;

abstract class Pickups extends Items{

  Pickups(int x, int y, int width, int height, String name){
    super(x, y, width,  height, name);
  }
  
}