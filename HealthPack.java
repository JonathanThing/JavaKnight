import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class HealthPack extends Items {

	private Rectangle healthPack;

	HealthPack(int x, int y, int width, int height, String name, BufferedImage sprite) {
		super(x, y, width, height, name, sprite);
	}

	public void draw(Graphics g, double offSetX, double offSetY) {

		g.setColor(Color.GREEN);
		g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(), getHeight());
	}

	public boolean checkCollision(Player p) {
		if (this.getCollision().intersects(p.getCollision())) {
			return true;
		}
		
		return false;

	}

}