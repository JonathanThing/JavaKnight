/**
 * [Game.java]
 * The main file for the game Java Knight.
 * Java Knight is a 2d top down shooter game where the player fight
 * enemies and attempts to reach the end of the level.
 * The player can either choose to play the premade levels or create
 * their own levels and play them using the map editor. * 
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 31, 2021
 */

//Graphics &GUI imports
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

//File I/O
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.FontMetrics;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.Point;

//Import ArrayList
import java.util.ArrayList;
import java.util.Scanner;

//Import Math
import java.lang.Math;

public class Game extends JFrame {

	// Class Variables
	static ArrayList<Enemy> enemyList = new ArrayList<Enemy>(); // Array List storing Enemies
	static ArrayList<HealthPack> healthPacks = new ArrayList<HealthPack>(); // Array List storing Healthpacks
	static ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>(); // Array list storing Sprites (more
																				// efficient)

	static Weapon[] weapons; // Array storing Weapons
	static Environment[][] map; // 2d array storing the map
	static Player player; // Player

	static GameAreaPanel gamePanel;
	static Graphics g;

	static int gameState = 0; // Variable to store the gamestate. 0 = Menu, 1 = Game, 2 = dead, 3 = win, 4 =
								// map editor

	// Map editor variables
	static String mapName; // Name of the map selected
	static String[] preMadeMaps; // Array to store the premade maps
	static int selectedMapIndex; // The index of the array preMadeMaps;
	static int editorSizeX, editorSizeY; // The Dimensions of the map in the map editor
	static char[][] editingMap; // 2d Array to store the map in the map editor
	static char paintBrush; // The current block selected in map editor

	static Scanner input; // Console input
	static boolean up, down, left, right; // Variables to store if player is moving in certain direction
	static boolean shooting; // Variable to store if the player is shooting/holding down the mouse
	static long lastShot; // Variables used to store the last time the player has shot
	static int mouseX, mouseY; // X and Y positions of the mouse

	// Camera variables
	static int offsetXMax, offsetYMax; // Maxmium limit for the position of the camera
	static int offsetXMin, offsetYMin; // Minium limit for the positoin of the camera
	static int worldSizeX, worldSizeY; // The dimensions of the world
	static double offsetX, offsetY; // the position of the camera

	// Fonts
	static Font title = new Font("Serif", Font.PLAIN, 50);
	static Font subTitle = new Font("Serif", Font.PLAIN, 25);
	static Font health = new Font("Serif", Font.PLAIN, 20);

	// Background Image
	static Image img = Toolkit.getDefaultToolkit().getImage("images/background.jpg");

	// GameFrame - Setups up the Window and starts displaying it
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

	//Main method
	public static void main(String[] args) throws Exception {

		// Console Scanner
		input = new Scanner(System.in);

		// Assinging the sprites into a arraylist to save resources and allow easier
		// spirite access
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

		// Creates the weapons array
		weapons = new Weapon[6];

		// Player Weapons
		weapons[0] = new Weapon(0, 0, 26, 26, "pistol", sprites.get(8), 30, 0.4, 10);
		weapons[1] = new Weapon(0, 0, 32, 32, "smg", sprites.get(9), 10, 0.1, 10);
		weapons[2] = new Weapon(0, 0, 58, 20, "shotgun", sprites.get(10), 15, 1, 10);

		// Enemy Weapons
		weapons[3] = new Weapon(0, 0, 10, 10, "bow", sprites.get(0), 15, 0.8, 10);
		weapons[4] = new Weapon(0, 0, 10, 10, "buckshot", sprites.get(0), 10, 1, 10);
		selectedMapIndex = 0;

		// Creates the array that stores the premade maps
		preMadeMaps = new String[3];

		// Assigning premade maps to the array
		preMadeMaps[0] = "1-1.txt";
		preMadeMaps[1] = "1-2.txt";
		preMadeMaps[2] = "1-3.txt";

		
		// Initializes Map
		mapInit("maps/" + preMadeMaps[0]);

		EventQueue.invokeLater(() ->

		{
			Game gameInstance = new Game();
			gameInstance.setVisible(true);
		});
	}

