package kindred.model;

import java.io.FileNotFoundException;

import kindred.view.AbstractView;

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
     * First and second Players.
     */
    private final String playerA, playerB;

    /**
     * Name of the file containing valid Terrain types.
     */
    private final String terrainFile = "/kindred/data/terrain/terrainColors.txt";

    /**
     * ID for the Player's Units
     */
    private final int team;

    /**
     * Number of Units each Player begins with.
     */
    private final int initUnits = 12;

    /**
     * Number of Units remaining for each Player.
     */
    private int playerAUnits = initUnits, playerBUnits = initUnits;

    /**
     * Creates Units for the Players to use during the game.
     */
    private UnitFactory unitFactory;

    /**
     * Object representing the means of interaction a Player has with the game.
     */
    private AbstractView view;

    /**
     * 
     */
    private boolean readyA = false, readyB = false;

    /**
     * Controls which Player is playing at the moment.
     */
    private int turn;

    /**
     * 
     */
    private boolean isOver;

    /**
     * Constructs a Game with an empty Map.
     * 
     * @param nameA
     *            playerA's name
     * @param nameB
     *            playerB's name
     * @param terrainFile
     *            name of the file containing valid Terrains
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
        playerA = nameA;
        playerB = nameB;

        this.unitFactory = new UnitFactory();
        this.view = view;
        this.team = team;
        turn = 1;
        isOver = false;
    }

    public boolean placeUnit(String unitName, int team, int x, int y) {
        return map.placeUnit(unitFactory.getNewUnit(unitName, team), x, y);
    }

    public boolean move(int xi, int yi, int xf, int yf) {
        return map.move(team, xi, yi, xf, yf);
    }

    public int attack(int xi, int yi, int xf, int yf) {
        return map.attack(team, xi, yi, xf, yf);
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
