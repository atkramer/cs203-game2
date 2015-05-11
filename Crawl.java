import javalib.funworld.*;
import javalib.worldimages.*;
import javalib.worldcanvas.*;
import java.awt.Color;

//TODO
//Make generic list to replace equiplist/pointlist/enemylist?

class Point extends Posn {

    public Point(int x, int y) {
	super(x, y);
    }

    public boolean equals(Point p) {
	return this.x == p.x && this.y == p.y;
    }

    
    public String toString() {
	return "(" + x + "," + y + ")";
    }
    
    //For use with testers
    public boolean isPath(Point pt, Cell[][] cells) {
	SearchPoint sp = new SearchPoint(this, null);
	SearchList closed = new EmptySearchList();
	SearchList open = closed.add(sp);
	return sp.isPath(pt, cells, open, closed);
    }
}

//Point list to be used later for passing hallway locations to dungeons in tree
interface PointList {

    public boolean isEmpty();

    public Point first();

    public Point last();
    
    public PointList rest();

    public PointList add(Point pt);

    public PointList append(Point pt);
    
    public int size();

    public Point get(int k);

    public PointList leftOf(int xPt);

    public PointList rightOf(int xPt);
    
    public PointList above(int yPt);
    
    public PointList below(int yPt);
    
    public Point leftMost();

    public Point rightMost();

    public Point topMost();

    public Point bottomMost();

    public PointList removeAtXBetween(int x, int lowY, int highY);

    public PointList removeAtYBetween(int y, int lowX, int highX);
}

class EmptyPtList implements PointList{
    public EmptyPtList() { }

    public boolean isEmpty() {
	return true;
    }

    public Point first() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public Point last() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public PointList rest() {
	return this;
    }
    
    public PointList add(Point pt) { 
	return new ConsPtList(pt, this);
    }

    public PointList append(Point pt) {
	return new ConsPtList(pt, this);
    }

    public int size() {return 0;}
    
    public Point get(int k) {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public PointList leftOf(int xPt) {
	return this;
    }

    public PointList rightOf(int xPt) {
	return this;
    }

    public PointList above(int yPt) {
	return this;
    }

    public PointList below(int yPt) {
	return this;
    }
    
    public Point leftMost() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public Point rightMost() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public Point topMost() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public Point bottomMost() {
	throw new EmptyListException("Can't take an item from an empty list");
    }

    public PointList removeAtXBetween(int x, int lowY, int highY) {
	return this;
    }

    public PointList removeAtYBetween(int y, int lowX, int highX) {
	return this;
    }

    public String toString() {
	return "";
    }
}

class ConsPtList implements PointList{
    Point first;
    PointList rest;

    public ConsPtList(Point first, PointList rest) {
	this.first = first;
	this.rest = rest;
    }

    public boolean isEmpty() {
	return false;
    }

    public Point first() {
	return first;
    }

    public Point last() {
	if(rest.isEmpty()) {
	    return first();
	} else {
	    return rest.last();
	}
    }

    public PointList rest() {
	return rest;
    }

    public PointList add(Point pt) {
	return new ConsPtList(pt, this);
    }

    public PointList append(Point pt) {
	    return new ConsPtList(first, rest.append(pt));
    }

    public int size() {
	return 1 + rest.size();
    }

    public Point get(int k) {
	if(k == 0) {
	    return first;
	}
	else {
	    return rest.get(k - 1);
	}
    }

    public PointList leftOf(int xPt) {
	if(first.x <= xPt)
	    return rest.leftOf(xPt).add(first);
	else
	    return rest.leftOf(xPt);
    }

    public PointList rightOf(int xPt) {
	if(first.x >= xPt)
	    return rest.rightOf(xPt).add(first);
	else
	    return rest.rightOf(xPt);
    }

    public PointList above(int yPt) {
	if(first.y <= yPt)
	    return rest.above(yPt).add(first);
	else
	    return rest.above(yPt);
    }

    public PointList below(int yPt) {
	if(first.y >= yPt)
	    return rest.below(yPt).add(first);
	else
	    return rest.below(yPt);
    }
    
    private Point leftMostAcc(Point pt) {
	if(rest.isEmpty()) 
	    if(first.x < pt.x)
		return first;
	    else
		return pt;
	else if(first.x < pt.x) 
	    return ((ConsPtList) rest).leftMostAcc(first);
	else
	    return ((ConsPtList) rest).leftMostAcc(pt);
    }

    public Point leftMost() {
	if(rest.isEmpty())
	    return first;
	else
	    return leftMostAcc(first);
    }

    private Point rightMostAcc(Point pt) {
	if(rest.isEmpty()) 
	    if(first.x > pt.x)
		return first;
	    else
		return pt;
	else if(first.x > pt.x) 
	    return ((ConsPtList) rest).rightMostAcc(first);
	else
	    return ((ConsPtList) rest).rightMostAcc(pt);
    }

    public Point rightMost() {
	if(rest.isEmpty())
	    return first;
	else
	    return rightMostAcc(first);
    }

    private Point topMostAcc(Point pt) {
	if(rest.isEmpty()) 
	    if(first.y < pt.y)
		return first;
	    else
		return pt;
	else if(first.y < pt.y) 
	    return ((ConsPtList) rest).topMostAcc(first);
	else
	    return ((ConsPtList) rest).topMostAcc(pt);
    }

    public Point topMost() {
	if(rest.isEmpty())
	    return first;
	else
	    return topMostAcc(first);
    }

    private Point bottomMostAcc(Point pt) {
	if(rest.isEmpty()) 
	    if(first.y > pt.y)
		return first;
	    else
		return pt;
	else if(first.y > pt.y) 
	    return ((ConsPtList) rest).bottomMostAcc(first);
	else
	    return ((ConsPtList) rest).bottomMostAcc(pt);
    }

    public Point bottomMost() {
	if(rest.isEmpty())
	    return first;
	else
	    return bottomMostAcc(first);
    }
    public String toString() {
	return "(" + first + " " + rest + ")";
    }

    public PointList removeAtXBetween(int x, int lowY, int highY) {
	if(this.first.x == x && lowY < this.first.y && this.first.y < highY) {
	    return rest.removeAtXBetween(x, lowY, highY);
	}
	else {
	    return new ConsPtList(first, rest.removeAtXBetween(x, lowY, highY));
	}
    }

    public PointList removeAtYBetween(int y, int lowX, int highX) {
	if(this.first.y== y && lowX < this.first.x && this.first.x < highX) {
	    return rest.removeAtYBetween(y, lowX, highX);
	}
	else {
	    return new ConsPtList(first, rest.removeAtYBetween(y, lowX, highX));
	}
    }
}

interface Collidable {

    Point getLocation();
}

class NegativeManaException extends Exception {
    String message;

    public NegativeManaException(String message) {
	super(message);
    }
}

class InventoryFullException extends Exception {
    String message;

    public InventoryFullException(String message) {
	super(message);
    }
}

class Player implements Collidable{

    //x and y used to identify current cell
    Point location;
    Posn position;
    Direction direction;
    int maxHealth;
    int health;
    int maxMana;
    int mana;
    String imgName;
    EquipList inventory;
    Weapon weapon;

    public Player(Point location, Direction direction, int maxHealth,
		  int health, int maxMana, int mana,
		  String imgName, EquipList inventory, Weapon weapon) {
	this.location = location;
	this.direction = direction;
	this.position = new Posn(location.x*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, location.y*Cell.CELL_HEIGHT+Cell.CELL_WIDTH/2);
	this.maxHealth = maxHealth;
	this.health = health;
	this.maxMana = maxMana;
	this.mana = mana;
	this.imgName = imgName;
	this.inventory = inventory;
	this.weapon = weapon;
    }

    public Point getLocation() {
	return this.location;
    }

    public Direction getDirection() {
	return this.direction;
    }
    
    public Posn getPosition() {
	return this.position;
    }

    public int getMaxHealth() {
	return this.maxHealth;
    }

    public int getHealth() {
	return this.health;
    }

