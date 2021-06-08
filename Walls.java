import java.awt.Graphics;

class Walls extends Environment{
  
  Walls(double x, double y, int width, int height, String name, Graphics g){
    super(x, y, width, height, name);
    draw(g);
  }
  
  public void assignMap(){
    char[][] map = null;
  }
  
  public void draw(Graphics g){
    
    g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
    
  }
  
}
