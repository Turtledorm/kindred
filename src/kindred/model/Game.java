package kindred.model;

import java.io.FileNotFoundException;

import kindred.view.AbstractView;
import kindred.view.cli.CLI;

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
    private Player playerA, playerB;

    /**
     * Number of Units remaining for each Player.
     */
    private int playerAUnits, playerBUnits;

    /**
     * Object representing the means of interaction a Player has with the game.
     */
    private AbstractView view;

    /**
     * Controls which Player is playing at the moment.
     */
    private boolean turn;

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
     */
    public Game(String nameA, String nameB, String terrainFile, String mapFile) {
        try {
            map = MapFileParser.parseFile(mapFile,
                    TerrainFileParser.parseFile(terrainFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        playerA = new Player(nameA);
        playerB = new Player(nameB);
        playerAUnits = playerBUnits = 3;
        turn = false;

        view = new CLI(map);
    }

    /**
     * Main loop. Runs the Game until a Player wins, i.e., destroys all of the
     * other Player's Units.
     */
    public void run() {
        // TODO: Place units on the Map
        while (playerAUnits > 0 && playerBUnits > 0) {
            Player current = (turn ? playerA : playerB);
            view.displayMap();
            while (!view.promptForAction())
                view.displayMap();
            turn = !turn;
        }
    }
}
