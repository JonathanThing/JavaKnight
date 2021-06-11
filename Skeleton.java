import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Skeleton extends Enemy {

	private long lastShot;
	private Player player;
	
	public void shoot(Player player, BufferedImage sprite) {
		int playerX = (int) (player.getX());
		int playerY = (int) (player.getY());

		double xDifference = getX() - player.getX();
		double yDifference = getY() - player.getY();

		double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

		double xChange = ((xDifference / hyp)*6);
		double yChange = ((yDifference / hyp)*6);

		
		if (this.getName().equals("eliteSkeleton")) {
			for (int i = 0 ; i < 4; i++) {
				this.getProjectilesList().add(new Projectile(getX(), getY(), 20, 20, "Bullet", sprite, 20, xChange+Math.random()*-2 + 1, yChange+Math.random()*-2 + 1));
			}
		} else {
			this.getProjectilesList().add(new Projectile(getX(), getY(), 20, 20, "Bullet", sprite, 20, xChange, yChange));

		}
	}

	public void moveProjectile() {

		for (int i = 0; i < this.getProjectilesList().size(); i++) {

			(this.getProjectilesList().get(i)).moveUp((this.getProjectilesList().get(i)).getChangeY());
			(this.getProjectilesList().get(i)).moveLeft((this.getProjectilesList().get(i)).getChangeX());

		}

	}

	public void drawEnemyProjectile(Graphics g, double offSetX, double offSetY) {
		for (int i = 0; i < this.getProjectilesList().size(); i++) {
			(this.getProjectilesList().get(i)).draw(g, offSetX, offSetY);
		}
	}
	

	Skeleton(double x, double y, int width, int height, String name, BufferedImage sprite, double health, Weapon weapon) {
		super(x, y, width, height, name, sprite, health, weapon);
		lastShot = System.nanoTime();
	}

	public void attack(Player player, BufferedImage sprite) {
		
		if (System.nanoTime()-lastShot >= 1e9 * this.getWeapon().getFireRate()) {
			this.shoot(player, sprite);	
			lastShot = System.nanoTime();
		}
	}

}