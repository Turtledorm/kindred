package kindred.client.model;

/**
 * Handles events related to a Map in the Game. A Map is divided into many
 * Tiles.
 * 
 * @author Kindred Team
 */
public class Map {

    /**
     * Representation of this Map as a 2x2 Tile matrix.
     */
    private final Tile[][] tiles;

    /**
     * Graphical measurement of the height of each Tile.
     */
    private final int tileHeight;

    /**
     * Graphical measurement of the width of each Tile.
     */
    private final int tileWidth;

    /**
     * Number of units that the first player has on the Map.
     */
    private int numUnitsA;

    /**
     * Number of units that the second player has on the Map.
     */
    private int numUnitsB;

    /**
     * Calculates results of battles between Units.
     */
    private final Battle battle;

    /**
     * Constructs a Map and initializes Battle module.
     * 
     * @param tiles
     *            2x2 Tile matrix. All Tiles are supposed to initially hold no
     *            Unit
     * @param tileHeight
     *            graphical height of each Tile
     * @param tileWidth
     *            graphical width of each Tile
     */
    public Map(Tile[][] tiles, int tileHeight, int tileWidth) {
        this.tiles = tiles;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.battle = new Battle();
        numUnitsA = numUnitsB = 0;
    }

    /**
     * Returns {@code true} if the given Tile (x, y) exists in this Map, or
     * {@code false} otherwise.
     * 
     * @param x
     *            x coordinate of the Tile to be verified
     * @param y
     *            y coordinate of the Tile to be verified
     * 
     * @return {@code true}, if the Tile is valid, or {@code false} otherwise
     */
    public boolean validPosition(int x, int y) {
        return x >= 0 && x < getMapHeight() && y >= 0 && y < getMapWidth();
    }

    /**
     * Adds a Unit to Tile (x, y) on this Map. Returns {@code true} if placement
     * was successful or {@code false} otherwise.
     * 
     * @param unit
     *            Unit to be inserted
     * @param x
     *            x coordinate of the Tile
     * @param y
     *            y coordinate of the Tile
     * 
     * @return {@code true}, if placement was successful, or {@code false}
     *         otherwise
     */
    public boolean placeUnit(Unit unit, int x, int y) {
        // Invalid unit/position or Tile already occupied
        if (unit == null || !validPosition(x, y) || tiles[x][y].getUnit() != null)
            return false;

        tiles[x][y].setUnit(unit);
        if (unit.getTeam() == 1)
            numUnitsA++;
        else
            numUnitsB++;

        return true;
    }

    /**
     * Moves Unit from the specified team on Tile (xi, yi) to Tile (xf, yf) on
     * this Map.
     * <p>
     * Returns {@code true} if movement was successful or {@code false}
     * otherwise.
     * 
     * @param xi
     *            x coordinate of the Unit to be moved
     * @param yi
     *            y coordinate of the Unit to be moved
     * @param xf
     *            x coordinate of the destination Tile
     * @param yf
     *            y coordinate of the destination Tile
     * @return {@code true}, if movement was successful, or {@code false}
     *         otherwise
     */
    public boolean move(int xi, int yi, int xf, int yf) {
        // Verify if coordinates are out of bounds
        if (!validPosition(xi, yi) || !validPosition(xf, yf))
            return false;

        // Tile already occupied
        if (tiles[xf][yf].getUnit() != null)
            return false;

        Unit unit = tiles[xi][yi].getUnit();

        if (unit == null)
            return false;

        int move = unit.getMove() - tiles[xi][yi].getTerrain().getMovePenalty();
        if (move <= 0)
            move = 1;

        // Calculate x/y distance from starting Tile to destination Tile
        int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);

        // Disallow movement to own Tile or to a Tile further than Unit's 'move'
        if (dx + dy == 0 || dx + dy > move)
            return false;

        // Successfully moves unit
        tiles[xi][yi].removeUnit();
        tiles[xf][yf].setUnit(unit);

