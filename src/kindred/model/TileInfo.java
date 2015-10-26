package kindred.model;

public class TileInfo {

    public final String name;
    public final int defenseModifier, agilityModifier;

    public TileInfo(String name, int defenseModifier, int agilityModifier) {
        this.name = name;
        this.defenseModifier = defenseModifier;
        this.agilityModifier = agilityModifier;
    }
}
