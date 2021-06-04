import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Rectangle;

class Enemy extends Character{
  
  static ArrayList<Projectile> enemyProjectiles = new ArrayList<Projectile>(); 
  
  private Player player;
   
  public void drawEnemy(Graphics g){

    g.fillRect((int) getX() -getWidth()/2,(int) getY() -getHeight()/2, getWidth(), getHeight());  
  }
   
  /*
  public void getHit(Player player){
    
    for (int i = 0; i < (player.playerProjectiles).size(); i++){ 
      if ((player.playerProjectiles.get(i).getX() >= this.getX()-this.getWidth()) && (player.playerProjectiles.get(i).getX() <= this.getX()+this.getWidth()) && (player.playerProjectiles.get(i).getY() >= this.getY()-this.getHeight()) && (player.playerProjectiles.get(i).getY() <= this.getY()+this.getHeight())){
        
       this.setHealth(this.getHealth()-5);
       System.out.println("hit");
      }
    }
    
  }
  */
  
  public boolean getHit(Player player){
    
    for (int i = 0; i < (player.playerProjectiles).size(); i++){ 
      if ((player.playerProjectiles.get(i).getX() >= this.getX()-this.getWidth()) && (player.playerProjectiles.get(i).getX() <= this.getX()+this.getWidth()) && (player.playerProjectiles.get(i).getY() >= this.getY()-this.getHeight()) && (player.playerProjectiles.get(i).getY() <= this.getY()+this.getHeight())){
        
        System.out.println("hit");
        
        return true;
      }
    }
    
    return false;
  }
  
  public void changeHealth(){
    if (getHit(this.player) == true){
      this.setHealth(this.getHealth()-5);
    }
  }
    
  public void shoot(Player player){
    int playerX =(int) (player.getX());
    int playerY =(int) (player.getY());
    
    double xDifference = getX() - player.getX();
    double yDifference = getY() - player.getY();
    
    double hyp = Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2));

    double xChange = ((xDifference/hyp)*20);
    double yChange = ((yDifference/hyp)*20);
    
    enemyProjectiles.add(new Projectile(getX(), getY(), 25, 25, "Bullet", 20, xChange, yChange));
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
  
  Enemy(double x, double y, int width, int height, String name, double health, String weapon, Player player){
  //Enemy(int x, int y, int width, int height, BufferedImage sprite, String name, double health, String weapon, Player player){
    super(x, y, width, height, name, health, weapon);
    //super(x, y, width, height, sprite, name, health, weapon);
  }

}