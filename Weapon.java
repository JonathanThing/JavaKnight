class Weapon extends Items{

  //damage this weapon deals
  private double damage;

  //rate at which this weapon can be used
  private double fireRate;

  //range of the weapon
  private double range;

  //type of ammo
  String ammoType;

  //constructor 
  Weapon(int x, int y, int width, int height, String name, double damage, double fireRate, double range, String ammoType){
    super(x, y, width,  height, name);
    this.damage = damage;
    this.fireRate = fireRate;
    this.range = range;
    this.ammoType = ammoType;
  }

}