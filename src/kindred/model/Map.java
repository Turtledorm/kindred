package kindred.model;

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
        // Tile already occupied
        if (tiles[x][y].getUnit() != null)
            return false;

        tiles[x][y].setUnit(unit);
        return true;
    }

    /**
     * Moves Unit on Tile (xi, yi) to Tile (xf, yf) on this Map. Supposes that
     * all given coordinates are valid, i.e., not outside borders of this Map.
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
    public boolean move(int team, int xi, int yi, int xf, int yf) {
        // Tile already occupied
        if (tiles[xf][yf].getUnit() != null)
            return false;

        Unit unit = tiles[xi][yi].getUnit();
        if (unit == null || unit.getTeam() != team)
            return false;

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
     * Makes the Unit on Tile (xi, yi) attack the Unit on Tile (xf, yf) during
     * the specified team's turn. Returns an integer representing damage:
     * <ul>
     * <li>If damage = -1, then the attack missed.</li>
     * <li>If damage &ge; 0, then it represents the damage received by the
     * defending Unit.</li>
     * </ul>
     * 
     * @param team
     *            number identifier of the player whose turn the game is
     *            currently on
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
    public int attack(int team, int xi, int yi, int xf, int yf) {
        Unit attacker = tiles[xi][yi].getUnit();
        Unit defender = tiles[xf][yf].getUnit();

        // Checks if Units exists on specified Tiles
        // and disallows friendly fire
        if (attacker == null || defender == null || attacker.getTeam() != team
                || defender.getTeam() == team)
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
     * Returns a String containing information about the Unit and Terrain on
     * Tile (x, y).
     * 
     * @return a String containing information about the specified Tile
     */
    public String getTileInfo(int x, int y) {
        String message = "";
        Tile tile = tiles[x][y];
        Unit unit;
        // TODO: Change this to a syntax
        if ((unit = tile.getUnit()) == null)
            message += "[No unit]";
        else {
            message += unit.getName() + " [" + unit.getCurrentHp() + "/"
                    + unit.getTotalHp() + " HP]";
            message += "\n Mov: " + unit.getMove();
            message += "\n Rng: " + unit.getRange();
            message += "\n Atk: " + unit.getAtk();
            message += "\n Def: " + unit.getDef();
            message += "\n Agi: " + unit.getAtk();
        }
        message += "\n" + tile.getTerrain().getName();
        message += String.format(" (%+d%% Def, %+d%% Agi, -%d Move)",
                tile.getTerrain().getDefenseModifier(),
                tile.getTerrain().getAgilityModifier(),
                tile.getTerrain().getMovePenalty());

        return message;
    }
}
