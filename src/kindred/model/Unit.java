package kindred.model;

/**
 * Defines a Unit that a User can command, as well as it's UnitClass,
 * Weapon and attributes.
 * @author Kindred Team
 */
public class Unit {

    /**
     * The Unit's name
     */
    private final String name;
    
    /**
     * The Unit's UnitClass
     */
    private final UnitClass unitClass;
    
    /**
     * The Unit's attributes
     */
    private final Attribute attribute;
    
    /**
     * The Unit's Weapon
     */
    private final Weapon weapon;

    /**
     * Constructs a Unit
     * @param name
     *          The Unit's name
     * @param unitClass
     *          The Unit's class
     * @param attribute
     *          The Unit's attribute
     * @param weapon 
     *          The Unit's weapon
     */
    public Unit(String name, UnitClass unitClass, Attribute attribute, Weapon weapon) {
        this.name = name;
        this.unitClass = unitClass;
        this.attribute = attribute;
        this.weapon = weapon;
    }

    /**
     * Returns the Unit's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Unit's total HP
     * @return hitPoints
     */
    public int getTotalHp() {
        return attribute.getHitPoints();
    }

    /**
     * Returns the Unit's current HP
     * @return currentHP
     */
    public int getCurrentHp() {
        return attribute.getCurrentHp();
    }

    /**
     * Returns the Unit's attack
     * @return attack
     */
    public double getAtk() {
        return attribute.getAttack();
    }

    /**
     * Returns the Unit's defense
     * @return defense
     */
    public double getDef() {
        return attribute.getDefense();
    }

    /**
     * Returns the Unit's agility
     * @return agility
     */
    public double getAgi() {
        return attribute.getAgility();
    }

    /**
     * Returns the Unit's movement
     * @return movement
     */
    public int getMove() {
        return attribute.getMovement();
    }

    /**
     * Returns the Unit's range
     * @return range
     */
    public int getRange() {
        return weapon.getRange();
    }

    /**
     * Returns the Unit's Weapon
     * @return weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Will set the Unit's attributes according to it's UnitClass' 
     * attributes modifiers
     */
    public void set_attributes() {
        attribute.setHitPoints(attribute.getHitPoints()
                + unitClass.getAttribute().getHitPoints());
        attribute.setAttack(attribute.getAttack()
                + unitClass.getAttribute().getAttack());
        attribute.setAttack(attribute.getAttack() + weapon.getPower());
        attribute.setDefense(attribute.getDefense()
                + unitClass.getAttribute().getDefense());
        attribute.setAgility(attribute.getAgility()
                + unitClass.getAttribute().getAgility());
    }

    /**
     * Updates the Unit's currentHp according to a given damage 
     * the Unit has taken
     * @param damage 
     */
    public void loseHp(int damage) {
        attribute.loseHp(damage);
    }

    /**
     * Returns the Unit's UnitClass
     * @return unitClass
     */
    public UnitClass getUnitClass() {
        return unitClass;
    }
}