    public int getMaxMana() {
	return this.maxMana;
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
    
    public WorldImage draw() {
	//Effect: returns a FromFileImage created using the
	//        Player's imgName
	if(direction == Direction.LEFT) {
	    return new FromFileImage(this.position, "images/player-left.png");
	} else if (direction == Direction.RIGHT) {
	    return new FromFileImage(this.position, "images/player-right.png");
	} else if (direction == Direction.UP) {
	    return new FromFileImage(this.position, "images/player-up.png");
	} else {
	    return new FromFileImage(this.position, "images/player-down.png");
	}
	//return new DiskImage(position, Cell.CELL_WIDTH/2, Color.BLUE);
    }

    public Player move(int dx, int dy) {
	//Effect: Adds dx and dy to the player's position
	Direction tempDir;
	String tempImgName;
	if(dx < 0) {
	    tempDir = Direction.LEFT;
	    tempImgName = "images/player-turned.png";
	} else if(dx > 0) {
	    tempDir = Direction.RIGHT;
	    tempImgName = "images/player-turned.png";
	} else if(dy < 0) {
	    tempDir = Direction.UP;
	    tempImgName = "images/player-straight.png";
	} else if(dy > 0) {
	    tempDir = Direction.DOWN;
	    tempImgName = "images/player-straight.png";
	} else {
	    tempDir = direction;
	    tempImgName = imgName;
	}
        return new Player (new Point(location.x + dx, location.y + dy), tempDir, maxHealth, health,
			   maxMana, mana, tempImgName, inventory, weapon);
    }

    public Player move(Point pt) {
	Direction tempDir;
	String tempImgName;
	if(pt.x < location.x) {
	    tempDir = Direction.LEFT;
	    tempImgName = "images/player-turned.png";
	} else if(pt.x > location.x) {
	    tempDir = Direction.RIGHT;
	    tempImgName = "images/player-turned.png";
	} else if(pt.y < location.y) {
	    tempDir = Direction.UP;
	    tempImgName = "images/player-straight.png";
	} else if(pt.y > location.y) {
	    tempDir = Direction.DOWN;
	    tempImgName = "images/player-straight.png";
	} else {
	    tempDir = direction;
	    tempImgName = imgName;
	}
	return new Player(pt, tempDir, maxHealth, health, maxMana, mana, tempImgName, inventory, weapon);
    }

    public Player attack(Direction dir) throws NegativeManaException{
	if(mana-weapon.getManaCost() >= 0) {
	return new Player(location, dir, maxHealth, health, maxMana, mana-weapon.getManaCost(), imgName, inventory, weapon);
	} else {
	    throw new NegativeManaException("Not Enough Mana to Use That!");
	}
    }

    public boolean isDead() {
	return health <= 0;
    }

    public Player addItem(Carryable item) throws InventoryFullException {
	//Effect: Adds the given spell if the player's spells are not already full,
	//        and does nothing if they are full
	if(inventory.size() >= 10) {
	    throw new InventoryFullException("Inventory is full");
	}
	else {
	    return new Player(location, direction, maxHealth, health, maxMana, mana, imgName, inventory.add(item), weapon);
	}
    }

    public Player changeMaxHealth(int newMaxHealth) {
	return new Player(location, direction, newMaxHealth, health, maxMana, mana, imgName, inventory, weapon);
    }

    public Player changeHealth(int newHealth) {
	//Effect: Change the player's health to the given value
        return new Player(location, direction, maxHealth, newHealth, maxMana, mana, imgName, inventory, weapon);
    }

    public Player changeMaxMana(int newMaxMana) {
	return new Player(location, direction, maxHealth, health, newMaxMana, mana, imgName, inventory, weapon);
    }

    public Player changeMana(int newMana) {
	//Effect: Change the player's mana to the given value
        return new Player(location, direction, maxHealth, health, maxMana, newMana, imgName, inventory, weapon);
    }

    public Player removeItem(Carryable item) {
	return new Player(location, direction, maxHealth, health, maxMana, mana, imgName, inventory.remove(item), weapon);
    }

    public Player changeWeapon(Weapon newWeapon) {
	//Effect: Changes the Player's Weapon to the given weapon
        return new Player(location, direction, maxHealth, health, maxMana, mana, imgName, inventory, newWeapon);
    }

    public Player changeImage(String newImgName) {
	//Effect: Changes the Player's imgName to the given String
        return new Player(location, direction, maxHealth, health, maxMana, mana, newImgName, inventory, weapon);
    }

    public Player struck(Enemy enemy, Cell[][] cells) {
        Direction enemyDir = enemy.getDirection();
        int tryX = location.x; 
	int tryY = location.y; 
	if(enemyDir == Direction.UP)
	    tryY = tryY-1;
	else if(enemyDir == Direction.RIGHT)
	    tryX = tryX+1;
	else if(enemyDir == Direction.DOWN)
	    tryY = tryY+1;
	else
	    tryX = tryX-1;

	//Enemy is attacking the player in the open, simply move the player back
	//in the direction they were atatcked from
	if(Crawl.validCell(new Point(tryX, tryY), cells)) {
	    return this.move(new Point(tryX, tryY)).changeHealth(health-5);
	}
	//Enemy is attacking the player along the y axis and the playr is up against
	//a wall
	else if(enemyDir == Direction.UP || enemyDir == Direction.DOWN) {
	    if(Crawl.validCell(new Point(location.x+1, location.y), cells)) {
	        return this.move(new Point(location.x+1, location.y)).changeHealth(health-5);
	    }
	    else if(Crawl.validCell(new Point(location.x-1, location.y), cells)) {
		return this.move(new Point(location.x-1, location.y)).changeHealth(health-5);
	    }
	    else if(Crawl.validCell(new Point(location.x, location.y+2), cells)) {
		return this.move(new Point(location.x, location.y+2)).changeHealth(health-5);
	    } else {
		return this.move(new Point(location.x, location.y-2)).changeHealth(health-5);
	    }
	}
	//Enemy is attacking the player along the x axis, and the player is up against
	//a wall
	else {
	    if(Crawl.validCell(new Point(location.x, location.y+1), cells)) {
	        return this.move(new Point(location.x, location.y+1)).changeHealth(health-5);
	    }
	    else if(Crawl.validCell(new Point(location.x, location.y-1), cells)) {
		return this.move(new Point(location.x, location.y-1)).changeHealth(health-5);
	    }
	    else if(Crawl.validCell(new Point(location.x+2, location.y), cells)) {
		return this.move(new Point(location.x+2, location.y)).changeHealth(health-5);
	    } else {
		return this.move(new Point(location.x-2, location.y)).changeHealth(health-5);
	    }
	} 

	

    }
}

//---//---// Pathfinding Algorithm //---//---//

class SearchPoint {
    Point pt;
    SearchPoint parent;

    public SearchPoint(Point pt, SearchPoint parent) {
	this.pt = pt;
	this.parent = parent;
    }

    public Point getPoint() {
	return this.pt;
    }

    public SearchPoint getParent() {
	return this.parent;
    }

    public double distance(Point pt2) {
	int xDist = this.pt.x-pt2.x;
	int yDist = this.pt.y-pt2.y;
	return Math.sqrt(xDist*xDist + yDist*yDist);
    }

    //To be used only between two points that are guaranteed to have a path
    private static PointList getReturnPath(SearchPoint sPt) {
	if(sPt.getParent() == null) {
	    return new EmptyPtList();
	}
	else {
	    return getReturnPath(sPt.getParent()).append(sPt.getPoint());
	    // return new ConsPtList(sPt.getPoint(), getReturnPath(sPt.getParent()));
	}
    }

    public PointList findPath(Point pt, Cell[][] cells, SearchList open, SearchList closed) {
	if(closed.pointMember(pt)) {
	    return getReturnPath(closed.getWithPoint(pt));
	    //Must be very far away if the target hasn't been found after checking this many, 
	    //so just move randomly
	    //not perfect but close enough for now
	} else if(closed.size() >= 50) {
	    return getReturnPath(this);
	} else { 
	    int x = this.pt.x;
	    int y = this.pt.y;
	    SearchList tempOpen = open;
	    //Check if each adjacent cell exists and is open, if so, add it to the open list of points to be checked
	    if(x-1 >= 0 &&
	       cells[x-1][y].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x-1,y), this));
	    if(x+1 < cells.length &&
	       cells[x+1][y].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x+1,y), this));
	    if(y-1 >= 0 &&
	       cells[x][y-1].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x,y-1), this));
	    if(y+1 < cells[0].length &&
	       cells[x][y+1].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x,y+1), this));
	    //remove the point currently being checked from the open list and add it to the closed
	    tempOpen = tempOpen.remove(this);
	    SearchList tempClosed = closed.add(this);
	    return open.getClosest(pt).findPath(pt, cells, tempOpen, tempClosed);
	}
    }
    
    //For use with testers
    public boolean isPath(Point pt, Cell[][] cells, SearchList open, SearchList closed) {
	if(closed.pointMember(pt)) {
	    return true;
	} else { 
	    int x = this.pt.x;
	    int y = this.pt.y;
	    SearchList tempOpen = open;
	    //Check if each adjacent cell exists and is open, if so, add it to the open list of points to be checked
	    if(x-1 >= 0 &&
	       cells[x-1][y].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x-1,y), this));
	    if(x+1 < cells.length &&
	       cells[x+1][y].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x+1,y), this));
	    if(y-1 >= 0 &&
	       cells[x][y-1].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x,y-1), this));
	    if(y+1 < cells[0].length &&
	       cells[x][y+1].isOpenHuh() &&
	       !closed.pointMember(this.pt))
		tempOpen = tempOpen.add(new SearchPoint(new Point(x,y+1), this));
	    //remove the point currently being checked from the open list and add it to the closed
	    tempOpen = tempOpen.remove(this);
	    SearchList tempClosed = closed.add(this);
	    return open.getClosest(pt).isPath(pt, cells, tempOpen, tempClosed);
	}
    }

    public boolean equals(SearchPoint sPt) {
	return this.pt.equals(sPt.getPoint());
    }

}

interface SearchList {
    public boolean isEmpty();
    
    public SearchPoint first();

    public SearchList rest();

    public SearchList add(SearchPoint sPt);

    public SearchList remove(SearchPoint sPt);

    public boolean member(SearchPoint sPt);

    public boolean pointMember(Point pt);

    public SearchPoint getWithPoint(Point pt);

    public SearchPoint getClosest(Point pt);

    public SearchPoint getRandom();

    public int size();
}

class EmptySearchList implements SearchList {

    public EmptySearchList() {}

    public boolean isEmpty() {
	return true;
    }

    public SearchPoint first() {
	throw new EmptyListException("No Items in this List");
    }

    public SearchList rest() {
	return this;
    }

    public SearchList add(SearchPoint sPt) {
	return new ConsSearchList(sPt, this);
    }

    public SearchList remove(SearchPoint sPt) {
	return this;
    }

    public boolean member(SearchPoint sPt) {return false;}

    public boolean pointMember(Point pt) {return false;}

    public SearchPoint getWithPoint(Point pt) {
	throw new EmptyListException("No Items in this list");
    }

    public SearchPoint getClosest(Point pt) {
	throw new EmptyListException("No Items in this list");
    }

    public SearchPoint getRandom() {
	throw new EmptyListException("No Items in this list");
    }

    public int size() {
	return 0;
    }
}

class ConsSearchList implements SearchList {
    SearchPoint first;
    SearchList rest;

    public ConsSearchList(SearchPoint first, SearchList rest) {
	this.first = first;
	this.rest = rest;
    }

    public boolean isEmpty() {
	return false;
    }

    public SearchPoint first() {
	return this.first;
    }

    public SearchList rest() {
	return this.rest;
    }

    public SearchList add(SearchPoint sPt) {
	return new ConsSearchList(first, rest.add(sPt));
    }

    public SearchList remove(SearchPoint sPt) {
	if(sPt.equals(first)) {
	    return rest;
	}
	else {
	    return new ConsSearchList(first, rest.remove(sPt));
	}
    }

    public boolean member(SearchPoint sPt) {
	return sPt.equals(first) || rest.member(sPt);	
    }

    public boolean pointMember(Point pt) {
	return pt.equals(first.getPoint()) || rest.pointMember(pt);
    }

    public SearchPoint getWithPoint(Point pt) {
	if(first.getPoint().equals(pt)) {
	    return first;
	} else {
	    return rest.getWithPoint(pt);
	}
    }

