/**
 * [Moveable.java]
 * Description: Interface for translating objects
 * @author Jonathan, Ray, Wajeeh
 * @version 1.0, May 10, 2021
 */

public interface Moveable{
  
  /**
   * moveLeft
   * method to shift the x coordinate of the object left by n
   * @param n, the amount to be shifted
   */
  public void moveLeft(double n);
  
  /**
   * moveRight
   * method to shift the x coordinate of the object right by n
   * @param n, the amount to be shifted
   */
  public void moveRight(double n);
  
  /**
   * moveUp
   * method to shift the y coordinate of the object up by n
   * @param n, the amount to be shifted
   */
  public void moveUp(double n);
  
  /**
   * moveDown
   * method to shift the y coordinate of the object down by n
   * @param n, the amount to be shifted
   */
  public void moveDown(double n);
  
}