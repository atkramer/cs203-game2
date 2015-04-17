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

class Player {

    Posn position;
    int width;
    int height;
    int health;
    int mana;
    String imgName;
    EquipList inventory;
    Usable weapon;

    public Player(Posn position, int width, int height, int health, int mana,
		  String imgName, EquipList inventory, Usable weapon) {
	this.position = position;
	this.width = width;
	this.height = height;
	this.health = health;
	this.mana = mana;
	this.imgName = imgName;
	this.inventory = inventory;
	this.weapon = weapon;
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

    public Usable getWeapon() {
	return this.weapon;
    }
    
    public FromFileImage draw() {
	//Effect: returns a FromFileImage created using the
	//        Player's imgName
	return new FromFileImage(this.position, this.imgName);
    }
    
    public void move(Posn newPosition) {
	//Effect: Changes the Player's position to be newPosition
        return new Player(newPosition, width, height, health, mana, imgName, inventory, weapon);
    }

    public void move(int dx, int dy) {
	//Effect: Adds dx and dy to the player's position
        return new Player (new Posn(this.position.x + dx, this.position.y + dy), width, height, health,
			   mana, imgName, inventory, weapon);
    }

    public void addItem(Carryable item) {
	//Effect: Adds the given spell if the player's spells are not already full,
	//        and does nothing if they are full
	if(inventory.size() >= 10) {
	    return this;
	}
	else {
	    return new Player(position, width, height, health, mana, imgName, inventory.add(item), weapon);
	}
    }

    public void changeHealth(int newHealth) {
	//Effect: Change the player's health to the given value
        return new Player(position, width, height, newHealth, mana, imgName, inventory, weapon);
    }

    public void changeMana(int newMana) {
	//Effect: Change the player's mana to the given value
        return new Player(position, width, height, health, mana, imgName, inventory, weapon);
    }

    public void changeWeapon(Usable newWeapon) {
	//Effect: Changes the Player's Weapon to the given weapon
        return new Player(position, width, height, health, mana, imgName, inventory, newWeapon);
    }

    public void changeImage(String newImgName) {
	//Effect: Changes the Player's imgName to the given String
        return new Player(position, width, height, health, mana, newImgName, inventory, newWeapon);
    }
}

class Enemy {

}

interface Carryable {

}

interface Weapon extends Carryable {
    
}

interface Consumable extends Carrayble {

}

interface EquipList {

    public int size();
    public Carryable first();
    public EquipList rest();
    public EquipList add(Carryable item);
    public EquipList remove(Carryable Item);
}

class EmptyList {

    public EmptyList() {}

    public int size() {
	return 0;
    }

    public Carryable first(){}

    public EquipList rest() {
	return this;
    }

    public Equiplist add(Carryable item) {
	return new List(item, this);
    }

    public EquipList remove(Carryable item) {
	return this;
    }
}

class List {
    Carryable first;
    EquipList rest;

    public List(Carryable first, Equiplist rest) {
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
