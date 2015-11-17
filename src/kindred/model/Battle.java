package kindred.model;

import java.util.Random;

/**
 * Calculates damage caused by a Unit to another Unit.
 * 
 * @author Kindred Team
 */
public class Battle {

    /**
     * Random number generator.
     */
    private final Random random;

    /**
     * Value added or subtracted to damage. Makes battles less predictable.
     */
    private final double luck = 0.07;

    /**
     * Initializes the random number generator.
     */
    public Battle() {
        random = new Random(System.currentTimeMillis());
    }

    /**
     * Checks if the Unit in the attacker Tile will hit the Unit in the defender
     * Tile. Supposes that both given Tiles have a Unit placed on them.
     * <p>
     * Returns an integer representing damage:
     * <ul>
     * <li>If damage = 0, then the attack missed.
     * <li>If damage &gt; 0, then it represents the damage received by the
     * defending Unit.</li>
     * </ul>
     * 
     * @param attacker
     *            Tile containing the attacking Unit
     * @param defender
     *            Tile containing the defending Unit
     * 
     * @return damage received by the defending unit if the attack hit, or 0
     *         otherwise
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

        // Damage formula
        double damage = (0.8 * atk + 0.2 * attackerAgi) - def;

        if (damage < 1)
            return 1;

        damage += randomInteger((int) Math.ceil(-luck * damage),
                (int) Math.ceil(luck * damage));

        return (int) damage;
    }

    /**
     * Checks if an attack will hit by comparing the attacker's agility with the
     * defender's. Returns {@code true} if attack will happen or {@code false}
     * otherwise.
     * 
     * @param attackerAgi
     *            agility of the attacking Unit, already considering the Terrain
     *            modifier
     * @param defenderAgi
     *            agility of the defending Unit, already considering the Terrain
     *            modifier
     * 
     * @return{@code true} if the attack succeeded, or{@code false} otherwise
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

        return randomInteger(1, 100) <= (int) hitChance;
    }

    /**
     * Randomly generates and returns an integer in the interval [min, max].
     * Supposes that min &le; max.
     * 
     * @param min
     *            minimum possible integer that can be generated
     * @param max
     *            maximum possible integer that can be generated
     * 
     * @return an integer in range [min, max]
     */
    private int randomInteger(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
