package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parse a file and create a UnitClass in the game
 * 
 * @author Kindred team
 */

public class UnitClassFileParser {
    /**
     * Not to be instantiated, since this class is purely static!
     */

    private UnitClassFileParser() {
        // Do not instantiate!
    }

    /**
     * Parses a given file containing information about a UnitClass.
     * <p>
     * Creates and returns a Weapon, based on the information contained in the
     * file.
     * 
     * @param filename
     *            Name of the UnitClass file
     * 
     * @return UnitClass object created based on the information from the file
     * 
     * @throws FileNotFoundException
     *             If the specified file is not found
     * 
     */
    public static UnitClass parseFile(String filename) throws FileNotFoundException {
        File f = new File(UnitClassFileParser.class.getResource(filename).getPath());
        Scanner scanner = new Scanner(f);

        Pattern p = Pattern.compile("\\s*\\w");
        String name;
        int hp, att, def, agi, mov;
        try {
            name = scanner.next(p);
            hp = scanner.nextInt();
            att = scanner.nextInt();
            def = scanner.nextInt();
            agi = scanner.nextInt();
            mov = scanner.nextInt();
        } catch (NoSuchElementException e) {
            System.err.format("Invalid file format in '%s'\n", filename);
            return null;
        } finally {
            scanner.close();
        }
        if (scanner.hasNext(p)) {
            System.err.format("Warning: Extra information in '%s'\n", filename);
        }
        Attribute attribute = new Attribute(hp, att, def, agi, mov);
        UnitClass unitClass = new UnitClass(name, attribute);
        scanner.close();
        return unitClass;
    }
}