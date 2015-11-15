package kindred.model;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class UnitFactory {

    private final String filename = "/kindred/unit/units.txt";
    private HashMap<String, Attribute> unitTypes;

    public UnitFactory() {
        try {
            this.unitTypes = UnitFileParser.parseFile(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Unit file not found!");
            System.exit(1);
        }
    }

    public Unit getNewUnit(String name) {
        Unit unit = new Unit(name, unitTypes.get(name));
        return unit;
    }
}
