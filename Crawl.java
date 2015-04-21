import javalib.funworld.*;
import javalib.worldimages.*;
import javalib.worldcanvas.*;

//THINGS TO BE REPRESENTED
//-player
//-enemies
//-spells/weapons
//-room
//-floor
//-dungeon

interface Collidable {

    int getX();

    int getY();

    int getWidth();
    
    int getHeight();
}

class Player implements Collidable{

    int x;
    int y;
    Posn position;
    int width;
    int height;
    int health;
    int mana;
    String imgName;
    EquipList inventory;
    Weapon weapon;

    public Player(int x, int y, int width, int height, int health, int mana,
		  String imgName, EquipList inventory, Weapon weapon) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.position = new Posn(x+width/2, y+height/2);
	this.health = health;
	this.mana = mana;
	this.imgName = imgName;
	this.inventory = inventory;
	this.weapon = weapon;
    }

    public int getX() {
	return this.x;
    }

    public int getY() {
	return this.y;
    }
    
    public Posn getPosition() {
	return this.position;
    }

    public int getWidth() {
	return this.width;
    }
    
    public int getHeight() {
	return this.height;
    }

    public int getHealth() {
	return this.health;
    }

    public int getMana() {
	return this.mana;
    }

    public String getImgName() {
	return this.imgName;
    }

    public EquipList getInventory() {
	return this.inventory;
    }

    public Weapon getWeapon() {
	return this.weapon;
    }
    
    public FromFileImage draw() {
	//Effect: returns a FromFileImage created using the
	//        Player's imgName
	return new FromFileImage(this.position, this.imgName);
    }

    public Player move(int dx, int dy) {
	//Effect: Adds dx and dy to the player's position
        return new Player (x+dx, y+dy, width, height, health,
			   mana, imgName, inventory, weapon);
    }

    public Player addItem(Carryable item) {
	//Effect: Adds the given spell if the player's spells are not already full,
	//        and does nothing if they are full
	if(inventory.size() >= 10) {
	    return this;
	}
	else {
	    return new Player(x, y, width, height, health, mana, imgName, inventory.add(item), weapon);
	}
    }

    public Player changeHealth(int newHealth) {
	//Effect: Change the player's health to the given value
        return new Player(x, y, width, height, newHealth, mana, imgName, inventory, weapon);
    }

    public Player changeMana(int newMana) {
	//Effect: Change the player's mana to the given value
        return new Player(x, y, width, height, health, mana, imgName, inventory, weapon);
    }

    public Player changeWeapon(Weapon newWeapon) {
	//Effect: Changes the Player's Weapon to the given weapon
        return new Player(x, y, width, height, health, mana, imgName, inventory, newWeapon);
    }

    public Player changeImage(String newImgName) {
	//Effect: Changes the Player's imgName to the given String
        return new Player(x, y, width, height, health, mana, newImgName, inventory, weapon);
    }
}

class Enemy {

}

interface Carryable {

}

interface Weapon extends Carryable {
    
}

interface Consumable extends Carryable {

}

interface EquipList {

    public int size();
    public Carryable first();
    public EquipList rest();
    public EquipList add(Carryable item);
    public EquipList remove(Carryable Item);
}

class EmptyListException extends RuntimeException {
    public EmptyListException(String message) {
	super();
    }
}

class EmptyList implements EquipList{

    public EmptyList() {}

    public int size() {
	return 0;
    }
    
    public Carryable first(){
	throw new EmptyListException("Tried to access an item from the empty EquipList");
    }

    public EquipList rest() {
	return this;
    }

    public EquipList add(Carryable item) {
	return new List(item, this);
    }

    public EquipList remove(Carryable item) {
	return this;
    }
}

class List implements EquipList{
    Carryable first;
    EquipList rest;

    public List(Carryable first, EquipList rest) {
	this.first = first;
	this.rest = rest;
    }

    public int size() {
	return 1 + rest.size();
    }

    public Carryable first() {
	return first;
    }

    public EquipList rest() {
	return rest;
    }

    public EquipList add(Carryable item) {
	return new List(first, rest.add(item));
    }

    public EquipList remove(Carryable item) {
	if(first.equals(item)) {
	    return rest;
	} else {
	    return new List(first, rest.remove(item));
	}
    }
}


