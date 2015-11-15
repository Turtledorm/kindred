package kindred.model;

/**
 * Class that defines a Unit's overall attributes and a UnitClass' attribute
 * modifiers
 * 
 * @author Kindred Team
 */

public class Attribute {
    /**
     * All the attributes that a Unit needs, including currentHP, which will
     * keep tabs on a Unit's Hit Points during a game session.
     */
    private int currentHp, hitPoints;
    private int attack, defense, agility;
    private int movement, range;

    /**
     * Constructs an Attribute
     * 
     * @param hp
     *            The Unit's hitPoints
     * @param atk
     *            The Unit's attack
     * @param def
     *            The Unit's defense
     * @param agi
     *            The Unit's agility
     * @param mov
     *            The Unit's movement, in other words, the maximum amount of
     *            tiles it can move past during a turn
     */
    public Attribute(int hp, int atk, int def, int agi, int mov, int rng) {
        this.hitPoints = hp;
        this.attack = atk;
        this.defense = def;
        this.agility = agi;
        this.movement = mov;
        this.range = rng;
    }

    /**
     * Returns the current HP a Unit has left
     * 
     * @return currentHP
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Returns the Unit's total hitPoints
     * 
     * @return hitPoints
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Returns the Unit's attack
     * 
     * @return attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Returns the Unit's defense
     * 
     * @return defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the Unit's agility
     * 
     * @return agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Returns the Unit's movement
     * 
     * @return movement
     */
    public int getMovement() {
        return movement;
    }

    /**
     * Returns the Unit's range of attack
     * 
     * @return range
     */
    public int getRange() {
        return range;
    }

    /**
     * Updates currentHP according to a certain damage the Unit took
     * 
     * @param damage
     */
    public void loseHp(int damage) {
        this.currentHp -= damage;
    }
}
