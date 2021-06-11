import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Environment extends GameObject {

	Environment(double x, double y, int width, int height, String name, BufferedImage sprite) {
		super(x, y, width, height, name, sprite);

	}

	 public abstract void enemyHit(Enemy a);
	 
	 public abstract void playerHit(Player p);
	
}
