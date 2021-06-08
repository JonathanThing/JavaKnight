import java.awt.Graphics;

public class Wall extends Environment {


	Wall(double x, double y, String name) {
		super(x, y, 64, 64, name);

	}
	
	public void draw(Graphics g, double offSetX, double offSetY) {

		g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
	}
	
}
