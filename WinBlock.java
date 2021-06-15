import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class WinBlock extends Environment {

	WinBlock(double x, double y, String name, BufferedImage sprite) {
		super(x, y, 32, 32, name, sprite);

	}

	public void draw(Graphics g, double offSetX, double offSetY) {
		g.setColor(Color.YELLOW);
		g.fillRect((int) (getX() - getWidth() / 2 - offSetX), (int) (getY() - getHeight() / 2 - offSetY), getWidth(),
				getHeight());
	}

	public void enemyHit(Enemy a) {

	}

	public void playerHit(Player p) {

	}

	public boolean playerWin(Player p) {
		// System.out.println("detecting hit");
		if (p.getCollision().intersects(this.getHitbox())) {
			System.out.println("hit win");
			return true;
		}

		return false;
	}

}