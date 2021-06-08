import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Enemy extends Character {

	public ArrayList<Projectile> enemyProjectiles;

	private Player player;

	public void drawEnemy(Graphics g) {

		g.setColor(Color.RED);
		g.fillRect((int) getX() - getWidth() / 2, (int) getY() - getHeight() / 2, getWidth(), getHeight());
	}

	public boolean getHit(Player player) {

		for (int i = 0; i < (player.playerProjectiles).size(); i++) {

			if (player.playerProjectiles.get(i).getHitbox().intersects(this.getHitbox())) {

				return true;
			}

		}

		return false;
	}


	public void shoot(Player player) {
		int playerX = (int) (player.getX());
		int playerY = (int) (player.getY());

		double xDifference = getX() - player.getX();
		double yDifference = getY() - player.getY();

		double hyp = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

		double xChange = ((xDifference / hyp)*10);
		double yChange = ((yDifference / hyp)*10);

		enemyProjectiles.add(new Projectile(getX(), getY(), 25, 25, "Bullet", 20, xChange, yChange));
	}

	public void moveProjectile() {

		for (int i = 0; i < this.enemyProjectiles.size(); i++) {

			(this.enemyProjectiles.get(i)).moveUp((this.enemyProjectiles.get(i)).getChangeY());
			(this.enemyProjectiles.get(i)).moveLeft((this.enemyProjectiles.get(i)).getChangeX());

		}

	}

	public void drawEnemyProjectile(Graphics g) {
		for (int i = 0; i < enemyProjectiles.size(); i++) {
			(enemyProjectiles.get(i)).drawProjectile(g);
		}
	}

	Enemy(double x, double y, int width, int height, String name, double health, String weapon, Player player) {
		// Enemy(int x, int y, int width, int height, BufferedImage sprite, String name,
		// double health, String weapon, Player player){
		super(x, y, width, height, name, health, weapon);
		// super(x, y, width, height, sprite, name, health, weapon);
		
		enemyProjectiles = new ArrayList<Projectile>();
		 
	}

}