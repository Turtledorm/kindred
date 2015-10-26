package kindred.model;

public class Unit {

    private final String name;
    private final UnitClass unitClass;
    private final Attribute attribute;

    public Unit(String name, UnitClass unitClass, Attribute attribute) {
        this.name = name;
        this.unitClass = unitClass;
        this.attribute = attribute;
    }
}