    private SearchPoint getClosestTrack(Point pt, SearchPoint trackPt, int trackDist) {
	if(rest.isEmpty()) {
	    return trackPt;
	} else {
	    int xDist = first.getPoint().x - pt.x;
	    int yDist = first.getPoint().y - pt.y;
	    int totalDist = (int) Math.sqrt(xDist*xDist + yDist*yDist);
	    if(trackPt == null || totalDist < trackDist) {
		return ((ConsSearchList) rest).getClosestTrack(pt, first, totalDist);
	    } else {
		return ((ConsSearchList) rest).getClosestTrack(pt, trackPt, trackDist);
	    }
	}
    }
    
    public SearchPoint getClosest(Point pt) {
	    int xDist = first.getPoint().x - pt.x;
	    int yDist = first.getPoint().y - pt.y;
	    int totalDist = (int) Math.sqrt(xDist*xDist + yDist*yDist);
	return this.getClosestTrack(pt, first, totalDist);
    }

    public SearchPoint getRandom() {
	if(rest.isEmpty()) {
	    return first;
	    //since we are looking at a list of 50 items, have a 1 in 50 chance
	    //of getting one
	} else if(Math.random() < .02) {
	    return first;
	} else {
	    return rest.getRandom();
	}
    }

    public int size() {
	return 1+rest.size();
    }
}

enum Direction {
    UP, RIGHT, DOWN, LEFT;
}      

class Enemy implements Collidable {
    Point location;
    int health;
    String imgName;
    Posn position;
    Direction direction;
    static final int CHASE_DIST = 12;

    public Enemy(Point location, int health, String imgName, Direction direction) {
	this.location = location;
	this.health = health;
	this.imgName = imgName;
	this.direction = direction;
	this.position = new Posn(location.x*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, location.y*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT/2);
    }

    public Point getLocation() {
	return this.location;
    }

    public int getHealth() {
	return this.health;
    }

    public Direction getDirection() {
	return this.direction;
    }

    public Enemy move(int dx, int dy) {
	return new Enemy(new Point(this.location.x + dx, this.location.y + dy), health, imgName, direction);
    }

    public Enemy move(Point pt) {
	Direction newDirection;
	if(pt.x > location.x) {
	    newDirection = Direction.RIGHT;
	} else if(pt.x < location.x) {
	    newDirection = Direction.LEFT;
	} else if(pt.y > location.y) {
	    newDirection = Direction.DOWN;
	} else if(pt.y < location.y) {
	    newDirection = Direction.UP;
	} else {
	    newDirection = direction;
	}
	return new Enemy(pt, health, imgName, newDirection);
    }

    public WorldImage draw() {
	return new FromFileImage(position, imgName);
    }

    public Enemy changeHealth(int newHealth) {
	return new Enemy(location, newHealth, imgName, direction);
    }

    public Enemy changeImage(String newImgName) {
	return new Enemy(location, health, newImgName, direction);
    }

    public boolean sameSpace(Point pt) {
	return pt.equals(this.location);
    }

    //This seems very ineffecient currently, as the path has to be recalculated every time that it moves one space.
    //Could possibly be improved by adding path field to the enemy that only gets updated every so often
    public Enemy chase(Player p, Cell[][] cells) {
	SearchPoint start = new SearchPoint(location, null);
	PointList path = start.findPath(p.getLocation(), cells, new EmptySearchList().add(start), new EmptySearchList());
	return this.move(path.first());
    }

public Enemy struck(Player player, Cell[][] cells) {
        Direction playerDir = player.getDirection();
        int tryX = location.x; 
	int tryY = location.y; 
	if(playerDir == Direction.UP)
	    tryY = tryY-1;
	else if(playerDir == Direction.RIGHT)
	    tryX = tryX+1;
	else if(playerDir == Direction.DOWN)
	    tryY = tryY+1;
	else
	    tryX = tryX-1;

	//Enemy is attacking the player in the open, simply move the player back
	//in the direction they were atatcked from
	if(Crawl.validCell(new Point(tryX, tryY), cells)) {
	    return this.move(new Point(tryX, tryY)).changeHealth(health-player.getWeapon().getDamage());
	}
	//Enemy is attacking the player along the y axis and the playr is up against
	//a wall
	else if(playerDir == Direction.UP || playerDir == Direction.DOWN) {
	    if(Crawl.validCell(new Point(location.x+1, location.y), cells)) {
	        return this.move(new Point(location.x+1, location.y)).changeHealth(health-player.getWeapon().getDamage());
	    }
	    else if(Crawl.validCell(new Point(location.x-1, location.y), cells)) {
		return this.move(new Point(location.x-1, location.y)).changeHealth(health-player.getWeapon().getDamage());
	    }
	    else if(Crawl.validCell(new Point(location.x, location.y+2), cells)) {
		return this.move(new Point(location.x, location.y+2)).changeHealth(health-player.getWeapon().getDamage());
	    } else {
		return this.move(new Point(location.x, location.y-2)).changeHealth(health-player.getWeapon().getDamage());
	    }
	}
	//Enemy is attacking the player along the x axis, and the player is up against
	//a wall
	else {
	    if(Crawl.validCell(new Point(location.x, location.y+1), cells)) {
	        return this.move(new Point(location.x, location.y+1)).changeHealth(health-player.getWeapon().getDamage());
	    }
	    else if(Crawl.validCell(new Point(location.x, location.y-1), cells)) {
		return this.move(new Point(location.x, location.y-1)).changeHealth(health-player.getWeapon().getDamage());
	    }
	    else if(Crawl.validCell(new Point(location.x+2, location.y), cells)) {
		return this.move(new Point(location.x+2, location.y)).changeHealth(health-player.getWeapon().getDamage());
	    } else {
		return this.move(new Point(location.x-2, location.y)).changeHealth(health-player.getWeapon().getDamage());
	    }
	} 

	

    }

    public Enemy onTick(Player p, Cell[][] cells) {
	int xDist = Math.abs(p.getLocation().x - location.x);
	int yDist = Math.abs(p.getLocation().y - location.y);
	double totalDist =  Math.sqrt(xDist*xDist+yDist*yDist);
	if(totalDist <= CHASE_DIST && !location.equals(p.getLocation())) {
	    return this.chase(p, cells);
	}
	else {
	    int newX = location.x;
	    int newY = location.y;
	    int choose = (int) (Math.random()*4);
	    if(choose == 0) {
		newX += 1;
	    } else if(choose == 1) {
		newX -= 1;
	    } else if(choose == 2) {
	        newY += 1;
	    } else {
	        newY -= 1;
	    }
	    if(newX >= 0 && newX < cells.length &&
	       newY >= 0 && newY < cells[0].length &&
	       cells[newX][newY].isOpenHuh()) {
		return this.move(new Point(newX, newY));
	    } else {
		return this;
	    }
	}
    }
}

interface EnemyList {
    boolean isEmpty();
    Enemy first();
    EnemyList rest();
    EnemyList add(Enemy e);
    EnemyList chase(Player p, Cell[][] cells); 
    EnemyList onTick(Player p, Cell[][] cells);
    EnemyList removeDead();
    EnemyList attacked(Player p, Cell[][] cells);
    boolean isCollision(Player p);
    Enemy whichCollision(Player p);
    //Methods to be used with tester functions in CrawlTests
    boolean isEnemyAt(Point pt);
    boolean validCells(Cell[][] cells);
    PointList getLocations();
    //
    WorldImage draw();
}

class EmptyEnemyList implements EnemyList{
    public EmptyEnemyList() {}

    public boolean isEmpty() {
	return true;
    }

    public Enemy first() {
	throw new EmptyListException("Nothing in an empty list");
    }

    public EnemyList rest() {
	return this;
    }

    public EnemyList add(Enemy e) {
	return new ConsEnemyList(e, this);
    }
    
    public EnemyList chase(Player p, Cell[][] cells) {
	return this;
    }

    public EnemyList onTick(Player p, Cell[][] cells) {
	return this;
    }

    public EnemyList removeDead() {
	return this;
    }

    public EnemyList attacked(Player p, Cell[][] cells) {
	return this;
    }

    public boolean isCollision(Player p) {
	return false;
    }

    public Enemy whichCollision(Player p) {
	throw new EmptyListException("No enemy was colliding with the player");
    }

    public boolean isEnemyAt(Point pt) {
	return false;
    }

    public boolean validCells(Cell[][] cells) {
	return true;
    }

    public PointList getLocations() {
	return new EmptyPtList();
    }

    public WorldImage draw() {
	return new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
    }
}

class ConsEnemyList implements EnemyList{
    Enemy first;
    EnemyList rest;
    
    public ConsEnemyList(Enemy first, EnemyList rest) {
	this.first = first;
	this.rest = rest;
    }

    public boolean isEmpty() {
	return false;
    }
    
    public Enemy first() {
	return first;
    }

    public EnemyList rest() {
	return rest;
    }

    public EnemyList add(Enemy e) {
	return new ConsEnemyList(e, this);
    }

    public EnemyList chase(Player p, Cell[][] cells) {
	return new ConsEnemyList(first.chase(p, cells), rest.chase(p, cells));
    }

    public EnemyList onTick(Player p, Cell[][] cells) {
	return new ConsEnemyList(first.onTick(p, cells), rest.onTick(p, cells));
    }

    public EnemyList removeDead() {
	if(first.getHealth() <= 0) {
	    return rest.removeDead();
	} else {
	    return new ConsEnemyList(first, rest.removeDead());
	}
    }

    public EnemyList attacked(Player p, Cell[][] cells) {
	int xDist = p.getLocation().x - first.getLocation().x;
	int yDist = p.getLocation().y - first.getLocation().y;
	int range = p.getWeapon().getRange();
	if(p.getDirection() == Direction.LEFT && yDist == 0 && (xDist >= 0 && xDist <= range)) {
	    return new ConsEnemyList(first.struck(p, cells), rest.attacked(p, cells));
	} else if(p.getDirection() == Direction.RIGHT && yDist == 0 && (xDist <= 0 && -xDist <= range)) {
	    return new ConsEnemyList(first.struck(p, cells), rest.attacked(p, cells));
	} else if(p.getDirection() == Direction.UP && xDist == 0 && (yDist >= 0 && yDist <= range)) {
	    return new ConsEnemyList(first.struck(p, cells), rest.attacked(p, cells));
	} else if(p.getDirection() == Direction.DOWN && xDist == 0 && (yDist <= 0 && -yDist <= range)) {
	    return new ConsEnemyList(first.struck(p, cells), rest.attacked(p, cells));
	} else {
	    return new ConsEnemyList(first, rest.attacked(p, cells));
	}
    }

