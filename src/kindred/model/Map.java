package kindred.model;

public class Map {

    private final Tile[][] tiles;
    private final int tileHeight;
    private final int tileWidth;

    public Map(Tile[][] tiles, int tileHeight, int tileWidth) {
        this.tiles = tiles;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }
}
