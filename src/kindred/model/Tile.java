package kindred.model;

/**
 * Representing a piece of the Map, contains a Terrain and may hold up to one
 * Unit.
 * 
 * @author Kindred Team
 */
public class Tile {

    /**
     * Unchanging Terrain occurring in the Tile.
     */
    private final Terrain terrain;

    /**
     * Unit currently occupying the Tile, if there is any. Otherwise, has value
     * <null>.
     */
    private Unit unit;

    /**
     * Constructs a Tile.
     * 
     * @param terrain
     *            Terrain object occurring in the Tile
     */
    public Tile(Terrain terrain) {
        this.terrain = terrain;
        this.unit = null;
    }

    /**
     * Returns the Terrain used in the Tile.
     * 
     * @return the Terrain used in the Tile
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Returns the Unit contained in the Tile. If no Unit exists, returns
     * <null>.
     * 
     * @return the Unit contained in Tile, if it exists, or <null> otherwise
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the Unit standing on the Tile.
     * 
     * @param unit
     *            Unit to be inserted on the Tile
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Removes and returns the Unit contained in Tile. If no Unit exists,
     * returns <null>.
     * 
     * @return the Unit contained in Tile, if it exists, or <null> otherwise
     */
    public Unit removeUnit() {
        Unit u = unit;
        this.unit = null;
        return u;
    }
}
