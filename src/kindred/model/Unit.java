package kindred.model;

/**
 * Defines a Unit that a User can command, as well as it's UnitClass, Weapon and
 * attributes.
 * 
 * @author Kindred Team
 */
public class Unit {

    /**
     * The Unit's name
     */
    private final String name;

    /**
     * The Unit's attributes
     */
    private final Attribute attribute;

    /**
     * Number representing the unit's team
     */
    private final int team;

    /**
     * Constructs a Unit.
     * 
     * @param name
     *            The Unit's name
     * @param attribute
     *            The Unit's attribute
     */
    public Unit(String name, Attribute attribute, int team) {
        this.name = name;
        this.attribute = attribute;
        this.team = team;
    }

    /**
     * Returns the Unit's name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Unit's team identifier
     * 
     * @return team identifier
     */
    public int getTeam() {
        return team;
    }

    /**
     * Returns the Unit's total HP
     * 
     * @return hitPoints
     */
    public int getTotalHp() {
        return attribute.getHitPoints();
    }

    /**
     * Returns the Unit's current HP
     * 
     * @return currentHP
     */
    public int getCurrentHp() {
        return attribute.getCurrentHp();
    }

    /**
     * Returns the Unit's attack
     * 
     * @return attack
     */
    public double getAtk() {
        return attribute.getAttack();
    }

    /**
     * Returns the Unit's defense
     * 
     * @return defense
     */
    public double getDef() {
        return attribute.getDefense();
    }

    /**
     * Returns the Unit's agility
     * 
     * @return agility
     */
    public double getAgi() {
        return attribute.getAgility();
    }

    /**
     * Returns the Unit's movement
     * 
     * @return movement
     */
    public int getMove() {
        return attribute.getMovement();
    }

    /**
     * Returns the Unit's range
     * 
     * @return range
     */
    public int getRange() {
        return attribute.getRange();
    }

    /**
     * Updates the Unit's currentHp according to a given damage the Unit has
     * taken
     * 
     * @param damage
     */
    public void loseHp(int damage) {
        attribute.loseHp(damage);
    }
}
