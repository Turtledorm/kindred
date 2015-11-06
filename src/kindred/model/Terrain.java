package kindred.model;

/*
 *  Gives details about the vegetation of a Tile and how
 *  it affects a Unit's attributes.
 */
public class Terrain {

    public final String name; // Name given to Terrain

    // Terrain's increase/decrease to a Unit's defense, in percentage
    public final int defenseModifier;

    // Terrain's increase/decrease to a Unit's agility, in percentage
    public final int agilityModifier;

    /*
     *  Example for modifier usage:
     *  defenseModifier = 30 means that a unit will gain +30% defense
     */

    // Creates a Terrain with the given parameters
    public Terrain(String name, int defenseModifier, int agilityModifier) {
        this.name = name;
        this.defenseModifier = defenseModifier;
        this.agilityModifier = agilityModifier;
    }

    // Returns the Terrain's name
    public String getName() {
        return name;
    }

    // Returns the Terrain's defense modifier
    public int getDefenseModifier() {
        return defenseModifier;
    }

    // Returns the Terrain's agility modifier
    public int getAgilityModifier() {
        return agilityModifier;
    }
}