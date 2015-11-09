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
     * an integer representing damage: - If damage = -1, then the attacked
     * missed. - If damage >= 0, then it represents the damage received by the
     * defending Unit.
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

    public boolean validPosition(int i, int j) {
        return i >= 0 && i < getHeight() && j >= 0 && j < getWidth();
    }

    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    public int getHeight() {
        return tiles.length;
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public String getTileInfo(int i, int j) {
        String message = "";
        Tile tile = tiles[i][j];
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
        message += String.format(" (%+d%% Def, %+d%% Agi)", tile.getTerrain()
                .getDefenseModifier(), tile.getTerrain().getAgilityModifier());

        return message;
    }
}
