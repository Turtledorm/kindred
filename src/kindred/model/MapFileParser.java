package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapFileParser {
    public static Map parseFile(String filename) throws FileNotFoundException {
        File f = new File(filename);
        Scanner scanner = new Scanner(f);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        int height = scanner.nextInt();
        int width = scanner.nextInt();
        Tile[][] tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char x = (char) scanner.nextByte();
                // TODO: check if valid
                tiles[i][j] = new Tile("tile-name", 'f', 1, 1);
            }
        }
        scanner.close();
        Map map = new Map();
        return null;

    }
}
