package kindred.model;

public class Tile {

    private final String name;
    private final char id;
    private final float defenseModifier;
    private final float agilityModifier;

    public Tile(String name, char id, float defenseModifier, float agilityModifier) {
        this.name = name;
        this.id = id;
        this.defenseModifier = defenseModifier;
        this.agilityModifier = agilityModifier;
    }
}
