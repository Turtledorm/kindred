package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parses a file to read and create a Map in the game.
 * 
 * @author Kindred Team
 */
public class MapFileParser {

    /**
     * Not to be instantiated, since this class is purely <static>!
     */
    private MapFileParser() {
        // Do not instantiate!
    }

    /**
     * Parses a given file containing information about a Map. Also uses a
     * char->Terrain HashMap to understand valid Terrains for the Map.
     * <p>
     * Creates and returns a Map, according to the information contained in the
     * file.
     * 
     * @param filename
     *            Name of the Map file
     * @param hashMap
     *            HashMap containing identifying chars as keys and Terrains as
     *            values
     * 
     * @return Map object created based on information from the file
     * 
     * @throws FileNotFoundException
     *             if the specified file is not found
     */
    public static Map parseFile(String filename, HashMap<Character, Terrain> hashMap)
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
                    // Sudden end of file
                    System.err.format("Invalid line in '%s'\n", filename);
                    scanner.close();
                    return null;
                }
                char c = scanner.next(p).trim().charAt(0);
                if (hashMap.containsKey(c))
                    tiles[i][j] = new Tile(hashMap.get(c));
                else {
                    // Invalid type of Terrain
                    System.err.format("Invalid line in '%s'\n", filename);
                    scanner.close();
                    return null;
                }
            }
        }

        if (scanner.hasNext(p)) {
            System.err.format("Extra information in '%s'\n", filename);
        }
        scanner.close();
        Map map = new Map(tiles, tileHeight, tileWidth);
        return map;
    }
}
