package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Parses a file to get information regarding valid Terrain types.
 * 
 * @author Kindred Team
 */
public class TerrainFileParser {

    /**
     * Not to be instantiated, since this class is purely static!
     */
    private TerrainFileParser() {
        // Do not instantiate!
    }

    /**
     * Parses a file containing information about Terrains. Returns a HashMap
     * containing a char identifier and its corresponding Terrain.
     * 
     * @param filename
     *            name of the Terrain file
     * 
     * @return HashMap containing chars identifying the Terrain as keys and
     *         Terrain objects themselves as values
     * 
     * @throws FileNotFoundException
     *             if the specified file is not found
     */
    public static HashMap<Character, Terrain> parseFile(String filename)
            throws FileNotFoundException {
        HashMap<Character, Terrain> hashMap = new HashMap<Character, Terrain>();

        File f = new File(TerrainFileParser.class.getResource(filename).getPath());
        Scanner scanner = new Scanner(f);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skips empty lines
            if (line.length() == 0)
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 5) {
                // Line contains more or less information than necessary
                System.err.format("Invalid line in '%s'\n", filename);
            } else {
                char id = tokens[0].charAt(0);
                String name = tokens[1];
                int defenseModifier, agilityModifier, movePenalty;
                try {
                    defenseModifier = Integer.parseInt(tokens[2]);
                    agilityModifier = Integer.parseInt(tokens[3]);
                    movePenalty = Integer.parseInt(tokens[4]);
                } catch (NumberFormatException e) {
                    // Modifiers aren't integers
                    System.err.format("Invalid line in '%s'\n", filename);
                    continue;
                }

                Terrain tileInfo = new Terrain(name, defenseModifier,
                        agilityModifier, movePenalty);
                hashMap.put(id, tileInfo);
            }
        }

        scanner.close();
        return hashMap;
    }
}