    public boolean isCollision(Player p) {
	return first.sameSpace(p.getLocation()) ||
	    rest.isCollision(p);
    }

    public Enemy whichCollision(Player p) {
	if(first.sameSpace(p.getLocation()))
	    return first;
	else
	    return rest.whichCollision(p);
    }

    public boolean isEnemyAt(Point pt) {
	return first.getLocation().equals(pt) ||
	    rest.isEnemyAt(pt);
    }

    public boolean validCells(Cell[][] cells) {
	return Crawl.validCell(first.getLocation(), cells) &&
	    rest.validCells(cells);
    }

    public PointList getLocations() {
	return new ConsPtList(first.getLocation(), rest.getLocations());
    }

    public WorldImage draw() {
	return first.draw().overlayImages(rest.draw());
    }

}

interface Carryable {
    public Point getLocation();
    public Player use(Player p);
    public WorldImage drawInWorld();
    WorldImage drawInInventory(int slot);
    //need to implement equals manually?
    //public boolean equals(Carryable item);
}

class Weapon implements Carryable {
    Point location;
    int damage;
    int range;
    int manaCost;
    String worldImgName;
    String invImgName;
    String leftEffect;
    String rightEffect;
    String upEffect;
    String downEffect;

    public Weapon(Point location, int damage, int range, int manaCost,
		  String worldImgName, String invImgName, String leftEffect, String rightEffect, String upEffect, String downEffect) {
	this.location = location;
	this.damage = damage;
	this.range = range;
	this.manaCost = manaCost;
	this.worldImgName = worldImgName;
	this.invImgName = invImgName;
	this.leftEffect = leftEffect;
	this.rightEffect = rightEffect;
	this.upEffect = upEffect;
	this.downEffect = downEffect;
    }

    public Point getLocation() {
	return this.location;
    }

    public int getDamage() {
	return this.damage;
    }

    public int getRange() {
	return this.range;
    }

    public int getManaCost() {
	return this.manaCost;
    }

    public String leftEffect() {
	return this.leftEffect;
    }

    public String rightEffect() {
	return this.rightEffect;
    }

    public String upEffect() {
	return this.upEffect;
    }

    public String downEffect() {
	return this.downEffect;
    }

    public Player use(Player p) {
	return p.changeWeapon(this);
    }

    public WorldImage drawInWorld() {
	Posn drawPoint = new Posn(location.x*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, location.y*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT/2);
	return new FromFileImage(drawPoint, worldImgName);
    }

    public WorldImage drawInInventory(int slot) {
	int xSpace = (Cell.CELL_WIDTH*Crawl.WINDOW_WIDTH)/5;
	int ySpace = (Cell.CELL_HEIGHT*Crawl.WINDOW_HEIGHT)/2;
	int x;
	int y;
	if(slot < 5) {
	    x = xSpace*slot + xSpace/2;
	    y = ySpace/2;
	} else {
	    x = xSpace*(slot-5) + xSpace/2;
	    y = ySpace+ySpace/2;
	}
	return new FromFileImage(new Point(x,y), invImgName);
    }

    public static Weapon sword(Point location) {
	return new Weapon(location, 5, 1, 0, "images/sword-world.png", "images/sword-inventory.png", "images/sword-left-effect.png",
			  "images/sword-right-effect.png", "images/sword-up-effect.png", "images/sword-down-effect.png"); 
    }

    public static Weapon lightning(Point location) {
	return new Weapon(location, 8, 5, 10, "images/lightning-world.png", "images/lightning-inventory.png", "images/lightning-left-effect.png",
			  "images/lightning-right-effect.png", "images/lightning-up-effect.png", "images/lightning-down-effect.png");
    }
}

interface Consumable extends Carryable {

}

class HealthPotion implements Consumable {
    Point location;
    static final int healthValue = 20;

    public HealthPotion(Point location) {
	this.location = location;
    }

    public Point getLocation() {
	return this.location;
    }

    public Player use(Player p) {
	return p.changeHealth(Math.min(p.getHealth()+healthValue, p.getMaxHealth())).removeItem(this);
    }

    public WorldImage drawInWorld() {
	Posn drawPoint = new Posn(location.x*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, location.y*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT/2);
	return new FromFileImage(drawPoint, "images/health-potion-world.png");
    }

    public WorldImage drawInInventory(int slot) {
	int xSpace = (Cell.CELL_WIDTH*Crawl.WINDOW_WIDTH)/5;
	int ySpace = (Cell.CELL_HEIGHT*Crawl.WINDOW_HEIGHT)/2;
	int x;
	int y;
	if(slot < 5) {
	    x = xSpace*slot + xSpace/2;
	    y = ySpace/2;
	} else {
	    x = xSpace*(slot-5) + xSpace/2;
	    y = ySpace+ySpace/2;
	}
	return new FromFileImage(new Point(x,y), "images/health-potion-inventory.png");
    }
}

class ManaPotion implements Consumable {
    Point location;
    static final int manaValue = 20;

    public ManaPotion(Point location) {
	this.location = location;
    }

    public Point getLocation() {
	return this.location;
    }

    public Player use(Player p) {
	return p.changeMana(Math.min(p.getMana()+manaValue, p.getMaxMana())).removeItem(this);
    }

    public WorldImage drawInWorld() {
	Posn drawPoint = new Posn(location.x*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, location.y*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT/2);
	return new FromFileImage(drawPoint, "images/mana-potion-world.png");
    }

    public WorldImage drawInInventory(int slot) {
	int xSpace = (Cell.CELL_WIDTH*Crawl.WINDOW_WIDTH)/5;
	int ySpace = (Cell.CELL_HEIGHT*Crawl.WINDOW_HEIGHT)/2;
	int x;
	int y;
	if(slot < 5) {
	    x = xSpace*slot + xSpace/2;
	    y = ySpace/2;
	} else {
	    x = xSpace*(slot-5) + xSpace/2;
	    y = ySpace+ySpace/2;
	}
	return new FromFileImage(new Point(x,y), "images/mana-potion-inventory.png");
    }
}

	   

interface EquipList {

    public int size();
    public Carryable first();
    public EquipList rest();
    public Carryable get(int place);
    public EquipList add(Carryable item);
    public EquipList remove(Carryable item);
    public boolean isItemAt(Point pt);
    public Carryable getItemAt(Point pt);
    public EquipList removeItemAt(Point pt);
    public WorldImage drawInWorld();
    public WorldImage drawInInventorySlot(int slot);
    public WorldImage drawInInventory();
    //Methods to be used with testers in CrawlTests
    public boolean validCells(Cell[][] cells);
    public boolean isItem(Carryable item);
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

    public Carryable get(int place) {
	throw new EmptyListException("Tried to access an item from the empty EquipList");
    }

    public boolean isItemAt(Point pt) {
	return false;
    }

    public Carryable getItemAt(Point pt) {
	throw new EmptyListException("Tried to access an item from the empty EquipList");
    }

    public EquipList removeItemAt(Point pt) {
	return this;
    }

    public WorldImage drawInWorld() {
	return new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
    }

    public WorldImage drawInInventorySlot(int slot) {
	return new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
    }

    public WorldImage drawInInventory() {
	return new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
    }

    public boolean validCells(Cell[][] cells) {
	return true;
    }

    public boolean isItem(Carryable item) {
	return false;
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

    public Carryable get(int place) {
	if(place == 0) {
	    return first;
	} else {
	    return rest.get(place-1);
	}
    }

    public boolean isItemAt(Point pt) {
	return first.getLocation().equals(pt) ||
	    rest.isItemAt(pt);
    }

    public Carryable getItemAt(Point pt) {
	if(first.getLocation().equals(pt)) {
	    return first;
	} else {
	    return rest.getItemAt(pt);
	}
    }

    public EquipList removeItemAt(Point pt) {
	if(first.getLocation().equals(pt)) {
	    return rest;
	} else {
	    return new List(first, rest.removeItemAt(pt));
	}
    }

    public WorldImage drawInWorld() {
	return new OverlayImages(first.drawInWorld(), rest.drawInWorld());
    }
    
    public WorldImage drawInInventorySlot(int slot) {
	return new OverlayImages(first.drawInInventory(slot), rest.drawInInventorySlot(slot+1));
    }

    public WorldImage drawInInventory() {
	return this.drawInInventorySlot(0);
    }

    public boolean validCells(Cell[][] cells) {
	return Crawl.validCell(first.getLocation(), cells) &&
	    rest.validCells(cells);
    }

    public boolean isItem(Carryable item) {
	return first.equals(item) || rest.isItem(item);
    }
}


//BSP DUNGEON GENERATION


class Cell {
    public static final int CELL_WIDTH = 35;
    public static final int CELL_HEIGHT = 35;
    boolean isOpen;
    int type; //0 for room, 1 for hall

    public Cell(boolean isOpen) {
	this.isOpen = isOpen;
	this.type = 0;
    }

    public Cell(boolean isOpen, int type) {
	this.isOpen = isOpen;
	this.type = type;
    }
    
    public boolean isOpenHuh() {
	return this.isOpen;
    }

    public Cell setEmptiness(boolean isOpen) {
	return new Cell(isOpen);
    }

    public String toString() {
        if(isOpen && type == 1) 
	    return "0";
	else if(isOpen)
	    return "X";
	else
	    return "_";
    }
}


//ALL WIDTHS AND HEIGHTS FOR THE DUNGEON ARE GIVEN IN CELLS
interface Dungeon {

    Dungeon generate(int minWidth, int minHeight);

    Cell[][] getCells();

    int getWidth();

    int getHeight();
}

class BaseDungeon implements Dungeon{
    Point location;
    int width;
    int height;
    PointList hallPoints;

    public BaseDungeon(Point location, int width, int height, PointList hallPoints) {
	this.location = location;
	this.width = width;
	this.height = height;
	this.hallPoints = hallPoints;
    }

