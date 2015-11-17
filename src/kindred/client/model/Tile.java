package kindred.client.model;

/**
 * Representing a piece of the Map, contains a Terrain and may hold up to one
 * Unit.
 * 
 * @author Kindred Team
 */
public class Tile {

    /**
     * Unchanging Terrain occurring in this Tile.
     */
    private final Terrain terrain;

    /**
     * Unit currently occupying this Tile, if any. Otherwise, has value
     * {@code null}.
     */
    private Unit unit;

    /**
     * Constructs a Tile, initially unoccupied by any Unit.
     * 
     * @param terrain
     *            Terrain object occurring in this Tile
     */
    public Tile(Terrain terrain) {
        this.terrain = terrain;
        this.unit = null;
    }

    /**
     * Returns the Terrain used in this Tile.
     * 
     * @return the Terrain used in this Tile
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Returns the Unit contained in this Tile. If no Unit exists, returns
     * {@code null}.
     * 
     * @return the Unit contained in Tile, if it exists, or {@code null}
     *         otherwise
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the Unit standing on this Tile.
     * 
     * @param unit
     *            Unit to be inserted on this Tile
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Removes and returns the Unit contained in this Tile. If no Unit exists,
     * returns {@code null}.
     * 
     * @return the Unit contained in this Tile, if it exists, or {@code null}
     *         otherwise
     */
    public Unit removeUnit() {
        Unit u = unit;
        this.unit = null;
        return u;
    }
}
