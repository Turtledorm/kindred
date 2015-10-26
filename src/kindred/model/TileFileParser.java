package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class TileFileParser {

    private TileFileParser() {
        // Can't be instantiated
    }

    public static HashMap<Character, TileInfo> parseFile(String filename)
            throws FileNotFoundException {
        File f = new File(filename);
        Scanner scanner = new Scanner(f);
        HashMap<Character, TileInfo> hashMap = new HashMap<Character, TileInfo>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.length() == 0)
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 4) {
                System.err.format("Linha inválida em '%s'\n", filename);
            } else {
                char id = tokens[0].charAt(0);
                String name = tokens[1];
                int defenseModifier, agilityModifier;
                try {
                    defenseModifier = Integer.parseInt(tokens[2]);
                    agilityModifier = Integer.parseInt(tokens[3]);
                } catch (NumberFormatException e) {
                    System.err.format("Linha inválida em '%s'\n", filename);
                    continue;
                }

                TileInfo tileInfo = new TileInfo(name, defenseModifier,
                        agilityModifier);
                hashMap.put(id, tileInfo);
            }

        }

        scanner.close();
        return hashMap;
    }
}
