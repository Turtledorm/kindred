package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Parses a file and gets information regarding valid Unit types.
 * 
 * @author Kindred team
 */

public class UnitFileParser {

    /**
     * Not to be instantiated, since this class is purely static!
     */
    private UnitFileParser() {
        // Do not instantiate!
    }

    /**
     * Parses a given file containing information about a Unit. Returns a
     * HashMap containing the name for each valid Unit and their attributes.
     * 
     * @param filename
     *            name of the Unit file
     * 
     * @return HashMap containing Strings identifying the Units as keys and
     *         their attributes as values
     * 
     * @throws FileNotFoundException
     *             if the specified file is not found
     */
    public static HashMap<String, Attribute> parseFile(String filename)
            throws FileNotFoundException {
        HashMap<String, Attribute> unitTypes = new HashMap<String, Attribute>();

        File f = null;
        try {
            f = new File(UnitFileParser.class.getResource(filename).getPath());
        } catch (NullPointerException e) {
            System.err.println("File '" + filename + "' not found!");
            System.exit(1);
        }

        Scanner scanner = new Scanner(f);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skips empty lines
            if (line.isEmpty())
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 7) {
                // Line contains more or less information than necessary
                System.err.format("Invalid line size in '%s'\n", filename);
            } else {
                String name = null;
                int hp, atk, def, agi, mov, rng;
                hp = atk = def = agi = mov = rng = 0;

                try {
                    name = tokens[0];
                    hp = Integer.parseInt(tokens[1]);
                    atk = Integer.parseInt(tokens[2]);
                    def = Integer.parseInt(tokens[3]);
                    agi = Integer.parseInt(tokens[4]);
                    mov = Integer.parseInt(tokens[5]);
                    rng = Integer.parseInt(tokens[6]);
                } catch (NumberFormatException e) {
                    System.err.format("Invalid file format in '%s'\n", filename);
                    System.exit(1);
                }

                // Adds the new Unit type to the HashMap
                Attribute attribute = new Attribute(hp, atk, def, agi, mov, rng);
                unitTypes.put(name, attribute);
            }
        }

        scanner.close();
        return unitTypes;
    }
}
