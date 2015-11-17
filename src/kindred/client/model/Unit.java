package kindred.client.model;

/**
 * Defines a Unit that a User can command, as well as its Attributes.
 * 
 * @author Kindred Team
 */
public class Unit {

    /**
     * Name for the Unit's type. There is no distinction in name for Units of
     * the same type.
     */
    private final String name;

    /**
     * Attributes defining Unit's performance.
     */
    private final Attribute attribute;

    /**
     * Identifier number for the Unit's team. Shows which player controls the
     * Unit.
     */
    private final int team;

    /**
     * Constructs a Unit.
     * 
     * @param name
     *            the Unit's name
     * @param attribute
     *            the Unit's attributes
     * @param team
     *            number identifying which Player the Unit belongs to
     */
    public Unit(String name, Attribute attribute, int team) {
        this.name = name;
        this.attribute = attribute;
        this.team = team;
    }

    /**
     * Returns this name given to the Unit's type.
     * 
     * @return this Unit's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this Unit's attribute that represents its total hit points.
     * 
     * @return this Unit's total hit points
     */
    public int getTotalHp() {
        return attribute.getTotalHp();
    }

    /**
     * Returns this Unit's attribute that represents its current hit points.
     * 
     * @return this Unit's current hit points
     */
    public int getCurrentHp() {
        return attribute.getCurrentHp();
    }

    /**
     * Reduces this Unit's current hit points according to the specified amount
     * of damage.
     * 
     * @param damage
     *            value to be subtracted from this Unit's current hit points
     */
    public void loseHp(int damage) {
        attribute.loseHp(damage);
    }

    /**
     * Returns the Unit's attribute that represents its attack power.
     * 
     * @return the Unit's attack
     */
    public int getAtk() {
        return attribute.getAttack();
    }

    /**
     * Returns the Unit's attribute that represents its defense value.
     * 
     * @return the Unit's defense
     */
    public int getDef() {
        return attribute.getDefense();
    }

    /**
     * Returns the Unit's attribute that represents its agility value.
     * 
     * @return the Unit's agility
     */
    public int getAgi() {
        return attribute.getAgility();
    }

    /**
     * Returns the Unit's attribute that represents its movement per turn.
     * 
     * @return the Unit's movement
     */
    public int getMove() {
        return attribute.getMovement();
    }

    /**
     * Returns the Unit's attribute that represents it maximum range of attack.
     * 
     * @return the Unit's range
     */
    public int getRange() {
        return attribute.getRange();
    }

    /**
     * Returns a number that identifies which player the Unit belongs to.
     * 
     * @return the Unit's team identifier
     */
    public int getTeam() {
        return team;
    }
}
