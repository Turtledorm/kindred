package kindred.model;

/**
 * Handles events related to a Map in the Game. A Map is divided in many Tiles.
 * 
 * @author Kindred Team
 */
public class Map {

    /**
     * Representation of the Map as a 2x2 Tile matrix.
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
     * Calculates results of battles between Units.
     */
    private final Battle battle;

    /**
     * Constructs a Map and initializes Battle module.
     * 
     * @param tiles
     *            2x2 Tile matrix. All Tiles are supposed to hold no Unit
     * @param tileHeight
     *            Graphical height of each Tile
     * @param tileWidth
     *            Graphical width of each Tile
     */
    public Map(Tile[][] tiles, int tileHeight, int tileWidth) {
        this.tiles = tiles;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.battle = new Battle();
    }

    /**
     * Adds a Unit to Tile (x, y) on the Map. Returns <true> if placement was
     * successful or <false> otherwise.
     * 
     * @param unit
     *            Unit to be inserted
     * @param x
     *            x coordinate of the Tile
     * @param y
     *            y coordinate of the Tile
     * 
     * @return <true>, if placement was successful, or <false> otherwise
     */
    public boolean placeUnit(Unit unit, int x, int y) {
        // Tile already occupied
        if (tiles[x][y].getUnit() != null)
            return false;

        tiles[x][y].setUnit(unit);
        return true;
    }

    /**
     * Moves Unit on Tile (xi, yi) to Tile (xf, yf) on the Map. Supposes that
     * all given coordinates are valid, i.e., not outside borders of the Map.
     * <p>
     * Returns <true> if movement was successful or <false> otherwise.
     * 
     * @param xi
     *            x coordinate of the Unit to be moved
     * @param yi
     *            y coordinate of the Unit to be moved
     * @param xf
     *            x coordinate of the destination Tile
     * @param yf
     *            y coordinate of the destination Tile
     * @return <true>, if movement was successful, or <false> otherwise
     */
    public boolean move(int xi, int yi, int xf, int yf) {
        // Tile already occupied
        if (tiles[xf][yf].getUnit() != null)
            return false;

        Unit unit = tiles[xi][yi].getUnit();
        if (unit == null)
            return false;

        // TODO: Check Player the unit belongs to

        // TODO: If necessary, change name of Unit method
        int move = unit.getMove() - tiles[xi][yi].getTerrain().getMovePenalty();
        if (move <= 0)
            move = 1;

        // Calculate x/y distance from starting Tile to destination Tile
        int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);

        // Disallow movement to a tile further than Unit's 'move'
        if (dx + dy > move)
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
        Unit attacker = tiles[xi][yi].getUnit();
        Unit defender = tiles[xf][yf].getUnit();
        if (attacker == null || defender == null)
            return -1;

        // TODO: Check Players the units belongs to

        // TODO: If necessary, change name of Unit/Weapon methods
        int range = attacker.getWeapon().getRange();

        // Calculate x/y distance between Units
        int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);

        // Disallow attack if distance in greater than range
        if (dx + dy > range)
            return -1;

        int damage = battle.execute(tiles[xi][yi], tiles[xf][yf]);

        return damage;
    }

    /**
     * Returns <true> if the given Tile (x, y) exists in the Map, or <false>
     * otherwise.
     * 
     * @param x
     *            x coordinate of the Tile to be verified
     * @param y
     *            y coordinate of the Tile to be verified
     * 
     * @return <true>, if the Tile is valid, or <false> otherwise
     */
    public boolean validPosition(int x, int y) {
        return x >= 0 && x < getHeight() && y >= 0 && y < getWidth();
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
     * Returns the height of the Map, i.e., the number of lines in the Tile
     * matrix.
     * 
     * @return height of the Map
     */
    public int getHeight() {
        return tiles.length;
    }

    /**
     * Returns the width of the Map, i.e., the number of columns in the Tile
     * matrix.
     * 
     * @return width of the Map
     */
    public int getWidth() {
        return tiles[0].length;
    }

    /**
     * Returns a String containing information about the Unit and Terrain on
     * Tile (x, y).
     * 
     * @return a String containing information about the specified Tile
     */
    public String getTileInfo(int x, int y) {
        String message = "";
        Tile tile = tiles[x][y];
        Unit unit;

        if ((unit = tile.getUnit()) == null)
            message += "[No unit]";
        else {
            Weapon weapon = unit.getWeapon();
            message += unit.getName() + " the " + unit.getUnitClass().getName();
            message += unit.getCurrentHp() + "/" + unit.getTotalHp() + " HP";
            message += "\n Mov: " + unit.getMove();
            message += "\n Atk: " + unit.getAtk();
            message += "\n Def: " + unit.getDef();
            message += "\n Agi: " + unit.getAtk();
            message += "\n Weapon: " + weapon.getName();
            message += "\n   (Power = " + weapon.getPower() + ", Range = "
                    + weapon.getRange() + ")\n";
        }
        message += "\n" + tile.getTerrain().getName();
        message += String.format(" (%+d%% Def, %+d%% Agi, -%d Move)", tile
                .getTerrain().getDefenseModifier(), tile.getTerrain()
                .getAgilityModifier(), tile.getTerrain().getMovePenalty());

        return message;
    }
}
