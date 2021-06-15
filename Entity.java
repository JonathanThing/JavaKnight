
/**
 * [Entity.java]
 * Description: The class for any object that is movable
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 27, 2021
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Entity extends GameObject implements Moveable {

	// Health
	private double health;

	/**
	 * getHealth
	 * 
	 * @return this.health, the heath of the enitity
	 */
	public double getHealth() {
		return this.health;
	}

	/**
	 * setHealth method to set the health of the entity
	 * 
	 * @param health, the value that the heath of the enitity will be set to
	 */
	public void setHealth(double health) {
		this.health = health;
	}

	/**
	 * moveLeft method to move the entity left
	 * 
	 * @param n, the value that the enitity will be moved by
	 */
	public void moveLeft(double n) {
		this.setX(this.getX() - n);
	}

	/**
	 * moveRight method to move the entity right
	 * 
	 * @param n, the value that the enitity will be moved by
	 */
	public void moveRight(double n) {
		this.setX(this.getX() + n);
	}

	/**
	 * moveUp method to move the entity up
	 * 
	 * @param n, the value that the enitity will be moved by
	 */
	public void moveUp(double n) {
		this.setY(this.getY() - n);
	}

	/**
	 * moveDown method to move the entity down
	 * 
	 * @param n, the value that the enitity will be moved by
	 */
	public void moveDown(double n) {
		this.setY(this.getY() + n);
	}

	// Constructor
	Entity(double x, double y, int width, int height, String name, BufferedImage sprite, double health) {
		super(x, y, width, height, name, sprite);
		this.health = health;
	}

}