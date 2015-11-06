package kindred.model;

public class Unit {

    private final String name;
    private final UnitClass unitClass;
    private final Attribute attribute;
    private final Weapon weapon;

    public Unit(String name, UnitClass unitClass, Attribute attribute, Weapon weapon) {
        this.name = name;
        this.unitClass = unitClass;
        this.attribute = attribute;
        this.weapon = weapon;
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
    
    public int getRange(){
        return weapon.getRange();
    }
    
    public Weapon getWeapon() {
        return Weapon;
    }

    public void loseHp(double floor) {
        // TODO: implement this
    }
}