    private Dungeon partitionLR(int minWidth, int minHeight) {
	int splitPt = (int) Math.max(minWidth*1.25, Math.random()*(width-minWidth*1.25));
	int hallPt = (int) (height*.125 + (Math.random()*height*.75));
        hallPoints = hallPoints.add(new Point(location.x + splitPt, location.y + hallPt));
	return new XSplit(location, width, height, splitPt,
			  new BaseDungeon(location, splitPt, height,
					  hallPoints.leftOf(location.x+splitPt)).generate(minWidth, minHeight),
			  new BaseDungeon(new Point(this.location.x+splitPt, this.location.y), 
					  width-splitPt, height,
					  hallPoints.rightOf(location.x+splitPt)).generate(minWidth, minHeight),
			  hallPoints);
    }

    private Dungeon partitionTB(int minWidth, int minHeight) {
	int splitPt = (int) Math.max(minHeight*1.25, Math.random()*(height-minHeight*1.25));
	int hallPt = (int) (width*.125 + (Math.random()*width*.75));
	hallPoints = hallPoints.add(new Point(location.x + hallPt, location.y+splitPt));
	return new YSplit(location, width, height, splitPt,
			  new BaseDungeon(location, width, splitPt, hallPoints.above(location.y+splitPt)).generate(minWidth, minHeight),
			  new BaseDungeon(new Point(this.location.x, this.location.y+splitPt),
					  width, height-splitPt, hallPoints.below(location.y+splitPt)).generate(minWidth, minHeight),
			  hallPoints);
    }

    public Dungeon generate(int minWidth, int minHeight) {
	//If the width or height is great enough, then we want to split the Dungeon
	if((width >= 2.5*minWidth && height >= 1.25*minHeight) || (height >= 2.5*minHeight && width >= 1.25*minWidth)) {
	    if(width >= height*1.5) {
		return this.partitionLR(minWidth, minHeight);
	    } else if(height >= width*1.5) {
		return this.partitionTB(minWidth, minHeight);
	    } else if(Math.random() >= 0.5) {
		return this.partitionLR(minWidth, minHeight);
	    } else {
		return this.partitionLR(minWidth, minHeight);
	    }
	    //neither width nor height is great enough to store two rooms, so just create one
	} else {
	    //Create room b/w room size and minSize
	    int leftBound;
	    int rightBound;
	    int topBound;
	    int bottomBound;
	    PointList allButLeft = hallPoints.removeAtXBetween(location.x, location.y, location.y+height);
	    if(allButLeft.isEmpty()) {
		leftBound = location.x + width/4;
	    } else {
		leftBound = Math.min(allButLeft.leftMost().x, location.x + width/4);
	    }
	    PointList allButRight = hallPoints.removeAtXBetween(location.x + width, location.y, location.y+height);
	    if(allButRight.isEmpty()) {
		rightBound = location.x + (int) (width*.75);
	    } else {
		rightBound = Math.max(allButRight.rightMost().x+1, location.x + (int) (width*.75));
	    }
	    PointList allButTop = hallPoints.removeAtYBetween(location.y, location.x, location.x+width);
	    if(allButTop.isEmpty()) {
		topBound = location.y + height/4;
	    } else {
		topBound = Math.min(allButTop.topMost().y, location.y + height/4);
	    }
	    PointList allButBottom = hallPoints.removeAtYBetween(location.y+height, location.x, location.x+width);
	    if(allButBottom.isEmpty()) {
	        bottomBound = location.y + (int) (height*.75);
	    } else {
		bottomBound = Math.max(allButBottom.bottomMost().y+1, location.y + (int) (height*.75));
	    }
	    int roomX = leftBound;
	    int roomY = topBound;
	    int roomWidth = rightBound-leftBound;
	    int roomHeight = bottomBound-topBound;
	    return new Room(location, width, height, new Point(roomX, roomY), roomWidth, roomHeight, hallPoints);
	}  
    }

    public Cell[][] getCells() {
	return new Cell[0][0];
    }

    public int getWidth() {
	return this.width;
    }

    public int getHeight() {
	return this.height;
    }
    
}

class XSplit implements Dungeon {
    Point location;
    int width;
    int height;
    int xSplitPt;
    Dungeon left;
    Dungeon right;
    Cell[][] cells;
    PointList hallPoints;

    public XSplit(Point location, int width, int height, int xSplitPt,
		  Dungeon left, Dungeon right, PointList hallPoints) {
        this.location = location;
	this.width = width;
	this.height = height;
	this.xSplitPt = xSplitPt;
	this.left = left;
	this.right = right;
	int leftWidth = left.getWidth();
	int rightWidth = right.getWidth();
	Cell[][] tempCells = new Cell[leftWidth+rightWidth][height];
	for(int i = 0; i < leftWidth; i++) {
	    for(int j = 0; j < height; j++) {
		tempCells[i][j] = left.getCells()[i][j];
	    }
	}
	for(int i = 0; i < right.getWidth(); i++) {
	    for(int j = 0; j < height; j++) {
		tempCells[i+leftWidth][j] = right.getCells()[i][j];
	    }
	}
	this.cells = tempCells;
	this.hallPoints = hallPoints;
    }

    public Dungeon generate(int minWidth, int minHeight) {
	return this;
    }

    public Cell[][] getCells() {
	return this.cells;
    }

    public int getWidth() {
	return this.width;
    }

    public int getHeight() {
	return this.height;
    }

    public String toString() {
	String s = "";
	for(int j = 0; j < cells[0].length; j++) {
	    for(int i = 0; i < cells.length; i++) {
		s = s + cells[i][j].toString() + " ";
	    }
	    s = s + "\n";
	}
	return s;
    }
}

class YSplit implements Dungeon {
    Point location;
    int width;
    int height;
    int ySplitPt;
    Dungeon top;
    Dungeon bottom;
    Cell[][] cells;
    PointList hallPoints;
   
    public YSplit(Point location, int width, int height, int ySplitPt,
		  Dungeon top, Dungeon bottom, PointList hallPoints) {
	this.location = location;
	this.width = width;
	this.height = height;
	this.ySplitPt = ySplitPt;
	this.top = top;
	this.bottom = bottom;
	int topHeight = top.getHeight();
	int bottomHeight = bottom.getHeight();
	Cell[][] tempCells = new Cell[width][topHeight+bottomHeight];
	for(int i = 0; i < width; i++) {
	    for(int j = 0; j < topHeight; j++) {
		tempCells[i][j] = top.getCells()[i][j];
	    }
	}
	for(int i = 0; i < width; i++) {
	    for(int j = 0; j < bottomHeight; j++) {
		tempCells[i][j+topHeight] = bottom.getCells()[i][j];
	    }
	}
	this.cells = tempCells;
	this.hallPoints = hallPoints;
    }
    
    public Dungeon generate(int minWidth, int minHeight) {
	return this;
    }

    public Cell[][] getCells() {
	return this.cells;
    }

    public int getHeight() {
	return this.height;
    }

    public int getWidth() {
	return this.width;
    }

    public String toString() {
	String s = "";
	for(int j = 0; j < cells[0].length; j++) {
	    for(int i = 0; i < cells.length; i++) {
		s = s + cells[i][j].toString() + " ";
	    }
	    s = s + "\n";
	}
	return s;
    }
}

class Room implements Dungeon {
    Point location;
    int width;
    int height;
    Point roomLocation;
    int roomWidth;
    int roomHeight;
    PointList hallPoints;
    Cell[][] cells;

    private static boolean xEquals(int x, PointList hallPoints) {
        if(hallPoints.isEmpty())
	    return false;
	else
	    return x == hallPoints.first().x ||
		xEquals(x, hallPoints.rest());
    }

    private static PointList getAllWithX(int x, PointList hallPoints) {
	if(hallPoints.isEmpty()) {
	    return new EmptyPtList();
	}
	else if(hallPoints.first().x == x) {
	    return new ConsPtList(hallPoints.first(), getAllWithX(x, hallPoints.rest()));
	}
	else {
	    return getAllWithX(x, hallPoints.rest());
	}
    }

    private boolean yEquals(int y, PointList hallPoints) {
	if(hallPoints.isEmpty())
	    return false;
	else
	    return y == hallPoints.first().y ||
		yEquals(y, hallPoints.rest());
    }

    private static PointList getAllWithY(int y, PointList hallPoints) {
	if(hallPoints.isEmpty()) {
	    return new EmptyPtList();
	}
	else if(hallPoints.first().y == y) {
	    return new ConsPtList(hallPoints.first(), getAllWithY(y, hallPoints.rest()));
	}
	else {
	    return getAllWithY(y, hallPoints.rest());
	}
    }
    
    //Still a lot to be done here, probably can make this a lot cleaner and more efficient by removing
    //checks that are not needed
    public Room(Point location, int width, int height, Point roomLocation, 
		int roomWidth, int roomHeight, PointList hallPoints) {
        this.location = location;                  
	this.width = width;                        
	this.height = height;                      
	this.roomLocation = roomLocation;          
	this.roomWidth = roomWidth;                
	this.roomHeight = roomHeight;               
	this.hallPoints = hallPoints;             
	Cell[][] tempCells = new Cell[width][height];
	for(int i = location.x; i < location.x + width; i++) {
	    for(int j = location.y; j < location.y + height; j++) {
		//Check to see if the given cell is in within the given bounds for the room
		if((roomLocation.x <= i && i < roomLocation.x+roomWidth) &&
		   (roomLocation.y <= j && j < roomLocation.y+roomHeight)) 
		    tempCells[i-location.x][j-location.y] = new Cell(true);
		//Check to see if the given cell should be an active hallway above room
		else if((roomLocation.x <= i && i < roomLocation.x + roomWidth) &&
			(location.y <= j && j < roomLocation.y) &&
			xEquals(i, getAllWithY(location.y, hallPoints)))
		    tempCells[i-location.x][j-location.y] = new Cell(true, 1);
		//Check for hallways to the right
		else if((roomLocation.x + roomWidth <= i && i < location.x + width) &&
			(roomLocation.y <= j && j < roomLocation.y + height) &&
		        yEquals(j, getAllWithX(location.x + width, hallPoints)))
		    tempCells[i-location.x][j-location.y] = new Cell(true, 1);  
		//Check for hallways below
		else if((roomLocation.x <= i && i < roomLocation.x + roomWidth) &&
			(roomLocation.y + roomHeight <= j && j < location.y + height) &&
			xEquals(i, getAllWithY(location.y+height, hallPoints)))
		    tempCells[i-location.x][j-location.y] = new Cell(true, 1);
		//Check for hallways to the left
		else if((location.x <= i && i < roomLocation.x) &&
			(roomLocation.y <= j && j < roomLocation.y + roomHeight) &&
		        yEquals(j, getAllWithX(location.x, hallPoints)))
		    tempCells[i-location.x][j-location.y] = new Cell(true, 1);       
		//Cell is not in the room or one of the hallways, so it is off  
		else {
		    tempCells[i-location.x][j-location.y] = new Cell(false);
		}
	    }
	}
	this.cells = tempCells;
    }

