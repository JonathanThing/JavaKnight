import java.awt.image.BufferedImage;

abstract class Items extends GameObject{

  Items(double x, double y, int width, int height, String name, BufferedImage sprite){
    super(x, y, width,  height, name, sprite);
  }

}