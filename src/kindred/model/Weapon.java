package kindred.model;

/**
 * Class that defines the Weapon a Unit is wielding, 
 * affecting it's attack power and range accordingly
 * @author Kindred Team
 */
public class Weapon {

    /**
     * The Weapon's name
     */
    private final String name;
    
    /**
     * The Weapon's power
     */
    private final int power;
   
    /**
     * The Weapon's range
     */
    private final int range;

    /**
     * Constructs a Weapon
     * @param name
     *          The Weapon's name
     * @param power
     *          The Weapon's power
     * @param range 
     *          The Weapon's range
     */
    public Weapon(String name, int power, int range) {
        this.name = name;
        this.power = power;
        this.range = range;
    }

    /**
     * Returns the Weapon's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Weapon's power
     * @return power
     */
    public int getPower() {
        return power;
    }

    /**
     * Returns the Weapon's range
     * @return range
     */
    public int getRange() {
        return range;
    }
}