    public Dungeon generate(int minWidth, int minHeight) {
	return this;
    }

    public Cell[][] getCells() {
	return this.cells;
    }

    public int getWidth() {
	return this.width;
    }

    public int getHeight() {
	return this.height;
    }

    public boolean inRoom(Collidable c) {
	return cells[c.getLocation().x][c.getLocation().y].isOpenHuh();
    }

    public String toString() {
	String s = "";
	for(int j = 0; j < cells[0].length; j++) {
	    for(int i = 0; i < cells.length; i++) {
		s = s + cells[i][j].toString() + " ";
	    }
	    s = s + "\n";
	}
	return s;
    }
}

class Effect {
    String imgName;
    Point location;
    int width;
    int height;
    Direction direction;

    public Effect(Point location, String imgName, int width, int height, Direction direction) {
	this.location = location;
	this.imgName = imgName;
	this.width = width;
	this.height = height;
	this.direction = direction;
    }

    public String getImgName() {
	return this.imgName;
    }

    public Point getLocation() {
	return this.location;
    }

    public WorldImage draw(Player p) {
	if(direction == Direction.RIGHT) {
	    return new FromFileImage(new Posn(location.x*Cell.CELL_WIDTH+(Cell.CELL_WIDTH*width)/2,
					      location.y*Cell.CELL_HEIGHT+(Cell.CELL_HEIGHT*height)/2), imgName);
	} else if(direction == Direction.LEFT) {
	    return new FromFileImage(new Posn(location.x*Cell.CELL_WIDTH-(Cell.CELL_WIDTH*width)/2,
					      location.y*Cell.CELL_HEIGHT+(Cell.CELL_HEIGHT*height)/2), imgName);
	} else if(direction == Direction.DOWN) {
	    return new FromFileImage(new Posn(location.x*Cell.CELL_WIDTH+(Cell.CELL_WIDTH*width)/2,
					      location.y*Cell.CELL_HEIGHT+(Cell.CELL_HEIGHT*height)/2), imgName);
	}else {
	    return new FromFileImage(new Posn(location.x*Cell.CELL_WIDTH+(Cell.CELL_WIDTH*width)/2,
					      location.y*Cell.CELL_HEIGHT-(Cell.CELL_HEIGHT*height)/2), 
				     imgName);
	}
    }
}

enum State {
    GAME, WIN, INVENTORY, GAMEOVER;
}

public class Crawl extends World{
   
    int level;
    Player player;
    Cell[][] floor;
    EnemyList enemies;
    Effect effect;
    EquipList items;
    int selectedItem;
    Point exit;
    State state;
    //in cells
    static final int WINDOW_WIDTH = 25;
    static final int WINDOW_HEIGHT = 15;
    static final int DUNGEON_WIDTH = 90;
    static final int DUNGEON_HEIGHT = 90;
    static final int MIN_ROOM_WIDTH = 12;
    static final int MIN_ROOM_HEIGHT = 12;
    static final int NUM_ENEMIES = 10;
    static final int NUM_ITEMS = 4;
    static final int WIN_LEVEL = 15;


    //Constructor with no effect to initialize it as null, signalling that
    //no effect is currently present
    public Crawl(int level, Player player, Cell[][] floor,
		 EnemyList enemies, EquipList items, int selectedItem,
		 Point exit, State state) {
	this.level = level;
	this.player = player;
	this.floor = floor;
	this.enemies = enemies;
	this.items = items;
	this.selectedItem = selectedItem;
	this.exit = exit;
	this.state = state;
    }

    public int getLevel() {
	return this.level;
    }

    public Player getPlayer() {
	return this.player;
    }

    public Cell[][] getFloor() {
	return this.floor;
    }

    public EnemyList getEnemies() {
	return this.enemies;
    }

    public EquipList getItems() {
	return this.items;
    }

    public int getSelectedItem() {
	return this.selectedItem;
    }

    public Point getExit() {
	return this.exit;
    }

    public State getState() {
	return this.state;
    }

    public Crawl(int level, Player player, Cell[][] floor,
		 EnemyList enemies, Effect effect, EquipList items, int selectedItem,
		 Point exit, State state) {
	this.level = level;
	this.player = player;
	this.floor = floor;
	this.enemies = enemies;
	this.effect = effect;
	this.items = items;
	this.selectedItem = selectedItem;
	this.exit = exit;
	this.state = state;
    }

    public static boolean validCell(Point p, Cell[][] cells) {
	return 0 <= p.x &&
	    p.x < cells.length && 
	    0 <= p.y &&
	    p.y < cells[0].length &&
	    cells[p.x][p.y].isOpenHuh();
    }

    public WorldImage drawCells() {
	WorldImage current = new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
	for(int i = 0; i < floor.length; i++) {
	    for(int j = 0; j < floor[0].length; j++) {
		Point pt = new Point(i,j);
		if(exit.equals(pt)) {
		    current = current.overlayImages(new OverlayImages(new RectangleImage(new Posn(i*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2,
												  j*Cell.CELL_HEIGHT +Cell.CELL_HEIGHT/2),
											  Cell.CELL_WIDTH, Cell.CELL_HEIGHT, Color.LIGHT_GRAY),
								      new FromFileImage(new Posn(i*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2,
												 j*Cell.CELL_HEIGHT +Cell.CELL_HEIGHT/2),
											"images/exit.png")));
		}else if(validCell(pt, floor)) {
		    current = current.overlayImages(new RectangleImage(new Posn(i*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2,
										j*Cell.CELL_HEIGHT +Cell.CELL_HEIGHT/2),
								       Cell.CELL_WIDTH, Cell.CELL_HEIGHT, Color.LIGHT_GRAY));
		    
		} else {
		    current = current.overlayImages(new RectangleImage(new Posn(i*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, 
										j*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT/2),
								       Cell.CELL_WIDTH, Cell.CELL_HEIGHT, Color.BLACK));
		}
	    }
	}
	return current;
    }

    public static EnemyList generateEnemies(Cell[][] cells, int numEnemies) {
	EnemyList tempEnemies =  new EmptyEnemyList();
	while(!(numEnemies==0)) {
	    int randX = (int) (Math.random()*cells.length);
	    int randY = (int) (Math.random()*cells[0].length);
	    Point possible = new Point(randX, randY);
	    if(validCell(possible, cells)) {
		tempEnemies = tempEnemies.add(new Enemy(possible, 20, "images/slime.png", Direction.LEFT));
		numEnemies -= 1;
	    }
	}
	return tempEnemies;
    }

    public static Carryable generateRandItem(Point location) {
        double choose = Math.random();
	if(choose < .1) {
	    return Weapon.sword(location);
	} else if(choose < .2) {
	    return Weapon.lightning(location);
	} else if(choose < .55) {
	    return new HealthPotion(location);
	} else {
	    return new ManaPotion(location);
	}
    }

    public static EquipList generateItems(Cell[][] cells, int numItems) {
	EquipList tempItems = new EmptyList();
	while(!(numItems==0)) {
	    int randX = (int) (Math.random()*cells.length);
	    int randY = (int) (Math.random()*cells[0].length);
	    Point possible = new Point(randX, randY);
	    if(validCell(possible, cells)) {
		tempItems = tempItems.add(generateRandItem(new Point(randX, randY)));
		numItems -= 1;
	    }
	}
	return tempItems;
    }

    public static Point findTopLeft(Cell[][] cells) {
	int x = 0;
	int y = 0;
	while(!cells[x][y].isOpenHuh()) {
	    if(y > x) {
		x++;
	    } else {
		y++;
	    }
	}
	return new Point(x,y);
    }

    public static Point findBottomRight(Cell[][] cells) {
	int x = cells.length-1;
	int y = cells[0].length-1;
	while(!cells[x][y].isOpenHuh()) {
	    if(y < x) {
		x--;
	    } else {
		y--;
	    }
	}
	return new Point(x,y);
    }

    public Crawl nextLevel() {
	if(level+1 < WIN_LEVEL) {
	    Cell[][] nextFloor = new BaseDungeon(new Point(0,0),
						 DUNGEON_WIDTH, DUNGEON_HEIGHT, new EmptyPtList()).generate(MIN_ROOM_WIDTH, MIN_ROOM_HEIGHT).getCells();
	    EnemyList nextEnemies = generateEnemies(nextFloor, NUM_ENEMIES);
	    EquipList nextItems = generateItems(nextFloor, NUM_ITEMS);
	    return new Crawl(level+1, player.move(findTopLeft(nextFloor)), nextFloor, nextEnemies,
			     nextItems, selectedItem, findBottomRight(nextFloor), state);
	} else {
	    return new Crawl(level+1, player, floor, enemies, items, selectedItem, exit, State.WIN);
	}
    }
    
    public static Crawl newGame() {
	Cell[][] newFloor = new BaseDungeon(new Point(0,0),
					    DUNGEON_WIDTH,
					    DUNGEON_HEIGHT,
					    new EmptyPtList()).generate(MIN_ROOM_WIDTH, MIN_ROOM_HEIGHT).getCells();
	Player newPlayer = new Player(findTopLeft(newFloor), Direction.DOWN, 50, 50, 50, 50, "images/player-straight.png", 
				      new EmptyList().add(Weapon.sword(new Point(0,0))), Weapon.sword(new Point(0,0)));
	return new Crawl(0, newPlayer, newFloor, generateEnemies(newFloor, NUM_ENEMIES),
			 generateItems(newFloor, NUM_ITEMS), 0, findBottomRight(newFloor), State.GAME);
    }

    public boolean isCollision() {
	return enemies.isCollision(player);
    }

