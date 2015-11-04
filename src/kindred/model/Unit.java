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

    public double getAtk() {
        return attribute.getAttack();
    }

    public int getMove() {
        return attribute.getMovement();
    }

    public double getAgi() {
        return attribute.getAgility();
    }

    public int getHp() {
        return attribute.getHitPoints();
    }

    public double getDef() {
        return attribute.getDefense();
    }

    public Weapon getWeapon() {
        return null;
        // TODO: implement this
    }

    public void loseHp(double floor) {
        // TODO: implement this
    }
}
