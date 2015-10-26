package kindred.model;

public class UnitClass {

    private final String name;

    private final Attribute attribute;

    public UnitClass(String name, Attribute attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}