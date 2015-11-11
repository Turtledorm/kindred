package kindred.model;

/**
 * Gives details about the vegetation of a Tile and how it affects a Unit's
 * attributes.
 * 
 * @author Kindred Team
 */
public class Terrain {

    /**
     * Name given to Terrain.
     */
    public final String name;

    /**
     * Percentage of defense increase/decrease to a Unit standing on a Tile with
     * this Terrain. For example, 30 gives +30% defense to the Unit.
     */
    public final int defenseModifier;

    /**
     * Percentage of agility increase/decrease to a Unit standing on a Tile with
     * this Terrain. For example, -20 gives -20% agility to the Unit.
     */
    public final int agilityModifier;

    /**
     * Movement penalty inflicted on a Unit standing on a Tile with this
     * terrain. For example, -1 means that the Unit will move 1 Tile less.
     */
    public final int movePenalty;

    /**
     * Constructs a Terrain.
     * 
     * @param name
     *            Name of the Terrain
     * @param defenseModifier
     *            Percentage of defense increase/decrease to a Unit standing on
     *            a Tile with this Terrain
     * @param agilityModifier
     *            Percentage of agility increase/decrease to a Unit standing on
     *            a Tile with this Terrain
     * @param movePenalty
     *            Movement penalty inflicted on a Unit standing on a Tile with
     *            this terrain
     */
    public Terrain(String name, int defenseModifier, int agilityModifier,
            int movePenalty) {
        this.name = name;
        this.defenseModifier = defenseModifier;
        this.agilityModifier = agilityModifier;
        this.movePenalty = movePenalty;
    }

    /**
     * Returns the Terrain's name.
     * 
     * @return the Terrain's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Terrain's defense modifier.
     * 
     * @return the Terrain's defense modifier
     */
    public int getDefenseModifier() {
        return defenseModifier;
    }

    /**
     * Returns the Terrain's agility modifier.
     * 
     * @return the Terrain's agility modifier
     */
    public int getAgilityModifier() {
        return agilityModifier;
    }

    /**
     * Returns the Terrain's movement penalty.
     * 
     * @return the Terrain's movement penalty
     */
    public int getMovePenalty() {
        return movePenalty;
    }
}
