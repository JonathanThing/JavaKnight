
//Graphics &GUI imports
import java.io.File;
import java.awt.FontMetrics;

import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

//Import ArrayList
import java.util.ArrayList;
import java.util.Scanner;

//Import Math
import java.lang.Math;

public class Game extends JFrame {

	/****************** CLASS VARIABLES *******************/
	/** The variables can be accessed across all methods **/
	/******************************************************/
	static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>();
	static ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
	static Weapon[] weapons;
	static Environment[][] map;

	static GameAreaPanel gamePanel;
	static Graphics g;
	static int gameState = 0; // 0 = Menu, 1 = Game, 2 = dead, 3 = win, 4 = map editor
	static String mapName;

	static Scanner input;
	static Player player;
	static int playerX, playerY;
	static boolean up, down, left, right;
	static boolean shooting;
	static long lastShot;
	static int mouseX, mouseY;

	static int offsetXMax, offsetYMax;
	static int offsetXMin, offsetYMin;
	static int worldSizeX, worldSizeY;
	static double offsetX, offsetY;

	static int editorSizeX, editorSizeY;
	static char[][] editingMap;
	static char paintBrush;

	static Font title = new Font("Serif", Font.PLAIN, 50);
	static Font subTitle = new Font("Serif", Font.PLAIN, 25);
	static Font health = new Font("Serif", Font.PLAIN, 20);

	static Image img = Toolkit.getDefaultToolkit().getImage("images/background.jpg");

	/***************************************************************/
	/** GameFrame - Setups up the Window and Starts displaying it **/
	/***************************************************************/

	Game() {

		super("My Game");

		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set resolution 1280x780
		this.setSize(1280, 720);

		// Prevent resizing of the tab
		this.setResizable(false);

		// Create Game panel for rendering
		gamePanel = new GameAreaPanel();
		this.add(new GameAreaPanel());

		// Keyboard Listener
		MyKeyListener keyListener = new MyKeyListener();
		this.addKeyListener(keyListener);

		// Mouse Listener
		MyMouseListener mouseListener = new MyMouseListener();
		this.addMouseListener(mouseListener);

		MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
		this.addMouseMotionListener(mouseMotionListener);

		// Sets the frame in focused
		this.requestFocusInWindow();

		// Thread
		Thread t = new Thread(new Runnable() {
			public void run() {
				animate();
			}
		}); // start the gameLoop
		t.start();

	}

	/************************ End of GameFrame ***********************/

	/********************* Main Method ************************/
	public static void main(String[] args) throws Exception {

		input = new Scanner(System.in);

		sprites.add(ImageIO.read(new File("images/stonebrick.png"))); // 0
		sprites.add(ImageIO.read(new File("images/mossystone.png"))); // 1
		sprites.add(ImageIO.read(new File("images/oakwood.png"))); // 2
		sprites.add(ImageIO.read(new File("images/player.png"))); // 3
		sprites.add(ImageIO.read(new File("images/skeleton.png"))); // 4
		sprites.add(ImageIO.read(new File("images/zombie.png"))); // 5
		sprites.add(ImageIO.read(new File("images/witherskeleton.png"))); // 6
		sprites.add(ImageIO.read(new File("images/healthpack.png"))); // 7
		sprites.add(ImageIO.read(new File("images/pistol.png"))); // 8
		sprites.add(ImageIO.read(new File("images/smg.png"))); // 9
		sprites.add(ImageIO.read(new File("images/shotgun.png"))); // 10

		weapons = new Weapon[6];

		// Player Weapons
		weapons[0] = new Weapon(0, 0, 26, 26, "pistol", sprites.get(8), 30, 0.4, 10);
		weapons[1] = new Weapon(0, 0, 32, 32, "smg", sprites.get(9), 10, 0.1, 10);
		weapons[2] = new Weapon(0, 0, 58, 20, "shotgun", sprites.get(10), 15, 1, 10);

		// Enemy Weapons
		weapons[3] = new Weapon(0, 0, 10, 10, "bow", sprites.get(0), 15, 0.8, 10);
		weapons[4] = new Weapon(0, 0, 10, 10, "buckshot", sprites.get(0), 10, 1, 10);

		mapName = "map1";
		mapInit("maps/" + mapName + ".txt");

		EventQueue.invokeLater(() ->

		{
			Game gameInstance = new Game();
			gameInstance.setVisible(true);
		});
	}

