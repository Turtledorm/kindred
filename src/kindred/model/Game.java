package kindred.model;

import java.io.FileNotFoundException;

public class Game {

    // Currently used for testing Map generation
    public void start() {
        String terrainFile = "kindred/data/terrain.txt";
        String mapFile = "kindred/data/simpleMap.txt";

        TerrainFileParser terrainParser = new TerrainFileParser();
        MapFileParser mapParser = new MapFileParser();

        try {
            Map map = mapParser.parseFile(mapFile,
                                          terrainParser.parseFile(terrainFile));
        }
        catch (FileNotFoundException e) {
            System.err.format("Arquivo não encontrado!\n");
        }
    }
}