    public Crawl onTick() {
	if(state == State.GAME) {
	    if(player.isDead()) {
		return new Crawl(level, player, floor, enemies, items, selectedItem, exit, State.GAMEOVER);
	    }
	    EnemyList enemiesMoved = enemies.removeDead().onTick(player, floor);
	    Crawl temp = new Crawl(level, player, floor, enemiesMoved, items, selectedItem, exit, state);
	    Player tempPlayer = player;
	    EquipList tempItems = items;
	    try {
		if(items.isItemAt(tempPlayer.getLocation())) {
		    tempPlayer = tempPlayer.addItem(items.getItemAt(tempPlayer.getLocation()));
		    tempItems = items.removeItemAt(player.getLocation());
		    temp = new Crawl(level, tempPlayer, floor, enemiesMoved, tempItems, selectedItem, exit, state);
		}
	    } catch(InventoryFullException e) { }
	    if(tempPlayer.getLocation().equals(exit)) {
		return temp.nextLevel();
	    } else if(temp.isCollision()){
		Player playerMoved = tempPlayer.struck(enemiesMoved.whichCollision(player), floor);
		return new Crawl(level, playerMoved, floor, enemiesMoved, tempItems, selectedItem, exit, state);
	    }
	    else {
		return temp;
	    }
	} else {
	    return this;
	}
    }
    
    public Crawl onKeyEvent(String ke) {
	if(state == State.GAME) {
	    if(ke.equals("a") && validCell(new Point(player.getLocation().x-1, player.getLocation().y), floor)) {
		return new Crawl(level, player.move(-1, 0), floor, enemies, items, selectedItem, exit, state);
	    } else if(ke.equals("d") && validCell(new Point(player.getLocation().x+1, player.getLocation().y), floor)) {
		return new Crawl(level, player.move(1, 0), floor, enemies, items, selectedItem, exit, state);
	    } else if(ke.equals("w") && validCell(new Point(player.getLocation().x, player.getLocation().y-1), floor)) {
		return new Crawl(level, player.move(0, -1), floor, enemies, items, selectedItem, exit, state);
	    } else if(ke.equals("s") && validCell(new Point(player.getLocation().x, player.getLocation().y+1), floor)) {
		return new Crawl(level, player.move(0, 1), floor, enemies, items, selectedItem, exit, state);
	    } else if(ke.equals("e")) {
		return new Crawl(level, player, floor, enemies, items, 0, exit, State.INVENTORY);
	    } else if(player.getWeapon() != null) {
		try {
		    if(ke.equals("left")) {
			Player attacking = player.attack(Direction.LEFT);
			EnemyList attacked = enemies.attacked(attacking, floor);
		        Effect nextEffect = new Effect(new Point(player.getLocation().x, player.getLocation().y), player.getWeapon().leftEffect(),
						       player.getWeapon().getRange(), 1, Direction.LEFT);
			return new Crawl(level, attacking, floor, attacked, nextEffect, items, selectedItem, exit, state);
		    }  else if(ke.equals("up")) {
			Player attacking = player.attack(Direction.UP);
			EnemyList attacked = enemies.attacked(attacking, floor);
		        Effect nextEffect = new Effect(new Point(player.getLocation().x, player.getLocation().y),
						       player.getWeapon().upEffect(), 1, player.getWeapon().getRange(), Direction.UP);
			return new Crawl(level, attacking, floor, attacked, nextEffect, items, selectedItem, exit, state);
		    }  else if(ke.equals("right")) {
			Player attacking = player.attack(Direction.RIGHT);
			EnemyList attacked = enemies.attacked(attacking, floor);
		        Effect nextEffect = new Effect(new Point(player.getLocation().x+1, player.getLocation().y),
						       player.getWeapon().rightEffect(), player.getWeapon().getRange(), 1, Direction.RIGHT);
			return new Crawl(level, attacking, floor, attacked, nextEffect, items, selectedItem, exit, state);
		    } else if(ke.equals("down")) {
			Player attacking = player.attack(Direction.DOWN);
			EnemyList attacked = enemies.attacked(attacking, floor);
		        Effect nextEffect = new Effect(new Point(player.getLocation().x, player.getLocation().y+1),
						       player.getWeapon().downEffect(), 1, player.getWeapon().getRange(), Direction.DOWN);
			return new Crawl(level, attacking, floor, attacked, nextEffect, items, selectedItem, exit, state);
		    }
		} catch(NegativeManaException e) {
		    return this;
		}
	    }
	    return this;
	} else if(state == State.INVENTORY) {
	    if(ke.equals("e")) {
		return new Crawl(level, player, floor, enemies, items, selectedItem, exit, State.GAME);
	    } else if((ke.equals("up") || ke.equals("w")) && selectedItem >= 5) {
		return new Crawl(level, player, floor, enemies, items, selectedItem-5, exit, state);
	    } else if((ke.equals("right") || ke.equals("d")) && selectedItem < 9) {
		return new Crawl(level, player, floor, enemies, items, selectedItem+1, exit, state);
	    } else if((ke.equals("down") || ke.equals("s")) && selectedItem < 5) {
		return new Crawl(level, player, floor, enemies, items, selectedItem+5, exit, state);
	    } else if((ke.equals("left") || ke.equals("a")) && selectedItem > 0) {
		return new Crawl(level, player, floor, enemies, items, selectedItem-1, exit, state);
	    } else if(ke.equals(" ") &&  selectedItem <= player.getInventory().size()-1) {
		Carryable item = player.getInventory().get(selectedItem);
		return new Crawl(level, item.use(player), floor, enemies, items, selectedItem, exit, state);
	    } else {
		return this;
	    }
	} else if(state == State.GAMEOVER || state == State.WIN) {
	    if(ke.equals("n")) {
		return newGame();
	    } else {
		return this;
	    }
	} else {
	    return this;
	}
    }

    public WorldImage makeImage() {
	if(state == State.GAME) {
	    int playerDrawX = WINDOW_WIDTH/2;
	    int playerDrawY = WINDOW_HEIGHT/2;
	    WorldImage playerImage = player.draw().getMovedTo(new Posn(playerDrawX*Cell.CELL_WIDTH+Cell.CELL_WIDTH/2, playerDrawY*Cell.CELL_WIDTH+Cell.CELL_HEIGHT/2));
	    int xDiff = player.getLocation().x - playerDrawX;
	    int yDiff = player.getLocation().y - playerDrawY;
	    WorldImage enemiesDrawMoved = enemies.draw().getMovedImage(-xDiff*Cell.CELL_WIDTH, -yDiff*Cell.CELL_HEIGHT);
	    WorldImage floorDrawMoved = this.drawCells().getMovedImage(-xDiff*Cell.CELL_WIDTH, -yDiff*Cell.CELL_HEIGHT);
	    WorldImage itemsDrawMoved = items.drawInWorld().getMovedImage(-xDiff*Cell.CELL_WIDTH, -yDiff*Cell.CELL_HEIGHT);
	    WorldImage healthFrame = new FrameImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/8, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/8),
						    player.getMaxHealth()*3, Cell.CELL_HEIGHT, Color.RED);
	    WorldImage healthFill = new RectangleImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/8, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/8),
						       player.getHealth()*3,
						       Cell.CELL_HEIGHT,
						       Color.RED).getMovedImage((int) (-1.5*(player.getMaxHealth()-player.getHealth())), 0);
	    WorldImage manaFrame = new FrameImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH*7)/8, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/8),
						    player.getMaxMana()*3, Cell.CELL_HEIGHT, Color.BLUE);
	    WorldImage manaFill = new RectangleImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH*7)/8, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/8),
						       player.getMana()*3,
						       Cell.CELL_HEIGHT,
						     Color.BLUE).getMovedImage((int) (1.5*(player.getMaxMana()-player.getMana())), 0);
	    WorldImage levelCount = new TextImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, Cell.CELL_WIDTH*1), "Level: " + (level+1), 15, 1, 
						  Color.RED);
	    WorldImage background = new RectangleImage(playerImage.pinhole,
						       //Temporary fix, find out what is causing the offset
						       WINDOW_WIDTH*Cell.CELL_WIDTH+Cell.CELL_WIDTH,
						       WINDOW_HEIGHT*Cell.CELL_HEIGHT+Cell.CELL_HEIGHT,
						       Color.BLACK);
	    WorldImage drawEffect;
	    if(effect != null) {
		drawEffect = effect.draw(player).getMovedImage(-xDiff*Cell.CELL_WIDTH, -yDiff*Cell.CELL_HEIGHT);
	    } else {
		drawEffect = new RectangleImage(new Posn(0,0), 0, 0, Color.BLACK);
	    }
	    return new OverlayImages(background,
			 new OverlayImages(floorDrawMoved,
			   new OverlayImages(itemsDrawMoved,
			     new OverlayImages(enemiesDrawMoved,
			       new OverlayImages(drawEffect,
			        new OverlayImages(healthFrame,
				 new OverlayImages(healthFill,
				   new OverlayImages(manaFrame,
				    new OverlayImages(manaFill,
				      new OverlayImages(levelCount, playerImage))))))))));
	} else if(state == State.INVENTORY) {
	    int xSpace = (Cell.CELL_WIDTH*Crawl.WINDOW_WIDTH)/5;
	    int ySpace = (Cell.CELL_HEIGHT*Crawl.WINDOW_HEIGHT)/2;
	    int x;
	    int y;
	    if(selectedItem < 5) {
		x = xSpace*selectedItem + xSpace/2;
		y = ySpace/2;
	    } else {
		x = xSpace*(selectedItem-5) + xSpace/2;
		y = ySpace+ySpace/2;
	    }
	    WorldImage selection = new FrameImage(new Posn(x,y), xSpace, ySpace, Color.BLACK);
	    WorldImage background = new RectangleImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/2),
						       WINDOW_WIDTH*Cell.CELL_WIDTH, WINDOW_HEIGHT*Cell.CELL_HEIGHT, new Color(192,117,42));
	    return new OverlayImages(background, new OverlayImages(selection, player.getInventory().drawInInventory()));
	} else if(state == State.GAMEOVER) {
	    WorldImage background = new RectangleImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/2),
						       WINDOW_WIDTH*Cell.CELL_WIDTH, WINDOW_HEIGHT*Cell.CELL_HEIGHT, Color.BLACK);
	    WorldImage gameOver = new TextImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/3),
						"You got to level " + (level+1) + ". Just " + (WIN_LEVEL-level) + " more to go next time!",
						18, 1, Color.RED);
	    WorldImage restart = new TextImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/2),
					     "Press N to start a new game!", 15, Color.RED);
	    return new OverlayImages(background, new OverlayImages(gameOver, restart));
						
	} else {
	    WorldImage background = new RectangleImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/2),
						       WINDOW_WIDTH*Cell.CELL_WIDTH, WINDOW_HEIGHT*Cell.CELL_HEIGHT, Color.BLACK);
	    WorldImage victory = new TextImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/3),
						"Congratulations! You escaped the dungeon and beat the game!",
						20, 1, Color.RED);
	    WorldImage restart = new TextImage(new Posn((WINDOW_WIDTH*Cell.CELL_WIDTH)/2, (WINDOW_HEIGHT*Cell.CELL_HEIGHT)/2),
					     "Press N to play again!", 15, Color.RED);
	    return new OverlayImages(background, new OverlayImages(victory, restart));
	}
    }

    public static void main(String[] args) {

	
	
	Dungeon gameDungeon = new BaseDungeon(new Point(0,0), 80, 80, new EmptyPtList()).generate(10,10);
	Cell[][] floor = gameDungeon.getCells();
        Player p = new Player(findTopLeft(floor), Direction.RIGHT, 50, 50, 50, 50, "",
			      new EmptyList().add(Weapon.sword(new Point(0,0))), Weapon.sword(new Point(0,0)));
	EnemyList es =  generateEnemies(floor, NUM_ENEMIES);
	EquipList items = generateItems(floor, NUM_ITEMS);
	Point exit = findBottomRight(floor);  
	//Crawl game = newGame();
	Crawl game = new Crawl(0, p, floor, es, items, 0, exit, State.GAME);
	game.bigBang(WINDOW_WIDTH*Cell.CELL_WIDTH, WINDOW_HEIGHT*Cell.CELL_HEIGHT, 0.5);
    }
}

