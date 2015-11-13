package kindred.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parse a file and create a Weapon in the game
 * 
 * @author Kindred team
 */

public class WeaponFileParser {
    /**
     * Not to be imstanciated, since this class is purely static!
     */
    
    private WeaponFileParser() {
        //Do not instantiate!
    }

/**
 * Parses a given file containing information about a Weapon.
 * <p>
 * Creates and returns a Weapon, based on the information contained in the file.
 * 
 * @param filename
 *          Name of the Weapon file
 * 
 * @retun Weapon object created based on the information from the file.
 * 
 * @throws FileNotFoundException
 *              If the especified file is not found
 * 
 */
public static Weapon parseFile(String filename)
        throws FileNotFoundException {
    File f = new File(Weapon.class.getResource(filename).getPath());
    Scanner scanner = new Scanner(f);
    Pattern p = Pattern.compile("\\s*\\w");
    
    String name = scanner.next(p);
    int power = scanner.nextInt();
    int range = scanner.nextInt();
    
    if (scanner.hasNext(p)) {
            System.err.format("Extra information in '%s'\n", filename);
        }
    
    Weapon weapon = new Weapon(name, power, range);
    
    return weapon;
        
    }
}