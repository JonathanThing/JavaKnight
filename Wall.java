import java.awt.Graphics;
import java.awt.Color;

public class Wall extends Environment {
  
  
  Wall(double x, double y, String name) {
    super(x, y, 32, 32, name);
    
  }
  
  public void draw(Graphics g, double offSetX, double offSetY) {
    g.setColor(Color.GRAY);
    g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
  }
  
  public void enemyHit(Enemy a) {
    
    for (int i = 0; i < (a.enemyProjectiles).size(); i++) {
      
      if (a.enemyProjectiles.get(i).getHitbox().intersects(this.getHitbox())) {
        
        a.enemyProjectiles.remove(i);
        
      }
      
    }
  }
  
  public void playerHit(Player p) {
    
    for (int i = 0; i < (p.playerProjectiles).size(); i++) {
      
      if (p.playerProjectiles.get(i).getHitbox().intersects(this.getHitbox())) {
        
        p.playerProjectiles.remove(i);
        
      }
      
    }
  }
  
  public boolean playerWin(Player p){
    return true;
  }
}