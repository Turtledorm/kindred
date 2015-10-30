package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

// Parses a file to get information regarding valid Terrain types
public class TerrainFileParser {

    /*
     *  Parses a given filename containing information about Terrains.
     *  Returns a hashmap containing a char identifier and its
     *  corresponding Terrain.
     */
    public static HashMap<Character, Terrain> parseFile(String filename)
            throws FileNotFoundException {
        File f = new File(filename);
        Scanner scanner = new Scanner(f);
        HashMap<Character, Terrain> hashMap = new HashMap<Character, Terrain>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skips empty lines
            if (line.length() == 0)
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 4) {
                // Line contains more or less information than necessary
                System.err.format("Linha inválida em '%s'\n", filename);
            }
            else {
                char id = tokens[0].charAt(0);
                String name = tokens[1];
                int defenseModifier, agilityModifier;

                try {
                    defenseModifier = Integer.parseInt(tokens[2]);
                    agilityModifier = Integer.parseInt(tokens[3]);
                }
                catch (NumberFormatException e) {
                    // Modifiers aren't integers
                    System.err.format("Linha inválida em '%s'\n", filename);
                    continue;
                }

                Terrain tileInfo = new Terrain(name, defenseModifier,
                        agilityModifier);
                hashMap.put(id, tileInfo);
            }
        }

        scanner.close();
        return hashMap;
    }
}
