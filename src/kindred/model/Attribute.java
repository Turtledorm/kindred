package kindred.model;

public class Attribute {
    private int currentHp, hitPoints, attack, defense, agility, movement;

    public int getCurrentHp() {
        return currentHp;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getAgility() {
        return agility;
    }

    public int getMovement() {
        return movement;
    }

    public void loseHp(int damage) {
        this.currentHp -= damage;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    // TODO: Check how to implement this class
}