	/**mapInit 
	 * This method initializes the map
	 */
	public static void mapInit(String file) {

		enemyList.clear();
		; // Clears the enemies to prevent overlapping mobs on game restart
		healthPacks.clear();
		; // Clears the healthpacks to prevent overlapping mobs on game restart

		char[][] tempMap = null; // Initializes map variable

		// Try and Catch for getting the map file
		try {
			tempMap = getMap(file); // Asising the temp map to the map in the file
			System.out.println("Map file acessed sucessfully");
		} catch (Exception e) {

			System.out.println("Map file failed to be acessed");
		}

		map = new Environment[tempMap.length][tempMap[0].length]; // Creates a map variable with the same size as
																	// tempMap

		// Assings the size of the world
		worldSizeX = 32 * tempMap[0].length + 16; // 32 is the length of one block, and 16 is an offset to make it
													// display properly
		worldSizeY = 32 * tempMap.length + 32;

		// Loops through the 2d tempMap array
		for (int i = 0; i < tempMap.length; i++) {
			for (int j = 0; j < tempMap[0].length; j++) {
				switch (tempMap[i][j]) {
				case 'w': // Creates a Wall object
					if (Math.random() >= 0.75) { // There is a 25% chance that the wall will be mossy, 75% chance it
													// will be stone brick
						map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(1));
					} else {
						map[i][j] = new Wall((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "wall", sprites.get(0));
					}
					break;

				case 'b': // Creates a win block object
					map[i][j] = new WinBlock((int) j * 32 + 32 / 2, (int) i * 32 + 32 / 2, "winblock", sprites.get(0));
					break;

				case 's': // Creates a skelton enemey
					enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "skeleton",
							sprites.get(4), 100, weapons[3]));
					break;

				case 'S': // Creates an elite skselton enemey
					enemyList.add(new Skeleton((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "eliteSkeleton",
							sprites.get(6), 200, weapons[4]));
					break;

				case 'h': // Creates a health pack
					healthPacks.add(
							new HealthPack((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "HP", sprites.get(7)));
					break;

				case 'p': // Creates the player
					player = new Player((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "player", sprites.get(3),
							100, weapons[0]);
					break;

				case 'z': // creates a zombie
					enemyList.add(new Zombie((int) j * 32 + 28 / 2, (int) i * 32 + 28 / 2, 28, 28, "zombie",
							sprites.get(5), 100, weapons[1]));
					break;
				}
			}
		}
	}

	/**
	 * getMap 
	 * This method assigns the map files to a 2d char array
	 * @param file The file path for the map
	 * @return Our 2d char array map
	 */
	public static char[][] getMap(String file) throws Exception {

		File myFile = new File(file);// Creates a new file based on the specified location

		// Output to message to the user via the console
		System.out.println("Attempting to read data from file: " + file);

		// Create a Scanner and associate it with the file
		Scanner input = new Scanner(myFile);

		// These variables control row and col length for our map
		int x = input.nextInt();
		int y = input.nextInt();

		String line = ""; // Sting variable to store lines from the file

		input.nextLine(); // Flushes the Scanner

		char[][] map = new char[y][x];

		for (int i = 0; i < y; i++) { // Loops through the file (rows)

			line = input.nextLine(); // Gets every line of the map in the file

			for (int j = 0; j < x; j++) { // Loops through the column

				map[i][j] = line.charAt(j); // Assigns the chars from the line to map[][]

			}
		}

		return map; // Return map

	}

	/**
	 * mapOut This method outputs the text files of the player-made maps
	 */
	public void mapOut() throws FileNotFoundException {

		input.nextLine(); // Flushes Scanner

		System.out.print("\nPlease enter the name of the map: ");

		String fileName = input.nextLine();// Assign the filename of the outputed file

		File myFile = new File("maps/" + fileName); // Creates a new file located in the maps folder

		PrintWriter output = new PrintWriter(myFile); // PrinterWritter to make print the map into the file

		output.println(editorSizeX + " " + editorSizeY); // Printing the width and height of the map

		// For loop to print out each character of the map into the file
		for (int i = 0; i < editorSizeY; i++) {
			for (int j = 0; j < editorSizeX; j++) {
				output.print(editingMap[i][j]);
			}
			output.println();
		}

		output.close(); // Closes the Printerwritter and completing the file printing

		changeState(0); // Returns the user to the menu screen

	}

	/**
	 * animate Repaints the screen and calls methods based on the game state
	 */
	public void animate() {

		while (true) { // Game loop

			if (gameState == 1) { // If in game perform a game tick

				gameTick();

			} else if (gameState == 4) { // If in map editor, perform a map editor tick
				mapEditorTick();
			}

			this.repaint(); // update the screen
		}

	}

	/**
	 * GameAreaPanel.java 
	 * Creates and initializes a graphics panel
	 * @author Jonathan Cai, Ray Chen, Wajeeh Haider
	 * @version 1.0
	 * @since May 31st
	 */
	private class GameAreaPanel extends JPanel {

		/**
		 * paintComponent 
		 * This method draws the map		 * 
		 * @param Graphics g (used to draw to screen)
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // required
			setDoubleBuffered(true); // Allows us to use double buffered drawings

			// Calls the render methods based on gamestate
			if (gameState == 0) { // Menu state
				menuRender(g);

			} else if (gameState == 1) { // Game State
				gameRender(g);

			} else if (gameState == 2) { // Death State
				deathRender(g);

			} else if (gameState == 3) { // Win State
				winRender(g);

			} else if (gameState == 4) { // Map editor sate
				mapEditorRender(g);
			}
		}
	}

	//Intalizing~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~``
	
	/**deathInit 
	 * This method initializes the death screen
	 */
	public void deathInit() {
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * menuInit 
	 * This method initializes the menu screen
	 */
	public void menuInit() {

		player.setHealth(100);

		setCursor(Cursor.getDefaultCursor());
	}

	 /**
	   * winInit
	   * This method initializes the win screen
	   */
	public void winInit() {
		setCursor(Cursor.getDefaultCursor());
	}

	 /**
	   * gameInit
	   * This method initializes the game
	   */
	public void gameInit() {

		mapInit("maps/" + mapName); //Initializes map

		player.setHealth(100);//Resets Health
		
		lastShot = System.nanoTime(); //Finds the delay between shots (cooldown time between shots)

		 //Camera
		offsetXMax = worldSizeX - 1280; //Max offset = worldsizeX - resolutionX
		offsetYMax = worldSizeY - 720; //Max offset = worldsizeY - resolutionY
		offsetXMin = 0; //Min x offset
		offsetYMin = 0;//Min y offset

		//Sets the cursor to a crosshair
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("images/crosshair.png").getImage(),
				new Point(16, 16), "crosshair"));
	}

	/**
	   * mapEditorInit
	   * This method initializes the map editor
	   */
	public void mapEditorInit() {
		
		//Getting world dimensions from the user
		System.out.print("\nMap editor:\nPlease enter the width of the map in tiles: ");
		editorSizeX = input.nextInt();
		System.out.print("\nPlease enter the height of the map in tiles: ");
		editorSizeY = input.nextInt();

		//Creating the editingMap
		editingMap = new char[editorSizeY][editorSizeX];

		paintBrush = 0;

		//Setting camera offset to be at 0,0
		offsetX = 0;
		offsetY = 0;

		//fils the map with empty characters
		for (int i = 0; i < editorSizeY; i++) {
			for (int j = 0; j < editorSizeX; j++) {
				editingMap[i][j] = ' ';

			}
		}
	}

	// Updating ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	 /**
	   * gameTick
	   * This method triggers 60x second (60fps) and controls basic game mechanics
	   */
	public void gameTick() {

		 //If health equal to or less than 0, trigger the loss screen
		if (player.getHealth() <= 0) {
			changeState(2);
		}

		//Assign the camera offset to the player position
		offsetX = player.getX() - 1280 / 2;
		offsetY = player.getY() - 720 / 2;

		//Prevents the offset from being smaller than the min or larger than the max
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

		//Check if both the player is shooting and there has a long enough time until the weapon can fire again
		if ((shooting) && (System.nanoTime() - lastShot >= 1e9 * player.getWeapon().getFireRate())) {
			player.shoot(mouseX + offsetX, mouseY + offsetY, sprites.get(0)); 
			lastShot = System.nanoTime(); //assiging the last time the player has shot as now
		}

		//Player movment 
		player.movement(up, down, left, right, enemyList, map);

		//Player's projecetile movment 
		player.moveProjectile();

		// Enviroment collisons
		//Loops through the map array
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {

				if (map[i][j] != null) { //Only runs when map != null (avoids an error)

					if (map[i][j].getName().equals("wall")) { //Checks if the players projectile has hit a wall, deletes if it has
						map[i][j].playerHit(player);
					} else if (map[i][j].getName().equals("winblock")) { //Checks for a win block
						if (map[i][j].playerWin(player)) {//If the player is on the win block, trigger the win state
							changeState(3);
						}
					}

				}

			}
		}

		// Enemy
		for (int i = 0; i < enemyList.size(); i++) { //Loops through the enemy arraylist

			if (!enemyList.get(i).getName().equals("zombie")) { //If the enemy isn't a zombie
		        ((Skeleton) enemyList.get(i)).moveProjectile(); //Move the enemy projectile
		      }

			//Checks if an enemy bullet has hit a wall
		      for (int a = 0; a < map.length; a++) { //Loops through the map
		        
		        for (int b = 0; b < map[0].length; b++) { //Loops through the map
		          
		          if (map[a][b] != null) { 
		            
		            if (map[a][b].getName().equals("wall")) { //If the enemy bullet hits a wall, delete it
		              map[a][b].enemyHit(enemyList.get(i));
		            }
		            
		          }
		          
		        }
		      }

		      //Player
		      if (player.wasHit(enemyList.get(i))) { //If player was hit
		        player.setHealth(player.getHealth() - enemyList.get(i).getWeapon().getDamage()); //Set player health to playerHealth - enemy damage
		      }

		      enemyList.get(i).getHit(player); //Checks if enemy was hit by player
		      
		      if ((enemyList.get(i)).getHealth() <= 0) { //If enemy health == 0, delete the enemy
		        enemyList.remove(i);
		        break;
		      }
		      
		      if (player.getAggro().intersects(enemyList.get(i).getCollision())) { //Checks if the player is in enemy radius
		        
		        if (!enemyList.get(i).getName().equals("zombie")) { //If the player is in radius of a skeleton or mega skeleton, it will shoot the player
		          (enemyList.get(i)).attack(player);
		          
		        } else { //If the player is in radius of a zombie, it will go towards the player
		          ((Zombie) enemyList.get(i)).move(player, map, enemyList);
		        }
		        
		      }
		      
		    }

		// HealthPack
		for (int i = 0; i < healthPacks.size(); i++) { //Loops through health pack arraylist
		      
		      if ((healthPacks.get(i)).checkCollision(player)) { //Checks if the player collides with a healthpack
		        
		        if (player.getHealth() >= 50) { //If your health is >50, set health to 100 (health packs heal 50, and 100 hp is the max), prevents going over max hp
		          player.setHealth(100);
		          
		        } else { //Else if health < 50, heal by 50 
		          player.setHealth(player.getHealth() + 50);
		        }
		        
		        healthPacks.remove(i); //Remove the healthpack that was collided with
		        break;
		      }
		      
		    }

		//Makes it so the game ticks at a certain rate (60 = 60fps)
		try {
			Thread.sleep(16); // 16 = 60fps, 33 = 30fps
		} catch (Exception exc) {

		} // delay

	}