class CrawlTests {



    //---//---// INVARIANT TESTS //---//---//

    //If the game is in the GAME state and an enemy is next to a player,
    //on the next tick, the player should be in a different position, and have less
    //health than before
    public static boolean testPlayerHit(Crawl game) {
	Point testPointLeft = new Point(game.getPlayer().getLocation().x - 1, game.getPlayer().getLocation().y);
	Point testPointRight = new Point(game.getPlayer().getLocation().x + 1, game.getPlayer().getLocation().y );
	Point testPointUp = new Point(game.getPlayer().getLocation().x, game.getPlayer().getLocation().y - 1);
	Point testPointDown = new Point(game.getPlayer().getLocation().x, game.getPlayer().getLocation().y + 1);
	if(game.getState() == State.GAME && game.onTick().getState() == State.GAME &&
	   (game.getEnemies().isEnemyAt(testPointLeft) || game.getEnemies().isEnemyAt(testPointRight) ||
	    game.getEnemies().isEnemyAt(testPointUp) || game.getEnemies().isEnemyAt(testPointDown))) {
	    return !(game.getPlayer().getLocation().equals(game.onTick().getPlayer().getLocation())) &&
		game.getPlayer().getHealth() > game.onTick().getPlayer().getHealth();
	} else {
	    return true;
	}
    }

    //No enemy, player, item, or exit should ever be in a cell that is not open
    public static boolean testValidCells(Crawl game) {
	boolean valid = true;
	for(int i = 0; i < game.getFloor().length; i++) {
	    for(int j = 0; j < game.getFloor()[0].length; j++) {
		if(!(Crawl.validCell(game.getPlayer().getLocation(), game.getFloor()) &&
		     Crawl.validCell(game.getExit(), game.getFloor()) &&
		     game.getEnemies().validCells(game.getFloor()) &&
		     game.getItems().validCells(game.getFloor()))) {
		    valid = false;
		}
	    }
	}
	return valid;
    }

    //If the game is in the GAME state and the player has 0 health, the game
    //should be in the GAMEOVER state on the following tick
    public static boolean testGameOver(Crawl game) {
	if(game.getState() == State.GAME && game.getPlayer().getHealth() <= 0) {
	    return game.onTick().getState() == State.GAMEOVER;
	} else {
	    return true;
	} 
    }

    //The player should never have more than 10 items in their inventory
    public static boolean testInventoryLimit(Crawl game) {
	return game.getPlayer().getInventory().size() <= 10;
    }

    //If the player is standing on top of an item, and their inventory is NOT full,
    //on the next tick, that item should be in the player's inventory, and should no
    //longer be at that point in the world
    public static boolean testItemPickup(Crawl game) {
	if(game.getItems().isItemAt(game.getPlayer().getLocation()) && game.getPlayer().getInventory().size() < 10) {
	    return !(game.onTick().getItems().isItemAt(game.getPlayer().getLocation())) &&
		(game.onTick().getPlayer().getInventory().isItem(game.getItems().getItemAt(game.getPlayer().getLocation())));
	} else {
	    return true;
	}
    }

    public static Point getRandomOpen(Cell[][] cells) {
	Point pt = null;
	int x;
	int y;
	while(pt == null) {
	    x = (int) (Math.random()*cells.length);
	    y = (int) (Math.random()*cells[0].length);
	    if(cells[x][y].isOpenHuh()) {
		pt = new Point(x,y);
	    }
	}
	return pt;
    }

    //There should exist a path between any two arbitrarily chosen open points
    //in the dungeon
    public static boolean testDungeonComplete(Crawl game) {
	Point pt1 = getRandomOpen(game.getFloor());
	Point pt2 = getRandomOpen(game.getFloor());
	return pt1.isPath(pt2, game.getFloor());
    }
    
    public static String getRandomKey() {
	int choose = (int) (Math.random()*11);
	if(choose == 0) {
	    return "w";
	} else if(choose == 1) {
	    return "a";
	} else if(choose == 2) {
	    return "s";
	} else if(choose == 3) {
	    return "d";
	} else if(choose == 4) {
	    return "up";
	} else if(choose == 5) {
	    return "right";
	} else if(choose == 6) {
	    return "up";
	} else if(choose == 7) {
	    return "down";
	} else if(choose == 8) {
	    return " ";
	} else if(choose == 9) {
	    return "e";
	} else {
	    return "n";
	}
    }

    public static String[] genRandomInputs(int n) {
	String[] inputs =  new String[n];
	for(int i = 0; i< inputs.length; i++) {
	    inputs[i] = getRandomKey();
	}
	return inputs;
    }
    
    public static void main(String[] args) {

	//---//---// SINGLE CASE TESTS //---//---//
    
	PointList mt = new EmptyPtList();
	PointList listy = new ConsPtList(new Point(1, 1), (new ConsPtList(new Point(2,2), (new ConsPtList(new Point(3,3), mt)))));
	System.out.println("listy.rightOf(2) should be ((2,2) ((3,3) )), is:\n" + listy.rightOf(2));
	System.out.println("listy.leftOf(2) should be ((1,1) ((2,2) )), is:\n" + listy.leftOf(2));
	System.out.println("listy.above(2) should be ((1,1) ((2,2) )), is:\n" + listy.above(2));
	System.out.println("listy.below(2) should be ((2,2) ((3,3) )), is:\n" + listy.below(2));
	System.out.println("listy.topMost() should be (1,1), is:\n" + listy.topMost());
	System.out.println("listy.bottomMost() should be (3,3), is:\n" + listy.bottomMost());
	System.out.println("listy.leftMost() should be (1,1), is:\n" + listy.leftMost());
	System.out.println("listy.rightMost() should be 3,3), is:\n" + listy.rightMost());

	Dungeon testRoom = new Room(new Point(0, 0), 10, 6, new Point(3, 2), 4, 2,
				    new EmptyPtList().add(new Point(0,1)).add(new Point(4,0)).add(new Point(0,3)).add(new Point(10,3)).add(new Point(5,6)).add(new Point(0,4)).add(new Point(7,0)));
	System.out.println(testRoom);
	System.out.println("\n");
	Dungeon left = new Room(new Point(0,0), 3, 5, new Point(0,0), 2, 5, new EmptyPtList().add(new Point(3,2)));
	Dungeon right = new Room(new Point(3,0), 3,5,new Point(4,0), 2, 5, new EmptyPtList().add(new Point(3,2)));
	Dungeon testXSplit = new XSplit(new Point(0,0), 6, 5, 3, left, right, new EmptyPtList().add(new Point(3,2)));
	System.out.println(testXSplit);
	System.out.println("\n");
	System.out.println("Search Algorithm testing\n");
	SearchPoint start = new SearchPoint(new Point(0,0), null);
	Point end = new Point(5,4);
	SearchList open = new EmptySearchList().add(start);
	SearchList closed = new EmptySearchList();
	System.out.println("Path from top left to bottom right is composed of the following points:\n" + 
			   start.findPath(end, testXSplit.getCells(), open, closed)); 
	System.out.println("\n");
	Dungeon testDungeon = new BaseDungeon(new Point(0,0), 40, 30, new EmptyPtList());
	testDungeon = testDungeon.generate(6, 6);
	System.out.println(testDungeon + "\n");

	//---//---// INVARIANT TESTS //---//---//
	
	Crawl testGame = Crawl.newGame();
	int times = 3000;
	String[] inputs = genRandomInputs(times);
	int testsFailed = 0;
	if(!testDungeonComplete(testGame)) {
	    System.out.println("Path could not be found between two points");
	    testsFailed++;
	}
	for(int i = 0; i < times*2; i++) {
	    if(i % 2 == 0) {
		testGame = testGame.onKeyEvent(inputs[i/2]);
	    } else {
		testGame = testGame.onTick();
	    }
	    if(!testPlayerHit(testGame)) {
		System.out.println("Enemies are not correctly attacking the player\nenemies are at:\n" +
				   testGame.getEnemies().getLocations() + "\nplayer is at: "
				   + testGame.getPlayer().getLocation());
		testsFailed++;
	    }
	    if(!testValidCells(testGame)) {
		System.out.println("Something is occupying an \"off\" cell");
		testsFailed++;
	    }
	    if(!testGameOver(testGame)) {
		System.out.println("Game is not ending when the player has no life");
		testsFailed++;
	    }
	    if(!testInventoryLimit(testGame)) {
		System.out.println("Player has too many items in their inventory");
		testsFailed++;
	    }
	    if(!testItemPickup(testGame)) {
		System.out.println("Item is not being picked up when it should be");
		testsFailed++;
	    }
	}
	System.out.println(testsFailed + " tests failed");
    }
}


