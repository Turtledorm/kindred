package kindred.model;

import java.io.FileNotFoundException;

public class Game {

    private int localPlayerUnits, remotePlayerUnits;

    public Game() {

    }

    // Currently used for testing Map generation
    public void start() {
        String terrainFile = "kindred/data/terrain.txt";
        String mapFile = "kindred/data/simpleMap.txt";

        try {
            Map map = MapFileParser.parseFile(mapFile,
                    TerrainFileParser.parseFile(terrainFile));
        } catch (FileNotFoundException e) {
            System.err.format("Arquivo não encontrado!\n");
        }
    }
}
