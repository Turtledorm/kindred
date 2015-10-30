package kindred.model;

import java.lang.Math;
import java.util.Random;

// Calculates damage caused by a Unit to another Unit
public class Battle {

    private final Random random;     // Random number generator
    private final float luck = 0.07; // Percentage added or subtracted to damage

    // Initializes random number generator
    public Battle() {
        random = new Random(System.currentTimeMillis());
    }

    /*
     *  Checks if the attacker will hit the defender and, if so, calculates
     *  damage caused to defender, reducing its HP.
     */
    public void execute(Tile attacker, Tile defender) {
        Unit attackUnit = attacker.getUnit();
        Unit defendUnit = defender.getUnit();

        // Calculates agility (with modifiers) and checks if attack will hit
        float attackerAgi = attackUnit.getAgi();
        attackerAgi *= attacker.getTerrain().getAgilityModifier()/100.0;
        float defenderAgi = defendUnit.getAgi();
        defenderAgi *= defender.getTerrain().getAgilityModifier()/100.0;

        if (!checkHit(attackerAgi, defenderAgi))
            return;

        float atk = attackUnit.getAtk();
        float def = defendUnit.getDef();
        def *= defender.getTerrain().getDefenseModifier()/100.0;
        float weaponPower = attackUnit.getWeapon().getPower();

        // Damage formula
        float damage = (0.75*atk + 0.5*weaponPower) - def;
        damage += randomInteger(int(-luck*damage), int(luck*damage));

        defendUnit.loseHp(Math.floor(damage));
    }

    /*
     *  Checks if an attack will hit by comparing the attacker's agility
     *  with the defender's. Returns 'true' if attack will happen or
     *  'false' otherwise.
     */
    private boolean checkHit(float attackerAgi, float defenderAgi) {
        if (attackerAgi >= 2*defenderAgi) return true;

        int hitChance;

        // Probability to hit is calculated with sin/cos functions for
        // nonlinearity.
        if (attackerAgi <= defenderAgi) {
            hitChance = sin((float) attackerAgi/defenderAgi * Math.PI/2);
            hitChance *= 80;
        }
        else {
            hitChance = cos((float) attackerAgi/defenderAgi * Math.PI/2);
            hitChance = (-20*hitChance) + 80;
        }

        return (randomInteger(1, 100) <= hitChance);
    }

    /*
     *  Randomly generates an integer in the interval [min, max].
     *  Supposes that min <= max.
     */
    private int randomInteger(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
