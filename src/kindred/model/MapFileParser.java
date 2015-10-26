package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MapFileParser {

    private MapFileParser() {
        // Can't be instantiated
    }

    public static Map parseFile(String filename, HashMap<Character, TileInfo> hashMap)
            throws FileNotFoundException {
        File f = new File(filename);
        Scanner scanner = new Scanner(f);

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        int tileHeight = scanner.nextInt();
        int tileWidth = scanner.nextInt();
        Tile[][] tiles = new Tile[rows][cols];

        Pattern p = Pattern.compile("\\s*\\w");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!scanner.hasNext(p)) {
                    System.err.format("Linha inválida em '%s'\n", filename);
                    scanner.close();
                    return null;
                }
                char c = scanner.next(p).trim().charAt(0);
                if (hashMap.containsKey(c))
                    tiles[i][j] = new Tile("tile-name", 'f', 1, 1);
                else {
                    System.err.format("Linha inválida em '%s'\n", filename);
                    scanner.close();
                    return null;
                }
            }
        }

        if (scanner.hasNext(p)) {
            System.err.format("Informação restando no final de '%s'\n", filename);
        }
        scanner.close();
        Map map = new Map(tiles, tileHeight, tileWidth);
        return map;
    }
}