//BSP DUNGEON GENERATION

class Room {
    int x;
    int y;
    int width;
    int height;
    Posn position;

    public Room(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.position = new Posn(x+width/2, y+height/2);
    }

    public Posn getPosn() {
	return position;
    }

    public boolean inRoom(Collidable c) {
	return c.getX() >= this.x && c.getX() <= this.x+this.width-c.getWidth() &&
	    c.getY() >= this.y && c.getY() <= this.y+this.height-c.getHeight();
    }
}


class Leaf {
    
    //x and y mark the top right corner of the Leaf
    int x;
    int y;
    int width;
    int height;
    //Is it bad to have these values left/right and room where only one ever
    //gets initialized in a given Leaf, and the other is null? Would it be
    //better to have Leaf by an interface implemented by classes like
    //LeftRightLeaf and RoomLeaf?
    Leaf left;
    Leaf right;
    Room room;
    Posn position;
    //These particular values will msot likely need to
    //be changed once I get a better idea of what size
    //everything will be
    static final int MIN_HEIGHT = 100;
    static final int MIN_WIDTH = 100;


    //A Leaf can have either be composed of two subleaves:
    public Leaf(int x, int y, int width, int height, Leaf left, Leaf right) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.left = left;
	this.right = right;
	this.position = new Posn(x+width/2, y+height/2);
    }

    //OR it can contain a Room
    public Leaf(int x, int y, int width, int height, Room room) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.room = room;
	this.position = new Posn(x+width/2, y+height/2);
    }

    //This constructor is just to be used in section() so that leaves can
    //be created to call section() on without having to know if they will
    //have subleaves or just a Room
    public Leaf(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.position = new Posn(x+width/2, y+height/2);
    }

    private Leaf partitionLR() {
	int vertLine = Math.max(MIN_WIDTH, (int) Math.random()*(width-MIN_WIDTH));
	Leaf newLeft = new Leaf(x, y, vertLine, height);
	Leaf newRight = new Leaf(x+vertLine, y, width - vertLine, height);
	return new Leaf(x, y, width, height, newLeft.section(), newRight.section());
    }

    private Leaf partitionTB() {
	int horzLine = Math.max(MIN_HEIGHT, (int) Math.random()*(height-MIN_HEIGHT));
	Leaf newTop = new Leaf(x, y, width, horzLine);
	Leaf newBottom = new Leaf(x, y+horzLine, width, height-horzLine);
	return new Leaf(x, y, width, height, newTop.section(), newBottom.section());
    }
    /**
     * Returns a new Leaf of the same size with either two sub-leaves or
     * a room
     * @return A new Leaf that has two sub-leaves if this is at least twice
     *         as large as the minimum room size, or one room if it is not
     */
    public Leaf section() {
	//If Leaf is large enough to hold two rooms, partition
	if(this.width >= MIN_WIDTH*2 && this.height >= MIN_HEIGHT*2) {
	    //If width is significantly greater than height, partition vertically
	    if(width >= height*1.5) {
		return this.partitionLR();
	    }
	    //If height is significantly greater than width, partition horizontally
	    else if(height >= width*1.5) {
		return this.partitionTB();
	    }
	    //If height and width are similar enough, choose partition randomly
	    else {
		if(Math.random() > .5) {
		    return this.partitionLR();
		} else {
		    return this.partitionTB();
		}
	    }
	    //Leaf is not large enough to hold two rooms, so just create
	    //a room in this one
	} else {
	    //Dimensions for room between 3/4ths the minimum size and the minimum size
	    int roomWidth = (int) Math.max(MIN_WIDTH * Math.random(), MIN_WIDTH * .75);
	    int roomHeight = (int) Math.max(MIN_HEIGHT * Math.random(), MIN_HEIGHT * .75);
	    int roomX = (int) this.x + (int) (Math.random() * (MIN_WIDTH - roomWidth));
	    int roomY = (int) this.y + (int) (Math.random() * (MIN_HEIGHT - roomHeight));
	    return new Leaf(x, y, width, height, new Room(roomX, roomY, roomWidth, roomHeight));
	}
    }
}

public class Crawl {

}

