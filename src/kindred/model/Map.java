package kindred.model;


/*
 *  Handles events related to a Map in the Game.
 *  A Map is divided in many Tiles.
 */
public class Map {

    private final Tile[][] tiles; // Map is represented as a Tile matrix
    private final int tileHeight; // Graphical height representation for each
                                  // tile
    private final int tileWidth; // Graphical width representation for each tile
    private final Battle battle; // Calculates result of battle between Units

    // Creates a Map with the specified parameters
    public Map(Tile[][] tiles, int tileHeight, int tileWidth) {
        this.tiles = tiles;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.battle = new Battle();
    }

    /*
     * Adds a unit to Tile (x, y) on the Map. Returns 'true' if placement was
     * successful or 'false' otherwise.
     */
    public boolean placeUnit(Unit unit, int x, int y) {
        // Tile already occupied
        if (tiles[x][y].getUnit() != null)
            return false;

        tiles[x][y].setUnit(unit);
        return true;
    }

    /*
     * Moves Unit on Tile (xi, yi) to Tile (xf, yf) on the Map. Returns 'true'
     * if movement was successful or 'false' otherwise.
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
        int move = unit.getMove();

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

    /*
     * Makes the Unit on Tile (xi, yi) attack the Unit on Tile (xf, yf). Returns
     * damage caused to defending Unit.
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
}
