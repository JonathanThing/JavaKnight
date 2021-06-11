  
import java.awt.Graphics;

public abstract class Environment extends GameObject {

 Environment(double x, double y, int width, int height, String name) {
  super(x, y, width, height, name);

 }

  public abstract void enemyHit(Enemy a);
  
  public abstract void playerHit(Player p);
  
  public abstract boolean playerWin(Player p);
 
}
