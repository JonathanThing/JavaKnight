import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class HealthPack extends Pickups {

	private Rectangle healthPack;

	HealthPack(int x, int y, int width, int height, String name) {
		super(x, y, width, height, name);
	}

	public void drawItem(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect((int) getX() - getWidth()/2, (int) getY() - getHeight()/2, getWidth(), getHeight());
	}

	public boolean checkCollision(Player p) {
		if (this.getCollision().intersects(p.getCollision())) {
			return true;
		}
		
		return false;

	}

}