	/**
	   * mapEditorTick
	   * This method triggers 60x second (60fps) and updates the map editor
	   */
	public void mapEditorTick() {

		//movment of the camera
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

		//Finding the hypotenuse
		double hyp = Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2));

		//normalizing the vector so that the camera travels at a constant speed
		if (hyp != 0) {

			offsetX += ((xMove / hyp) * 5);

			offsetY -= ((yMove / hyp) * 5);
		}

		//The current tile the user has its mouse over
		int tileX = (int) ((mouseX + offsetX) / 32);
		int tileY = (int) ((mouseY + offsetY) / 32);

		//If the user is both holding the mouse and is over a tile
		if ((shooting) && (tileX < editorSizeX && tileX >= 0) && (tileY < editorSizeY && tileY >= 0)) {
			switch (paintBrush) {
			case 2: // Add a floor to the map
				editingMap[tileY][tileX] = ' ';
				break;
			case 0: // Add a Wall to the map
				editingMap[tileY][tileX] = 'w';
				break;
			case 8: // Add a Win Block to the map
				editingMap[tileY][tileX] = 'b';
				break;
			case 3: // Add a Player to the map
				editingMap[tileY][tileX] = 'p';
				break;
			case 4: // Add a Skeleton to the map
				editingMap[tileY][tileX] = 's';
				break;
			case 5: // Add a Zombie to the map
				editingMap[tileY][tileX] = 'z';
				break;
			case 6: // Add a Elite Skeleton to the map
				editingMap[tileY][tileX] = 'S';
				break;
			case 7: // Add a Health Pack to the map
				editingMap[tileY][tileX] = 'h';
				break;

			}

		}

		//Makes it so the game ticks at a certain rate (60 = 60fps)
		try {
			Thread.sleep(16); // 16 = 60fps, 33 = 30fps
		} catch (Exception exc) {

		} // delay
	}

	// Rendering ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	   * menuRender
	   * This method renders the menu
	   * @param g Used to render graphics
	   */
	public void menuRender(Graphics g) {

		g.drawImage(img, 0, 0, null); //Draws the background image

		g.setColor(Color.BLACK); //set colour to black for the outline
		
		//Draws the outlines of buttons
		g.drawRect(640 - 50, 320 + 20, 100, 40);
		g.drawRect(640 - 50, 370 + 20, 100, 40);
		g.drawRect(640 - 50, 420 + 20, 100, 40);
		g.drawRect(640 - 50, 470 + 20, 100, 40);
		g.drawRect(640 - 50, 520 + 20, 100, 40);
 
		g.setColor(Color.GREEN); //set colour to green for the text
		g.setFont(title);
		drawCenteredString("Java Knight", 1280, 720, g); //draw title

		g.setFont(subTitle);
		
		//Draws string for each of the options
		drawCenteredString("Play", 1280, 820, g);
		drawCenteredString(preMadeMaps[selectedMapIndex], 1280, 920, g); //displays the current map selected
		drawCenteredString("Map editor", 1280, 1020, g);
		drawCenteredString("Load map", 1280, 1120, g);
		drawCenteredString("Quit", 1280, 1220, g);
	}

	/**
	   * deathRender
	   * This method renders a screen when you die
	   * @param g Used to render graphics
	   */
	public void deathRender(Graphics g) {

		g.setFont(title); //Sets the font to title
	    drawCenteredString("Game Over", 1280, 720, g); //Draws a large 'game over!' text (centered in the middle)
	    
	    g.setFont(subTitle); //Sets the font to subtitle
	    
	    //Draws the boxes and string for options
	    drawCenteredString("Menu", 1280, 820, g);
	    g.drawRect(640 - 50, 320 + 20, 100, 40);
	    
	    drawCenteredString("Quit", 1280, 920, g);
	    g.drawRect(640 - 50, 370 + 20, 100, 40);

	}

	  /**
	   * winRender
	   * This method renders a screen when you win
	   * @param g Used to render graphics
	   */

	public void winRender(Graphics g) {

		g.setFont(title); //Sets the font to title
	    drawCenteredString("You Win!!!", 1280, 720, g); //Draws a large win text (centered in the middle)
	    
	    g.setFont(subTitle); //Sets the font to subtitle
	    
	    //Draws the boxes and string for options
	    drawCenteredString("Menu", 1280, 820, g);
	    g.drawRect(640 - 50, 320 + 20, 100, 40);
	    
	    drawCenteredString("Quit", 1280, 920, g);
	    g.drawRect(640 - 50, 370 + 20, 100, 40);

	}

	/**
	   * gameRender
	   * This method renders the screen when you play
	   * @param g Used to render graphics
	   */
	public void gameRender(Graphics g) {
		
		//The camera offset works by drawing the positions of the objects subtracted by the offset.
		//This means that if the camera has moved 200 units to the right. Any object between 0,0 and 200 will be drawn offscreen.
		
		//Loops through the map
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == null) { //If the map is null (no information/floor of map) draw a floor tile
					g.drawImage(sprites.get(2), (int) (j * 32 - offsetX), (int) (i * 32 - offsetY), null);
				} else { //If it's not null (floor tile) we call the .draw method to draw player, enemy etc
					map[i][j].draw(g, offsetX, offsetY);
				}
			}
		}

		 //Draws a laserbeam, at the crosshair (lasersight)
		player.laserBeam(g, (int) (mouseX + offsetX), (int) (mouseY + offsetY), offsetX, offsetY);

		 //Draws the player
	    player.draw(g, offsetX, offsetY);
	    
	    //Draws the playr projectile
	    player.drawPlayerProjectile(g, offsetX, offsetY);

	  //Loops through the health pack arraylist
	    for (int j = 0; j < healthPacks.size(); j++) {
	      healthPacks.get(j).draw(g, offsetX, offsetY); //Draws the healthpacks
	    }
	    
	    //Loops through the enemy arraylist
	    for (int i = 0; i < enemyList.size(); i++) {
	      (enemyList.get(i)).draw(g, offsetX, offsetY); //Draws the enemy
	      
	      if (!enemyList.get(i).getName().equals("zombie")) { //If the enemy isn't a zombie, draw its projectile
	        ((Skeleton) enemyList.get(i)).drawEnemyProjectile(g, offsetX, offsetY);
	      }
	      
	    }

	  //Draws the health above the player
	    g.setColor(Color.BLACK); //Sets colour to black
	    g.fillRect((int) (player.getX() - offsetX - 13), (int) (player.getY() - offsetY - 35), 32, 17); //Draws the box in which health amount is contained in
	    
	    g.setFont(health); //Changes to the health font
	    g.setColor(Color.RED); //Sets colour to red
	    g.drawString("" + (int) player.getHealth(), (int) (player.getX() - offsetX - 13), //Draws the health string at player location - offset etc
	                 (int) (player.getY() - offsetY - 20));
	    
	    //Draws the player weapon beside the crosshair
	    g.drawImage(player.getWeapon().getSprite(), (int) (mouseX - player.getWeapon().getWidth() / 2 ),
	                (int) (mouseY - player.getWeapon().getHeight() / 2 - 30 ), null);
	    
	}

	 /**
	   * mapEditorRender
	   * This method renders the map editor
	   * @param g Used to render graphics
	   */
	public void mapEditorRender(Graphics g) {
		//variable used to tie the objects to their sprite
		int tempInt = 0;
		
		//Looping through the map
		for (int i = 0; i < editingMap.length; i++) {
			for (int j = 0; j < editingMap[0].length; j++) {
				if (editingMap[i][j] == 'b') { //If its a win block draw a yellow square as win block doesnt have a sprite
					g.setColor(Color.YELLOW);
					g.fillRect((int) (32 * j - offsetX), (int) (32 * i - offsetY), 32, 32);
				} else {
					//assing the tempInt depending on the object at that tile
					switch (editingMap[i][j]) {
					case 'w': //Wall
						tempInt = 0;
						break;
					case ' ': //Floor
						tempInt = 2;
						break;
					case 's': //Skeleton
						tempInt = 4;
						break;
					case 'z': //Zombie
						tempInt = 5;
						break;
					case 'S': //Elite Skeleton
						tempInt = 6;
						break;
					case 'h': //Health pack
						tempInt = 7;
						g.drawImage(sprites.get(2), (int) (32 * j - offsetX), (int) (32 * i - offsetY), null); // Drawing the floor under the healthpack
						break;
					case 'p': //Player
						tempInt = 3;
						break;
					}

					//Draw the sprite of the object
					g.drawImage(sprites.get(tempInt), (int) (32 * j - offsetX), (int) (32 * i - offsetY), null);
				}
			}
		}

		//Drawing the border for the UI
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 380, 70);

		//Drawing the currently selected block
		g.setColor(Color.BLACK);
		g.fillRect(8, 8, 36, 36);
		if (paintBrush == 8) { //If selected block is the win block
			g.setColor(Color.YELLOW);
			g.fillRect(10, 10, 32, 32);
		} else { //else, draw the sprite of the object
			g.drawImage(sprites.get(paintBrush), 10, 10, null);
		}
		
		//Drawing the key for the map editor
		
		//Drawing the images of the object
		g.drawImage(sprites.get(0), 60, 10, null); //Wall
		g.drawImage(sprites.get(2), 100, 10, null); //floor
		g.setColor(Color.YELLOW); 
		g.fillRect(140, 10, 32, 32); //win block
		g.drawImage(sprites.get(3), 180, 10, null); // Player
		g.drawImage(sprites.get(4), 220, 10, null); // skeleton
		g.drawImage(sprites.get(5), 260, 10, null); // zombie
		g.drawImage(sprites.get(6), 300, 10, null); // elite skelton
		g.drawImage(sprites.get(7), 340, 10, null); // health pack

		//Drawing the numbers that the user has to press to select they represnt
		g.setColor(Color.BLACK);
		g.setFont(subTitle);
		g.drawString("1     2    3     4     5     6    7     8", 70, 63);

	}

	/**
	   * changeState
	   * This method changes the game state
	   * @param state The state specified by the player
	   */
	public void changeState(int state) {

	    gameState = state; //sets the class variable state to the state specified by the player
	    
	    if (gameState == 0) { //If state == menu, initialize menu
	      menuInit();
	      
	    } else if (gameState == 1) { //If state == level 1/level 2/level 3, initialize game
	      gameInit();
	      
	    } else if (gameState == 2) { //If state == death, initialize the death/loss screen
	      deathInit();
	      
	    } else if (gameState == 3) { //If state == win, initialize the win screen
	      winInit(); 
	    } else if (gameState == 4) { //If state == map editor, initialize the map editor screen
	      mapEditorInit();
	    }

	}

	  /**
	   * drawCenteredString
	   * This method draws centered string in java swing, based on specified text, width, and height
	   * @param text The text to be drawn
	   * @param width The width of the screen or rect that you would like to centre text in
	   * @param height The height of the screen or rect that you would like to centre text in
	   * @param g Draws to the screen
	   */
	public void drawCenteredString(String text, int width, int height, Graphics g) {
	    FontMetrics fontMetrics = g.getFontMetrics(); //Initializes the font metrics varialbe (contains info about text)
	    
	    int x = (width - fontMetrics.stringWidth(text)) / 2; //The x of the center = width - font width
	    int y = (fontMetrics.getAscent() + (height - (fontMetrics.getAscent() + fontMetrics.getDescent())) / 2) - 50; //The y of the center = ascent + (height - (ascent + descent) )
	    
	    g.drawString(text, x, y); //Draws the string at the calculated location
	  }

	/** Listens and reacts based on keyboard input
	    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
	    * @version 1.0
	    * @since May 31st
	    */
	  class MyKeyListener implements KeyListener {

		  /**
		     * keyTyped
		     * This method does stuff when certain keys are typed
		     * @param e A keyEvent
		     */
		public void keyTyped(KeyEvent e) {

			if (gameState == 0) {

				if ((e.getKeyChar() == 'a') && (selectedMapIndex > 0)) {
					selectedMapIndex--;
				}

				if ((e.getKeyChar() == 'd') && (selectedMapIndex < preMadeMaps.length - 1)) {
					selectedMapIndex++;
				}
			} else if (gameState == 1) {

				if (e.getKeyChar() == '1') { //Player selected pistol
					player.setWeapon(weapons[0]);
					
				} else if (e.getKeyChar() == '2') { //Player selected smg
					player.setWeapon(weapons[1]);
					
				} else if (e.getKeyChar() == '3') { //Player selected shotgun
					player.setWeapon(weapons[2]);
				}
			}

		}

		 /**
	     * keyPressed
	     * This method does stuff when certain keys are pressed
	     * @param e A keyEvent
	     */
		public void keyPressed(KeyEvent e) {

			 if (e.getKeyCode() == 'W') { //If the player hits/holds 'W', move up until they stop
			        up = true;
			      }
			      
			      if (e.getKeyCode() == 'S') { //If the player hits/holds 'S', move down until they stop
			        down = true;
			      }
			      
			      if (e.getKeyCode() == 'A') { //If the player hits/holds 'A', move left until they stop
			        left = true;
			      }
			      
			      if (e.getKeyCode() == 'D') { //If the player hits/holds 'D', move right until they stop
			        right = true;
			      }

			if (gameState == 4) {
				switch (e.getKeyCode()) { //Assing paintbrush in map editor
				case '1': // Wall
					paintBrush = 0;
					break;
				case '2': // Nothing
					paintBrush = 2;
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

				if (e.getKeyCode() == KeyEvent.VK_ENTER) { //If user pressed enter, print out map into file
					try {
						mapOut();
					} catch (FileNotFoundException e1) {

					}
				}

			}
		}

		  /**
	     * keyReleased
	     * This method does stuff when certain keys are released
	     * @param e A keyEvent
	     */
		public void keyReleased(KeyEvent e) {

			 if (e.getKeyCode() == 'W') { //If the player lets go of 'W', stop moving up
		        up = false;
		      }
		      
		      if (e.getKeyCode() == 'S') { //If the player lets go of 'S', stop moving down
		        down = false;
		      }
		      
		      if (e.getKeyCode() == 'A') { //If the player lets go of 'A', stop moving left
		        left = false;
		      }
		      
		      if (e.getKeyCode() == 'D') { //If the player lets go of 'D', stop moving right
		        right = false;
		      }
			      
	    }

	}

	  /** Listens and reacts based on mouse motion
	    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
	    * @version 1.0
	    * @since May 31st
	    */
	class MyMouseMotionListener implements MouseMotionListener {

		/**
	     * mouseDragged
	     * This method does stuff when you drag the mouse
	     * @param e A mouse event
	     */
	    public void mouseDragged(MouseEvent e) { //Update the mouseposition based on the cursor's position and an offset to increase accuracy
	      mouseX = e.getX() - 7;
	      mouseY = e.getY() - 30;
	    }
	    
	    /**
	     * mouseMoved
	     * This method does stuff when you move the mouse
	     * @param e A mouse event
	     */
	    public void mouseMoved(MouseEvent e) { //Update the mouseposition based on the cursor's position and an offset to increase accuracy
	      mouseX = e.getX() - 7;
	      mouseY = e.getY() - 30;
	      
	    }

	}

	   /** MyMouseListener
	    * Listens and reacts based on mouse clicks
	    * @author Jonathan Cai, Ray Chen, Wajeeh Haider
	    * @version 1.0
	    * @since May 31st
	    */
	class MyMouseListener implements MouseListener {

		 /**
	     * mouseClicked
	     * This method does stuff when you click the mouse
	     * @param e A mouse event
	     */
		public void mouseClicked(MouseEvent e) {

		}

		 /**
	     * mousePressed
	     * This method does stuff when you press the mouse
	     * @param e A mouse event
	     */
		public void mousePressed(MouseEvent e) {

			// temporary variables just in case the mouse has to be offset
			int tempX = e.getX();
			int tempY = e.getY();

			// menu buttons
			if ((gameState == 0) && (tempX >= 590) && (tempX <= 690)) { //Check if in the menu screen and the mouse position is in the collumn where all the buttons are located
				if ((tempY >= 370) && (tempY <= 410)) { //Pressed play
					mapName = preMadeMaps[selectedMapIndex];
					changeState(1);

				} else if ((tempY >= 460) && (tempY <= 500)) { //Pressed Map editor
					changeState(4);

				} else if ((tempY >= 510) && (tempY <= 550)) { //Pressed load map
					System.out.print("\nPlease enter the nane of the map file: ");
					mapName = input.nextLine();
					changeState(1);

				} else if ((tempY >= 560) && (tempY <= 600)) { //Pressed quit
					System.exit(0);
				}

			} else if ((gameState == 1) || (gameState == 4)) { //Check if the player is holding the mouse button down
				shooting = true;

				// death screen buttons
			} else if (((gameState == 2)||(gameState == 3)) && (tempX >= 590) && (tempX <= 690)) { //Check if in the death or win screen as they have the same button positions

				if ((tempY >= 340) && (tempY <= 410)) { //Pressed menu
					changeState(0);
				} else if ((tempY >= 370) && (tempY <= 460)) { // Pressed quit
					System.exit(0);
				}
			}
		}
		
		/**
	     * mouseReleased
	     * This method does stuff when you release the mouse
	     * @param e A mouse event
	     */
		public void mouseReleased(MouseEvent e) {
			if ((gameState == 1) || (gameState == 4)) {
				shooting = false;
			}

		}

		 /**
	     * mouseEntered
	     * This method does stuff when you move the mouse onto the window
	     * @param e A mouse event
	     */
		public void mouseEntered(MouseEvent e) {

		}

		/**
	     * mouseExited
	     * This method does stuff when you move the mouse off the window
	     * @param e A mouse event
	     */
		public void mouseExited(MouseEvent e) {

		}

	}

	/****** Mouse Listener *********************************/
}
