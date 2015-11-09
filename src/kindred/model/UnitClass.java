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

    public void set_mods() {
        if (name.equals("Lancer")) {
            attribute.setHitPoints(3);
            attribute.setAttack(4);
            attribute.setDefense(1);
            attribute.setAgility(-5);
        } else if (name.equals("Archer")) {
            attribute.setHitPoints(-8);
            attribute.setAttack(5);
            attribute.setDefense(-7);
            attribute.setAgility(10);
        } else if (name.equals("Knight")) {
            attribute.setHitPoints(7);
            attribute.setAttack(2);
            attribute.setDefense(7);
            attribute.setAgility(-8);
        } else if (name.equals("Swordsman")) {
            attribute.setHitPoints(-2);
            attribute.setAttack(6);
            attribute.setDefense(-4);
            attribute.setAgility(7);
        }
    }
}
