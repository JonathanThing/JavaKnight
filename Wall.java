import java.awt.Graphics;

public class Wall extends Environment {


	Wall(double x, double y, String name) {
		super(x, y, 64, 64, name);

	}
	
	public void draw(Graphics g) {
		g.fillRect((int) getX() - getWidth()/2, (int) getY() -  getHeight()/2, getWidth(), getHeight());
		
	}
	
}
