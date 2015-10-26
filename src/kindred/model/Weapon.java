package kindred.model;

public class Weapon {

    private final String name;
    private final int power;
    private final int range;

    public Weapon(String name, int power, int range) {
        this.name = name;
        this.power = power;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public int getRange() {
        return range;
    }
}
