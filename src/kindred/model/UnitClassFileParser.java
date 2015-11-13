package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parse a file and create a UnitClass in the game
 * 
 * @author Kindred team
 */

public class UnitClassFileParser {
    /**
     * Not to be imstanciated, since this class is purely static!
     */
    
    private UnitClassFileParser() {
        //Do not instantiate!
    }

/**
 * Parses a given file containing information about a UnitClass.
 * <p>
 * Creates and returns a Weapon, based on the information contained in the file.
 * 
 * @param filename
 *          Name of the UnitClass file
 * 
 * @retun UnitClass object created based on the information from the file.
 * 
 * @throws FileNotFoundException
 *              If the especified file is not found
 * 
 */
public static UnitClass parseFile(String filename)
        throws FileNotFoundException {
    File f = new File(UnitClass.class.getResource(filename).getPath());
    Scanner scanner = new Scanner(f);
    
    Pattern p = Pattern.compile("\\s*\\w");
    
    String name = scanner.next(p);
    int hp = scanner.nextInt();
    int att = scanner.nextInt();
    int def = scanner.nextInt();
    int agi = scanner.nextInt();
    int mov = scanner.nextInt();
    
    
    if (scanner.hasNext(p)) {
            System.err.format("Extra information in '%s'\n", filename);
        }
    
    Attribute attribute = new Attribute(hp, att, def, agi, mov);
    UnitClass class = new UnitClass(name, attribute);
    
    return class;
        
    }
}