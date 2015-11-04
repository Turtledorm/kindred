package kindred.model;

import java.util.Random;

// Calculates damage caused by a Unit to another Unit
public class Battle {

    private final Random random; // Random number generator
    private final double luck = 0.07; // Percentage added or subtracted to
                                      // damage

    // Initializes random number generator
    public Battle() {
        random = new Random(System.currentTimeMillis());
    }

    /*
     * Checks if the attacker will hit the defender and, if so, returns damage
     * caused to defender.
     */
    public int execute(Tile attacker, Tile defender) {
        Unit attackUnit = attacker.getUnit();
        Unit defendUnit = defender.getUnit();

        // Calculates agility (with modifiers) and checks if attack will hit
        double attackerAgi = attackUnit.getAgi();
        attackerAgi *= attacker.getTerrain().getAgilityModifier() / 100.0;
        double defenderAgi = defendUnit.getAgi();
        defenderAgi *= defender.getTerrain().getAgilityModifier() / 100.0;

        if (!checkHit(attackerAgi, defenderAgi))
            return 0;

        double atk = attackUnit.getAtk();
        double def = defendUnit.getDef();
        def *= defender.getTerrain().getDefenseModifier() / 100.0;
        double weaponPower = attackUnit.getWeapon().getPower();

        // Damage formula
        double damage = (0.75 * atk + 0.5 * weaponPower) - def;

        if (damage < 1)
            return 1;

        damage += randomInteger((int) Math.ceil(-luck * damage),
                (int) Math.ceil(luck * damage));

        return (int) damage;
    }

    /*
     * Checks if an attack will hit by comparing the attacker's agility with the
     * defender's. Returns 'true' if attack will happen or 'false' otherwise.
     */
    private boolean checkHit(double attackerAgi, double defenderAgi) {
        if (attackerAgi >= 2 * defenderAgi)
            return true;

        double hitChance;

        // Probability to hit is calculated with sin/cos functions for
        // nonlinearity.
        if (attackerAgi <= defenderAgi) {
            hitChance = Math.sin(attackerAgi / defenderAgi * Math.PI / 2);
            hitChance *= 80;
        } else {
            hitChance = Math.cos(attackerAgi / defenderAgi * Math.PI / 2);
            hitChance = (-20 * hitChance) + 80;
        }

        return (randomInteger(1, 100) <= (int) hitChance);
    }

    /*
     * Randomly generates an integer in the interval [min, max]. Supposes that
     * min <= max.
     */
    private int randomInteger(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
