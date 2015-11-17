package kindred.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import kindred.model.TerrainFileParser;

/**
 * Provides a method to parse a file that contains the description of Terrains
 * in a command-line user interface.
 * 
 * @author Kindred Team
 */

public class CLITerrainParser {

    /**
     * Do not instantiate!
     */
    private CLITerrainParser() {
        // Do not instantiate!
    }

    /**
     * Parses a file that describes the display of a Terrain in a terminal
     * interface.
     * <p>
     * The file must have lines containing the name of each Terrain and the name
     * of its background colour separated by whitespaces.
     * <p>
     * The name of the Terrain must be exactly the same as the one in the file
     * parsed by {@link TerrainFileParser}.
     * <p>
     * The name of the colour must be exactly one of the ones defined in the
     * {@link Colour} enum.
     * 
     * @param filename
     *            name of the file informing colours for each Terrain
     * @return HashMap mapping each Terrain name to a colour object representing
     *         its background colour
     * @throws FileNotFoundException
     *             if the specified file is not found
     */
    public static HashMap<String, Colour> parseFile(String filename)
            throws FileNotFoundException {
        File f = new File(CLITerrainParser.class.getResource(filename).getPath());
        Scanner scanner = new Scanner(f);
        HashMap<String, Colour> hashMap = new HashMap<String, Colour>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skips empty lines
            if (line.length() == 0)
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 2) {
                // Line contains more or less information than necessary
                System.err.format("Invalid line in '%s'\n", filename);
            } else {
                Colour colour = null;
                try {
                    colour = Colour.valueOf(tokens[1]);
                } catch (IllegalArgumentException ex) {
                    System.err.format("Color not found in '%s'\n", filename);
                    System.exit(1);
                }
                hashMap.put(tokens[0], colour);
            }
        }

        scanner.close();
        return hashMap;
    }
}
