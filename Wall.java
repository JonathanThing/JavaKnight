import java.awt.Graphics;

public class Wall extends Environment {


	Wall(double x, double y, String name) {
		super(x, y, 32, 32, name);

	}
	
	public void draw(Graphics g, double offSetX, double offSetY) {

		g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
	}
	
	  public void enemyHit(Enemy a) {
		    
		    for (int i = 0; i < (a.getProjectilesList()).size(); i++) {
		      
		      if (a.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) {
		        
		        a.getProjectilesList().remove(i);
		        
		      }
		      
		    }
		  }
		  
		  public void playerHit(Player p) {
		    
		    for (int i = 0; i < (p.getProjectilesList()).size(); i++) {
		      
		      if (p.getProjectilesList().get(i).getHitbox().intersects(this.getHitbox())) {
		        
		        p.getProjectilesList().remove(i);
		        
		      }
		      
		    }
		  }
}
