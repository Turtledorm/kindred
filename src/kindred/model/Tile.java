package kindred.model;

/*
 *  Representing a piece of the Map, contains a Terrain and may hold up
 *  to one Unit.
 */
public class Tile {

    private final Terrain terrain; // Fixed Terrain occurring in the Tile
    private Unit unit;             // Unit currently occupying the Tile

    // Creates a Tile with the specified Terrain
    public Tile(Terrain terrain) {
        this.terrain = terrain;
        this.unit = null;
    }

    // Returns the Terrain used in Tile
    public Terrain getTerrain() {
        return terrain;
    }

    /*
     *  Returns the Unit contained in Tile.
     *  If no Unit exists, returns 'null'.
     */
    public Unit getUnit() {
        return unit;
    }

    // Sets the Tile's Unit to be the specified parameter
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    // Removes and returns the Unit contained in Tile
    public Unit removeUnit() {
        Unit u = unit;
        this.unit = null;
        return u;
    }
}
