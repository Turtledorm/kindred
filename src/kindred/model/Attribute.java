package kindred.model;
/**
 * Class that defines a Unit's overall attributes and a UnitClass' 
 * attribute modifiers
 * @author Kindred Team
 */

public class Attribute {
    /**
     * All the attributes that a Unit needs, including currentHP, wich will
     * keep tabs on a Unit's Hit Points during a game session.
     */
    private int currentHp, hitPoints, attack, defense, agility, movement;

    /**
     * Constructs an Attribute
     * @param hp
     *          The Unit's hitPoints
     * @param att
     *          The Unit's attack
     * @param def
     *          The Unit's defense
     * @param agi
     *          The Unit's agility
     * @param mov 
     *          The Unit's movement, in other words, 
     * the maximum ammount of tiles it can move past during a turn
     */
    public Attribute(int hp, int att, int def, int agi, int mov) {
        this.hitPoints = hp;
        this.attack = att;
        this.defense = def;
        this.agility = agi;
        this.movement = mov;
    }

    /**
     * Returns the current HP a Unit has left
     * @return currentHP
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Returns the Unit's total hitPoints
     * @return hitPoints
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Returns the Unit's attack
     * @return attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Returns the Unit's defense
     * @return defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the Unit's agility
     * @return agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Returns the Unit's movement
     * @return movement
     */
    public int getMovement() {
        return movement;
    }

    /**
     * Updates currentHP according to a certain damage the Unit took
     * @param damage 
     */
    public void loseHp(int damage) {
        this.currentHp -= damage;
    }

    // TODO: Check how to implement this class
}
