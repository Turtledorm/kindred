package kindred.client.model;

import java.io.FileNotFoundException;
import java.util.HashSet;

import kindred.client.parsing.MapFileParser;
import kindred.client.parsing.TerrainFileParser;

/**
 * Represents a game match where two Players battle against each other.
 * 
 * @author Kindred Team
 */
public class Game {

    /**
     * Name of the file containing valid Terrain types.
     */
    private final String terrainFile = "/kindred/common/data/terrain/terrain.txt";

    /**
     * Map played by both Players.
     */
    private Map map;

    /**
     * HashSet for all Units that have moved this turn.
     */
    private HashSet<Unit> unitsThatMoved;

    /**
     * HashSet for all Units that have attacked this turn.
     */
    private HashSet<Unit> unitsThatAttacked;

    /**
     * Controls which Player is playing at the moment.
     */
    private int turn;

    /**
     * Stores the status of the match: {@code true} if finished, {@code false}
     * otherwise.
     */
    private boolean isOver;

    /**
     * Constructs a Game with an empty Map.
     * 
     * @param nameA
     *            playerA's name
     * @param nameB
     *            playerB's name
     * @param mapFile
     *            name of the file containing Map to be played on
     * @param team
     *            user's ID representing which team he will play on
     */
    public Game(String nameA, String nameB, String mapFile, int team) {
        try {
            map = MapFileParser.parseFile(mapFile,
                    TerrainFileParser.parseFile(terrainFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        unitsThatMoved = new HashSet<Unit>();
        unitsThatAttacked = new HashSet<Unit>();

        turn = 1;
        isOver = false;
    }

    /**
     * Moves Unit from the specified team on Tile (xi, yi) to Tile (xf, yf) on
     * this Map during the current player's turn. Supposes that all given
     * coordinates are valid, i.e., not outside borders of this Map.
     * <p>
     * Returns {@code true} if movement was successful or {@code false}
     * otherwise.
     * 
     * @param xi
     *            x coordinate of the Unit to be moved
     * @param yi
     *            y coordinate of the Unit to be moved
     * @param xf
     *            x coordinate of the destination Tile
     * @param yf
     *            y coordinate of the destination Tile
     * @return {@code true}, if movement was successful, or {@code false}
     *         otherwise
     */
    public boolean move(int xi, int yi, int xf, int yf) {
        if (!map.validPosition(xi, yi))
            return false;

        Unit unit = map.getTile(xi, yi).getUnit();

        if (unit == null || unit.getTeam() != turn)
            return false;
        if (!unitsThatMoved.contains(unit) && !unitsThatAttacked.contains(unit)
                && map.move(xi, yi, xf, yf)) {
            unitsThatMoved.add(unit);
            return true;
        }

        return false;
    }

    /**
     * Moves a Unit from Tile (xi, yi) to Tile (xf, yf) without making any
     * assumption, i.e., checking if the Unit exists, its team, etc.
     * 
     * @param xi
     *            x coordinate of the Unit to be moved
     * @param yi
     *            y coordinate of the Unit to be moved
     * @param xf
     *            x coordinate of the destination Tile
     * @param yf
     *            y coordinate of the destination Tile
     */
    public void forceMovement(int xi, int yi, int xf, int yf) {
        map.move(xi, yi, xf, yf);
    }

    /**
     * Makes the Unit on Tile (xi, yi) attack the Unit on Tile (xf, yf), if
     * possible, on the current player's turn. Returns an integer representing
     * damage:
     * <ul>
     * <li>If damage = -1, then the attack wasn't possible to be done.</li>
     * <li>If damage &ge; 0, then it represents the damage received by the
     * defending Unit.</li>
     * </ul>
     * 
     * @param xi
     *            x coordinate of the attacker
     * @param yi
     *            y coordinate of the attacker
     * @param xf
     *            x coordinate of the defender
     * @param yf
     *            y coordinate of the defender
     * @return damage received by the defending unit if the attack succeeded, or
     *         -1 otherwise
     */
    public int attack(int xi, int yi, int xf, int yf) {
        if (!map.validPosition(xi, yi) || !map.validPosition(xf, yf))
            return -1;

        Unit attacker = map.getTile(xi, yi).getUnit();
        Unit defender = map.getTile(xf, yf).getUnit();

        if (attacker == null || attacker.getTeam() != turn || defender == null
                || defender.getTeam() == turn)
            return -1;
        if (!unitsThatAttacked.contains(attacker)) {
            int damage = map.attack(xi, yi, xf, yf);
            if (damage >= 0) {
                unitsThatAttacked.add(attacker);
                map.causeDamage(xf, yf, damage);
                if (Math.min(map.getNumUnitsA(), map.getNumUnitsB()) <= 0)
                    isOver = true;
            }
            return damage;
        }

        return -1;
    }

    /**
     * Directly causes the specified amount of damage to the Unit on Tile (x, y)
     * of the Map. Supposes that a Unit exists on the specified Tile. Returns
     * {@code true} if the Unit is dead, i.e., has no more hit points left, or
     * {@code false} otherwise.
     * 
     * @param x
     *            x coordinate of the Unit that will take damage
     * @param y
     *            y coordinate of the Unit that will take damage
     * @param damage
     *            damage to be caused to the Unit
     * @return {@code true}, if the Unit is dead, or {@code false} otherwise
     */
    public boolean causeDamage(int x, int y, int damage) {
        boolean isDead = map.causeDamage(x, y, damage);
        if (Math.min(map.getNumUnitsA(), map.getNumUnitsB()) <= 0)
            isOver = true;
        return isDead;
    }

    /**
     * Returns the Map being played in the Game.
     * 
     * @return the Map being played in the Game
     */
    public Map getMap() {
        return map;
    }

    /**
     * Makes the current player forfeit the match.
     */
    public void surrender() {
        isOver = true;
    }

    /**
     * Ends the current player's turn, letting the next user take action.
     * 
     */
    public void endTurn() {
        turn ^= 0x03; // 1 <-> 2
        unitsThatMoved.clear();
        unitsThatAttacked.clear();
    }

    /**
     * Returns {@code true} if the Game has ended, i.e., one of the players lost
     * all of their Units, or {@code false} otherwise.
     * 
     * @return {@code true} if the Game ended, or {@code false} otherwise
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Returns the identifier of the current player's turn.
     * 
     * @return identifier of the current player's turn
     */
    public int getTurn() {
        return turn;
    }
}
