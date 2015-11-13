package kindred.model;

/**
 * Defines the UnitClass a Unit can belong to, as well as it's 
 * attribute modifiers
 * @author Kindred Team
 */
public class UnitClass {

    /**
     * The UnitClass' name
     */
    private final String name;
    
    /**
     * The UnitClass' attribute modifiers
     */
    private final Attribute attribute;

    /**
     * Construct a UnitClass
     * @param name
     *          UnitClass' name
     * @param attribute 
     *          UnitClass' attribute modifiers
     */
    public UnitClass(String name, Attribute attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    /**
     * Returns the UnitClass' name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the UnitClass' attribute modifiers
     * @return attribute
     */
    public Attribute getAttribute() {
        return attribute;
    }
/*  Modifiers to use in the txt files
    public void set_mods() {
        if (name.equals("Lancer")) {
            attribute.setHitPoints(3);
            attribute.setAttack(4);
            attribute.setDefense(1);
            attribute.setAgility(-5);
        } else if (name.equals("Archer")) {
            attribute.setHitPoints(-8);
            attribute.setAttack(5);
            attribute.setDefense(-7);
            attribute.setAgility(10);
        } else if (name.equals("Knight")) {
            attribute.setHitPoints(7);
            attribute.setAttack(2);
            attribute.setDefense(7);
            attribute.setAgility(-8);
        } else if (name.equals("Swordsman")) {
            attribute.setHitPoints(-2);
            attribute.setAttack(6);
            attribute.setDefense(-4);
            attribute.setAgility(7);
        }
    } */
}
