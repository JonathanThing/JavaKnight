import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Enemy extends Character{
  
  static ArrayList<Projectile> enemyProjectiles = new ArrayList<Projectile>(); 
   
  public void drawEnemy(Graphics g){

    g.fillRect((int) getX(),(int) getY(), getWidth(), getHeight());  
  }
    
  public void getHit(Player player){
    
    for (int i = 0; i < (player.playerProjectiles).size(); i++){ 
      if (this.collisionRect.intersects(((player.playerProjectiles).get(i)).collisionRect)){
        System.out.println("hit");
      }
    }
    
  }
    
  public void shoot(Player player){
    int playerX =(int) (player.getX());
    int playerY =(int) (player.getY());
    
    int xDifference =(int) (getX() - player.getX());
    int yDifference =(int) (getY() - player.getY());
    
    int xChange = Math.round(xDifference/20);
    int yChange = Math.round(yDifference/20);
    
    enemyProjectiles.add(new Projectile(getX(), getY(), 50, 50, "Bullet", 20, xChange, yChange));
  }
  
  public void moveProjectile(){
    
    for (int i = 0; i < enemyProjectiles.size(); i++){
      
      (enemyProjectiles.get(i)).moveUp((enemyProjectiles.get(i)).getChangeY());
      (enemyProjectiles.get(i)).moveLeft((enemyProjectiles.get(i)).getChangeX());
      
    }
    
  }
  
  public void drawEnemyProjectile(Graphics g){
    for (int i = 0; i < enemyProjectiles.size(); i++){
      (enemyProjectiles.get(i)).drawProjectile(g);
    }
  }
  
  Enemy(int x, int y, int width, int height, String name, double health, String weapon, Player player){
  //Enemy(int x, int y, int width, int height, BufferedImage sprite, String name, double health, String weapon, Player player){
    super(x, y, width, height, name, health, weapon);
    //super(x, y, width, height, sprite, name, health, weapon);
  }

}