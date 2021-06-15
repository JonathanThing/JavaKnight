import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Zombie extends Enemy {

	private long lastHit;
	private Player player;

	public void move(Player player, Environment[][] b, ArrayList<Enemy> a) {
		int playerX = (int) (player.getX());
		int playerY = (int) (player.getY());

		double xDifference = getX() - player.getX();
		double yDifference = getY() - player.getY();

		double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

		double xChange = ((xDifference / hyp) * 3);
		double yChange = ((yDifference / hyp) * 3);

		this.moveLeft(xChange);

		if (collision(player, b, a)) {

			this.moveRight(xChange);
		}

		this.moveUp(yChange);

		if (collision(player, b, a)) {

			this.moveDown(yChange);

		}
	}

	public boolean collision(Player player, Environment[][] b, ArrayList<Enemy> a) {

		for (int i = 0; i < a.size(); i++) {
			if ((this != a.get(i)) && this.getCollision().intersects(a.get(i).getCollision())) {
				return true;
			}
		}

		if (this.getCollision().intersects(player.getCollision())) {
			if (System.nanoTime() - lastHit >= 1e9) {
				player.setHealth(player.getHealth() - 10);
				lastHit = System.nanoTime();
			}
			return true;
		}

		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				if ((b[i][j] != null) && (this.getCollision().intersects(b[i][j].getCollision()))) {
					return true;
				}
			}
		}
		return false;
	}

	Zombie(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
		super(x, y, width, height, name, sprite, health, weapon);
		lastHit = System.nanoTime();
	}

	@Override
	public void attack(Player player) {
		// TODO Auto-generated method stub

	}

}