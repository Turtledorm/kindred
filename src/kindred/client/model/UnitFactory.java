package kindred.client.model;

import java.io.FileNotFoundException;
import java.util.HashMap;

import kindred.client.parsing.UnitFileParser;

/**
 * Follows the Factory pattern to produce Units according to specifications.
 * 
 * @author Kindred Team
 */
public class UnitFactory {

    /**
     * Name of the file containing valid Unit types.
     */
    private final String filename = "/kindred/data/unit/unit.txt";

    /**
     * HashMap containing Unit names as keys and their attributes as values.
     */
    private HashMap<String, Attribute> unitTypes;

    /**
     * Initializes the factory by reading valid Unit types from a file.
     */
    public UnitFactory() {
        try {
            this.unitTypes = UnitFileParser.parseFile(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Unit file not found!");
            System.exit(1);
        }
    }

    /**
     * Creates and returns a Unit according to the name given. Also sets the
     * Unit's team with an identifier of the player the Unit will belong to.
     * 
     * @param name
     *            name of the type of Unit to be created
     * @param team
     *            value that identifies which player the created Unit belongs to
     * @return a new Unit, complete with name, attributes and team
     */
    public Unit getNewUnit(String name, int team) {
        Unit unit = new Unit(name, unitTypes.get(name), team);
        return unit;
    }
}