	public static void mapInit(String file) {
		enemyList.clear();
		healthPacks.clear();
		char[][] temp = null;

		try {
			temp = getMap(file);
			System.out.println("got file");
		} catch (Exception e) {

			System.out.println("it broke");
		}

		map = new Environment[temp.length][temp[0].length];

		worldSizeX = 32 * temp[0].length + 16;
		worldSizeY = 32 * temp.length + 32;

		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				switch (temp[i][j]) {
				case 'w':
					if (Math.random() >= 0.75) {
						map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(1));
					} else {
						map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(0));
					}
					break;

				case 'b':
					map[i][j] = new WinBlock((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "winblock", sprites.get(0));
					break;

				case 's':
					enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "skeleton",
							sprites.get(4), 100, weapons[3]));
					break;

				case 'S':
					enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "eliteSkeleton",
							sprites.get(6), 200, weapons[4]));
					break;

				case 'h':
					healthPacks.add(
							new HealthPack((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "HP", sprites.get(7)));
					break;

				case 'p':
					player = new Player((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "player", sprites.get(3),
							100, weapons[0]);
					playerX = (int) j * 32 + 32 / 2;
					playerY = (int) i * 32 + 23 / 2;
					break;

				case 'z':
					enemyList.add(new Zombie((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "zombie",
							sprites.get(5), 100, weapons[1]));
					break;
				}
			}
		}
	}

	public static char[][] getMap(String file) throws Exception {

		File myFile = new File(file);

		// output to message to the user via the console
		System.out.println("Attempting to read data from file: " + file);

		// Create a Scanner and associate it with the file
		Scanner input = new Scanner(myFile);

		int x = input.nextInt();
		int y = input.nextInt();

		String line = "";

		input.nextLine();

		char[][] a = new char[y][x];

		for (int i = 0; i < y; i++) {
			line = input.nextLine();
			// System.out.println(i);
			for (int j = 0; j < x; j++) {
				// System.out.println(j);
				a[i][j] = line.charAt(j);
				// System.out.println(a[i][j]);
			}
		}

		return a;

	}

	/****** end of Main *********************************/

	/********************* Animate - Gameloop ************************/
	/******* This section is where the games state is updated. *******/
	/*****************************************************************/
	public void animate() {

		// Intialize functions

		while (true) {

			if (gameState == 1) { // Game State

				gameTick();

			} else if (gameState == 4) { // Map editor
				mapEditorTick();
			}

			this.repaint(); // update the screen
		}

	}

	/****** End of Animate *********************************/

	// Inner class - JPanel
	private class GameAreaPanel extends JPanel {

		/************************** PaintComponenet ************************/
		/** This section is where the screen is drawn **/
		/*******************************************************************/

		public void paintComponent(Graphics g) {
			super.paintComponent(g); // required
			setDoubleBuffered(true);

			// Call render methods
			if (gameState == 0) {
				menuRender(g);

			} else if (gameState == 1) { // Game State
				gameRender(g);

			} else if (gameState == 2) {
				deathRender(g);

			} else if (gameState == 3) {
				winRender(g);
			} else if (gameState == 4) {
				mapEditorRender(g);
			}
		}
	}

	public void deathInit() {
		setCursor(Cursor.getDefaultCursor());
	}

	/****** End of paintComponent *********************************/

	public void menuInit() {

		player.setX(playerX);
		player.setY(playerY);

		player.setHealth(100);

		setCursor(Cursor.getDefaultCursor());
	}

	public void winInit() {
		setCursor(Cursor.getDefaultCursor());
	}

	public void gameInit() {

		mapInit("maps/" + mapName + ".txt");

		player.setHealth(100);
		lastShot = System.nanoTime();

		offsetXMax = worldSizeX - 1280;
		offsetYMax = worldSizeY - 720;
		offsetXMin = 0;
		offsetYMin = 0;

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("images/crosshair.png").getImage(),
				new Point(16, 16), "crosshair"));
	}

	public void mapEditorInit() {
		System.out.print("\nMap editor:\nPlease enter the width of the map in tiles: ");
		editorSizeX = input.nextInt();
		System.out.print("\nPlease enter the height of the map in tiles: ");
		editorSizeY = input.nextInt();

		editingMap = new char[editorSizeY][editorSizeX];

		paintBrush = 0;

		offsetX = 0;
		offsetY = 0;

		for (int i = 0; i < editorSizeY; i++) {
			for (int j = 0; j < editorSizeX; j++) {
				editingMap[i][j] = ' ';

			}
		}
	}

	// Updating ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// Game update
	public void gameTick() {

		if (player.getHealth() <= 0) {
			changeState(2);
		}

		offsetX = player.getX() - 1280 / 2;
		offsetY = player.getY() - 720 / 2;

		if (offsetX > offsetXMax) {
			offsetX = offsetXMax;
		} else if (offsetX < offsetXMin) {
			offsetX = offsetXMin;
		}
		if (offsetY > offsetYMax) {
			offsetY = offsetYMax;
		} else if (offsetY < offsetYMin) {
			offsetY = offsetYMin;
		}

		if (shooting && (System.nanoTime() - lastShot >= 1e9 * player.getWeapon().getFireRate())) {
			player.shoot(mouseX + offsetX, mouseY + offsetY, sprites.get(0));
			lastShot = System.nanoTime();
		}

		player.movement(up, down, left, right, enemyList, map);

		player.moveProjectile();

		// Enviroment collisons
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {

				if (map[i][j] != null) {

					if (map[i][j].getName().equals("wall")) {
						map[i][j].playerHit(player);
					} else if (map[i][j].getName().equals("winblock")) {
						if (map[i][j].playerWin(player)) {
							// player.setHealth(0);
							changeState(3);
						}
					}

				}

			}
		}

		// Enemy
		for (int i = 0; i < enemyList.size(); i++) {

			if (!enemyList.get(i).getName().equals("zombie")) {
				((Skeleton) enemyList.get(i)).moveProjectile();
			}

			// enemy bullet hiting wall;
			for (int a = 0; a < map.length; a++) {
				for (int b = 0; b < map[0].length; b++) {

					if (map[a][b] != null) {
						if (map[a][b].getName().equals("wall")) {
							map[a][b].enemyHit(enemyList.get(i));
						}
					}

				}
			}

			if (player.wasHit(enemyList.get(i))) {
				player.setHealth(player.getHealth() - enemyList.get(i).getWeapon().getDamage());
			}

			enemyList.get(i).getHit(player);

			if ((enemyList.get(i)).getHealth() <= 0) {
				enemyList.remove(i);
				break;
			}

			if (player.getAggro().intersects(enemyList.get(i).getCollision())) {
				if (!enemyList.get(i).getName().equals("zombie")) {
					(enemyList.get(i)).attack(player);
				} else {
					((Zombie) enemyList.get(i)).move(player, map, enemyList);
				}

			}

		}

		// HealthPack
		for (int i = 0; i < healthPacks.size(); i++) {

			if ((healthPacks.get(i)).checkCollision(player)) {
				if (player.getHealth() >= 50) {
					player.setHealth(100);
				} else {
					player.setHealth(player.getHealth() + 50);
				}

				healthPacks.remove(i);
				break;
			}

		}

		try {
			Thread.sleep(16); // 16 = 60fps, 33 = 30fps
		} catch (Exception exc) {

		} // delay

	}

	public void mapEditorTick() {

		double xMove = 0;
		double yMove = 0;

		if (up) {
			yMove += 1;
		}

		if (down) {
			yMove -= 1;
		}

		if (left) {
			xMove -= 1;
		}

		if (right) {
			xMove += 1;
		}

		double hyp = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2));

		if (hyp != 0) {

			offsetX += ((xMove / hyp) * 5);

			offsetY -= ((yMove / hyp) * 5);
		}

		int tileX = (int) ((mouseX + offsetX) / 32);
		int tileY = (int) ((mouseY + offsetY) / 32);

		if ((shooting) && (tileX < editorSizeX && tileX >= 0) && (tileY < editorSizeY && tileY >= 0)) {
			switch (paintBrush) {
			case 2: // Nothing
				editingMap[tileY][tileX] = ' ';
				break;
			case 0: // Wall
				editingMap[tileY][tileX] = 'w';
				break;
			case 8: // Win Block
				editingMap[tileY][tileX] = 'b';
				break;
			case 3: // Player
				editingMap[tileY][tileX] = 'p';
				break;
			case 4: // Skeleton
				editingMap[tileY][tileX] = 's';
				break;
			case 5: // Zombie
				editingMap[tileY][tileX] = 'z';
				break;
			case 6: // Elite Skeleton
				editingMap[tileY][tileX] = 'S';
				break;
			case 7: // Health Pack
				editingMap[tileY][tileX] = 'h';
				break;

			}

		}

		try {
			Thread.sleep(16); // 16 = 60fps, 33 = 30fps
		} catch (Exception exc) {

		} // delay
	}

	// Rendering ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// Rendering the Menu
	public void menuRender(Graphics g) {

		g.drawImage(img, 0, 0, null);

		g.setColor(Color.BLACK);
		g.drawRect(640 - 50, 320 + 20, 100, 40);
		g.drawRect(640 - 50, 370 + 20, 100, 40);
		g.drawRect(640 - 50, 420 + 20, 100, 40);
		g.drawRect(640 - 50, 420 + 20, 100, 40);

		g.setColor(Color.GREEN);
		g.setFont(title);
		drawCenteredString("Java Knight", 1280, 720, g);

		g.setFont(subTitle);
		drawCenteredString("Play", 1280, 820, g);
		drawCenteredString("Play 2", 1280, 920, g);
		drawCenteredString("Play 3", 1280, 1020, g);
		drawCenteredString("Quit", 1280, 1120, g);
	}

	// rendering when you die
	public void deathRender(Graphics g) {

		g.setFont(title);
		drawCenteredString("Game Over", 1280, 720, g);

		g.setFont(subTitle);
		drawCenteredString("Menu", 1280, 820, g);
		g.drawRect(640 - 50, 320 + 20, 100, 40);

		drawCenteredString("Quit", 1280, 920, g);
		g.drawRect(640 - 50, 370 + 20, 100, 40);

	}

	// Rendering when you win

	public void winRender(Graphics g) {

		g.setFont(title);
		drawCenteredString("You Win!!!", 1280, 720, g);

		g.setFont(subTitle);
		drawCenteredString("Menu", 1280, 820, g);
		g.drawRect(640 - 50, 320 + 20, 100, 40);

		drawCenteredString("Quit", 1280, 920, g);
		g.drawRect(640 - 50, 370 + 20, 100, 40);

	}

	// Rendering the Game
	public void gameRender(Graphics g) {

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == null) {
					g.drawImage(sprites.get(2), (int) (j * 32 - offsetX), (int) (i * 32 - offsetY), null);
				} else {
					map[i][j].draw(g, offsetX, offsetY);
				}
			}
		}

		player.laserBeam(g, (int) (mouseX + offsetX), (int) (mouseY + offsetY), offsetX, offsetY);
		player.draw(g, offsetX, offsetY);

		player.drawPlayerProjectile(g, offsetX, offsetY);
		for (int j = 0; j < healthPacks.size(); j++) {
			healthPacks.get(j).draw(g, offsetX, offsetY);
		}

		for (int i = 0; i < enemyList.size(); i++) {

			(enemyList.get(i)).draw(g, offsetX, offsetY);
			if (!enemyList.get(i).getName().equals("zombie")) {
				((Skeleton) enemyList.get(i)).drawEnemyProjectile(g, offsetX, offsetY);
			}
		}

		g.setColor(Color.BLACK);
		g.fillRect((int) (player.getX() - offsetX - 13), (int) (player.getY() - offsetY - 35), 32, 17);

		g.setFont(health);
		g.setColor(Color.RED);
		g.drawString("" + (int) player.getHealth(), (int) (player.getX() - offsetX - 13),
				(int) (player.getY() - offsetY - 20));

		g.drawImage(player.getWeapon().getSprite(), (int) (mouseX - player.getWeapon().getWidth() / 2),
				(int) (mouseY - player.getWeapon().getHeight() / 2 - 30), null);

	}

	public void mapEditorRender(Graphics g) {
		g.setColor(Color.BLACK);
		int tempInt = 0;
		for (int i = 0; i < editingMap.length; i++) {
			for (int j = 0; j < editingMap[0].length; j++) {
				if (editingMap[i][j] == 'b') {
					g.setColor(Color.YELLOW);
					g.fillRect((int) (32 * j - offsetX), (int) (32 * i - offsetY), 32, 32);
				} else {
					switch (editingMap[i][j]) {
					case 'w':
						tempInt = 0;
						break;
					case ' ':
						tempInt = 2;
						break;
					case 's':
						tempInt = 4;
						break;
					case 'z':
						tempInt = 5;
						break;
					case 'S':
						tempInt = 6;
						break;
					case 'h':
						tempInt = 7;
						g.drawImage(sprites.get(2), (int) (32 * j - offsetX), (int) (32 * i - offsetY), null); //Drawing the floor under healthpack
						break;
					case 'p':
						tempInt = 3;
						break;
					}

					g.drawImage(sprites.get(tempInt), (int) (32 * j - offsetX), (int) (32 * i - offsetY), null);
				}
			}
		}
		
		g.setColor(Color.gray);
		g.fillRect(0,0,500,70);
		
		if (paintBrush == 8) {
			g.setColor(Color.YELLOW);
			g.fillRect(10, 10, 32, 32);
		} else {
			g.drawImage(sprites.get(paintBrush), 10, 10, null);
		}

	}

	// Method to change the states of the game and intialize the things needed.
	public void changeState(int a) {

		gameState = a;

		if (gameState == 0) {
			menuInit();

		} else if (gameState == 1) {
			gameInit();
		} else if (gameState == 2) {
			deathInit();
		} else if (gameState == 3) {
			winInit();
		} else if (gameState == 4) {
			mapEditorInit();
		}

	}

	public void drawCenteredString(String text, int width, int height, Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int x = (width - fontMetrics.stringWidth(text)) / 2;
		int y = (fontMetrics.getAscent() + (height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2) - 50;
		g.drawString(text, x, y);
	}

	/***************************** Key Listener ************************/
	/** This section is where keyboard input is handled **/
	/** You will add code to respond to key presses **/
	/*******************************************************************/
	class MyKeyListener implements KeyListener {

		public void keyTyped(KeyEvent e) {

			if (e.getKeyChar() == 'l') {
				player.setHealth(0);
			}

			if (e.getKeyChar() == 'm') {
				changeState(4);
			}

			if (e.getKeyChar() == '1') {
				player.setWeapon(weapons[0]);
			} else if (e.getKeyChar() == '2') {
				player.setWeapon(weapons[1]);
			} else if (e.getKeyChar() == '3') {
				player.setWeapon(weapons[2]);
			}

		}

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == 'W') {
				up = true;
			}
			if (e.getKeyCode() == 'S') {
				down = true;
			}
			if (e.getKeyCode() == 'A') {
				left = true;
			}
			if (e.getKeyCode() == 'D') {
				right = true;
			}

			if (gameState == 4) {
				switch (e.getKeyCode()) {
				case '1': // Nothing
					paintBrush = 2;
					break;
				case '2': // Wall
					paintBrush = 0;
					break;
				case '3': // Win Block
					paintBrush = 8;
					break;
				case '4': // Player
					paintBrush = 3;
					break;
				case '5': // Skeleton
					paintBrush = 4;
					break;
				case '6': // Zombie
					paintBrush = 5;
					break;
				case '7': // Elite Skeleton
					paintBrush = 6;
					break;
				case '8': // Health Pack
					paintBrush = 7;
					break;

				}

			}
		}

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == 'W') {
				up = false;
			}
			if (e.getKeyCode() == 'S') {
				down = false;
			}
			if (e.getKeyCode() == 'A') {
				left = false;
			}
			if (e.getKeyCode() == 'D') {
				right = false;
			}
		}
	}

	/****** Key Listener *********************************/

	/**************************** Mouse Listener ************************/
	/** This section is where mouse input is handled **/
	/** You may have to add code to respond to mouse clicks **/
	/********************************************************************/
	class MyMouseMotionListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX() - 7;
			mouseY = e.getY() - 30;
		}

		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX() - 7;
			mouseY = e.getY() - 30;

		}

	}

	class MyMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

			// temporary variables just in case the mouse has to be offset
			int tempX = e.getX();
			int tempY = e.getY();

			// menu buttons
			if ((gameState == 0) && (tempX >= 590) && (tempX <= 690)) {
				if ((tempY >= 370) && (tempY <= 410)) {
					mapName = "map1";
					changeState(1);

				} else if ((tempY >= 410) && (tempY <= 450)) {
					mapName = "map2";
					changeState(1);

				} else if ((tempY >= 460) && (tempY <= 500)) {
					mapName = "map3";
					changeState(1);

				} else if ((tempY >= 510) && (tempY <= 550)) {
					System.exit(0);
				}

			} else if ((gameState == 1) || (gameState == 4)) {
				shooting = true;

				// death screen buttons
			} else if ((gameState == 2) && (tempX >= 590) && (tempX <= 690)) {

				if ((tempY >= 340) && (tempY <= 410)) {
					changeState(0);
				} else if ((tempY >= 370) && (tempY <= 460)) {
					System.exit(0);
				}
				// win screen buttons
			} else if ((gameState == 3) && (tempX >= 590) && (tempX <= 690)) {
				if ((tempY >= 340) && (tempY <= 410)) {
					changeState(0);
				} else if ((tempX <= 690) && (tempY >= 370) && (tempY <= 460)) {
					System.exit(0);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if ((gameState == 1) || (gameState == 4)) {
				shooting = false;
			}

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

	}

	/****** Mouse Listener *********************************/
}