        return true;
    }

    /**
     * Makes the Unit on Tile (xi, yi) attack the Unit on Tile (xf, yf). Returns
     * an integer representing damage:
     * <ul>
     * <li>If damage = -1, then the attack missed.</li>
     * <li>If damage &ge; 0, then it represents the damage received by the
     * defending Unit.</li>
     * </ul>
     * 
     * @param xi
     *            x coordinate of the attacker
     * @param yi
     *            y coordinate of the attacker
     * @param xf
     *            x coordinate of the defender
     * @param yf
     *            y coordinate of the defender
     * @return damage received by the defending unit if the attack succeeded, or
     *         -1 otherwise
     */
    public int attack(int xi, int yi, int xf, int yf) {
        // Verify if coordinates are out of bounds
        if (!validPosition(xi, yi) || !validPosition(xf, yf))
            return -1;

        Unit attacker = tiles[xi][yi].getUnit();
        Unit defender = tiles[xf][yf].getUnit();

        if (attacker == null || defender == null)
            return -1;

        int range = attacker.getRange();

        // Calculate x, y distance between Units
        int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);

        // Disallow attack if distance in greater than range
        if (dx + dy > range)
            return -1;

        int damage = battle.execute(tiles[xi][yi], tiles[xf][yf]);

        return damage;
    }

    /**
     * Directly causes the specified amount of damage to the Unit on Tile (x, y)
     * of the Map. Returns {@code true} if the Unit is dead, i.e., has no more
     * hit points left, or {@code false} otherwise.
     * 
     * @param x
     *            x coordinate of the Unit that will take damage
     * @param y
     *            y coordinate of the Unit that will take damage
     * @param damage
     *            damage to be caused to the Unit
     * @return {@code true}, if the Unit is dead, or {@code false} otherwise
     */
    public boolean causeDamage(int x, int y, int damage) {
        if (!validPosition(x, y))
            return false;

        Unit unit = getTile(x, y).getUnit();
        if (unit == null)
            return false;

        unit.loseHp(damage);
        boolean isDead = unit.getCurrentHp() <= 0;
        if (isDead) {
            getTile(x, y).removeUnit();
            if (unit.getTeam() == 1)
                numUnitsA--;
            else
                numUnitsB--;
        }

        return isDead;
    }

    /**
     * Returns the Tile on position (x, y).
     * 
     * @param x
     *            x coordinate of the desired Tile
     * @param y
     *            y coordinate of the desired Tile
     * 
     * @return Tile on position (x, y)
     */
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     * Returns the height of this Map, i.e., the number of lines in the Tile
     * matrix.
     * 
     * @return height of this Map
     */
    public int getMapHeight() {
        return tiles.length;
    }

    /**
     * Returns the width of this Map, i.e., the number of columns in the Tile
     * matrix.
     * 
     * @return width of this Map
     */
    public int getMapWidth() {
        return tiles[0].length;
    }

    /**
     * Returns the height of a Tile in pixels (used by GUI).
     * 
     * @return the height of a Tile in pixels
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Returns the width of a Tile in pixels (used by GUI).
     * 
     * @return the width of a Tile in pixels
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Returns the remaining number of Units that player A has on the Map.
     * 
     * @return the number of Units of player A
     */
    public int getNumUnitsA() {
        return numUnitsA;
    }

    /**
     * Returns the remaining number of Units that player B has on the Map.
     * 
     * @return the number of Units of player B
     */
    public int getNumUnitsB() {
        return numUnitsB;
    }

    /**
     * Returns a String containing information about the Unit and Terrain on
     * Tile (x, y).
     * 
     * @param x
     *            x coordinate of the desired Tile
     * @param y
     *            y coordinate of the desired Tile
     * @return a String containing information about the specified Tile
     */
    public String getTileInfo(int x, int y) {
        String message = "";
        Tile tile = tiles[x][y];
        Terrain terrain = tile.getTerrain();
        message += terrain.getName() + ",";
        message += terrain.getDefenseModifier() + ",";
        message += terrain.getAgilityModifier() + ",";
        message += terrain.getMovePenalty();
        Unit unit = tile.getUnit();
        if (unit != null) {
            message += ";";
            message += unit.getName() + ",";
            message += unit.getCurrentHp() + ",";
            message += unit.getTotalHp() + ",";
            message += unit.getMove() + ",";
            message += unit.getRange() + ",";
            message += unit.getAtk() + ",";
            message += unit.getDef() + ",";
            message += unit.getAgi();
            message += "";
        }
        return message;
    }
}
