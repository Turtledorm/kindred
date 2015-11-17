package kindred.model;

/**
 * Class that defines a Unit's overall attributes that influence their
 * performance during the game.
 * 
 * @author Kindred Team
 */

public class Attribute {
    /**
     * Indicates a Unit's total hit points.
     */
    private int totalHp;

    /**
     * Indicates a Unit's current hit points. When it reaches zero, it is
     * considered dead.
     */

    private int currentHp;

    /**
     * Raises damage caused by a Unit.
     */
    private int attack;

    /**
     * Reduces damage received by a Unit.
     */
    private int defense;

    /**
     * Raises chance of evading an enemy Unit's attack.
     */
    private int agility;

    /**
     * Indicates the maximum number of Tiles a Unit can move during each turn.
     */
    private int movement;

    /**
     * Indicates the maximum distance, in Tiles, that the Unit has to attack the
     * opponent's Units.
     */
    private int range;

    /**
     * Constructs an Attribute.
     * 
     * @param hp
     *            the Unit's total hit points
     * @param atk
     *            the Unit's attack points
     * @param def
     *            the Unit's defense points
     * @param agi
     *            the Unit's agility points
     * @param mov
     *            the Unit's maximum movement per turn
     * @param rng
     *            the Unit's maximum range
     */
    public Attribute(int hp, int atk, int def, int agi, int mov, int rng) {
        this.totalHp = this.currentHp = hp;
        this.attack = atk;
        this.defense = def;
        this.agility = agi;
        this.movement = mov;
        this.range = rng;
    }

    /**
     * Returns the Unit's maximum hit points.
     * 
     * @return the Unit's total hit points
     */
    public int getTotalHp() {
        return totalHp;
    }

    /**
     * Returns the number of hit points remaining for the Unit.
     * 
     * @return the Unit's current hit points
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Reduces the Unit's current hit points according to the specified amount
     * of damage.
     * 
     * @param damage
     *            value to be subtracted from the Unit's current hit points
     */
    public void loseHp(int damage) {
        this.currentHp -= damage;
    }

    /**
     * Returns the Unit's attack value.
     * 
     * @return the Unit's attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Returns the Unit's defense value.
     * 
     * @return the Unit's defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the Unit's agility value.
     * 
     * @return the Unit's agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Returns the Unit's maximum movement per turn.
     * 
     * @return the Unit's movement
     */
    public int getMovement() {
        return movement;
    }

    /**
     * Returns the Unit's maximum range of attack.
     * 
     * @return the Unit's range
     */
    public int getRange() {
        return range;
    }
}
