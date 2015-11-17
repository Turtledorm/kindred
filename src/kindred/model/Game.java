package kindred.model;

import java.io.FileNotFoundException;
import java.util.HashSet;

import kindred.view.AbstractView;

//TODO: Check Javadoc for this class
/**
 * Represents a game match where two Players battle against each other.
 * 
 * @author Kindred Team
 */
public class Game {

    /**
     * Map played by both Players.
     */
    private Map map;

    /**
     * Name of the file containing valid Terrain types.
     */
    private final String terrainFile = "/kindred/data/terrain/terrain.txt";

    /**
     * ID for the Player's Units.
     */
    private final int team;

    /**
     * Number of Units remaining for player A.
     */
    private int playerAUnits; // TODO: Read this number

    /**
     * Number of Units remaining for Player B.
     */
    private int playerBUnits;

    /**
     * 
     */
    private HashSet<Unit> unitsThatMoved;

    /**
     * 
     */
    private HashSet<Unit> unitsThatAttacked;

    private boolean readyA = false;

    private boolean readyB = false;

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
     * @param view
     *            the user interface
     */
    public Game(String nameA, String nameB, String mapFile, int team,
            AbstractView view) {
        try {
            map = MapFileParser.parseFile(mapFile,
                    TerrainFileParser.parseFile(terrainFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        unitsThatMoved = new HashSet<Unit>();
        unitsThatAttacked = new HashSet<Unit>();

        this.team = team;
        turn = 1;
        isOver = false;
    }

    public boolean move(int xi, int yi, int xf, int yf) {
        if (turn == team) {
            Unit unit = map.getTile(xi, yi).getUnit();
            if (unit == null)
                return false;
            if (!unitsThatMoved.contains(unit) && !unitsThatAttacked.contains(unit)
                    && map.move(team, xi, yi, xf, yf)) {
                unitsThatMoved.add(unit);
                return true;
            }
        }
        return false;
    }

    public int attack(int xi, int yi, int xf, int yf) {
        if (turn == team) {
            Unit unit = map.getTile(xi, yi).getUnit();
            if (unit == null)
                return -1;
            if (!unitsThatAttacked.contains(unit)) {
                int damage = map.attack(team, xi, yi, xf, yf);
                if (damage >= 0)
                    unitsThatAttacked.add(unit);
                return map.attack(team, xi, yi, xf, yf);
            }
        }

        return -1;
    }

    public boolean causeDamage(int x, int y, int damage) {
        Unit unit = map.getTile(x, y).getUnit();
        if (unit == null)
            return false;
        unit.loseHp(damage);
        boolean isDead = unit.getCurrentHp() <= 0;
        if (isDead) {
            if (unit.getTeam() == 1)
                playerAUnits--;
            else
                playerBUnits--;
            if (Math.min(playerAUnits, playerBUnits) <= 0)
                isOver = true;
        }
        return isDead;
    }

    public Map getMap() {
        return map;
    }

    public void surrender() {
        isOver = true;
    }

    public void endTurn() {
        turn ^= 0x03; // 1 <-> 2
    }

    public boolean isOver() {
        return isOver;
    }
}
