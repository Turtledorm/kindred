package kindred.model;

/*
 *  Handles events related to a Map in the Game.
 *  A Map is divided in many Tiles.
 */
public class Map {

    private final Tile[][] tiles; // Map is represented as a Tile matrix
    private final int tileHeight; // Graphical height representation for each tile
    private final int tileWidth;  // Graphical width representation for each tile

    // Creates a Map with the specified parameters
    public Map(Tile[][] tiles, int tileHeight, int tileWidth) {
        this.tiles = tiles;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }
}
