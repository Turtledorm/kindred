package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

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

        File f = new File(UnitFileParser.class.getResource(filename).getPath());
        Scanner scanner = new Scanner(f);
        Pattern p = Pattern.compile("\\s*\\w");

        while (scanner.hasNextLine()) {
            String name;
            int hp, atk, def, agi, mov, rng;

            try {
                name = scanner.next(p);
                hp = scanner.nextInt();
                atk = scanner.nextInt();
                def = scanner.nextInt();
                agi = scanner.nextInt();
                mov = scanner.nextInt();
                rng = scanner.nextInt();
            } catch (NoSuchElementException e) {
                System.err.format("Invalid file format in '%s'\n", filename);
                return null;
            } finally {
                scanner.close();
            }

            if (scanner.hasNext(p))
                System.err.format("Warning: Extra information in '%s'\n", filename);

            // Adds the new Unit type to the HashMap
            Attribute attribute = new Attribute(hp, atk, def, agi, mov, rng);
            unitTypes.put(name, attribute);
        }

        scanner.close();
        return unitTypes;
    }
}
