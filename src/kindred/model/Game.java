package kindred.model;

import java.io.FileNotFoundException;

import kindred.view.AbstractView;
import kindred.view.cli.CLI;

// Represents a game match where two Players battle against each other
public class Game {

    private Map map;
    private Player playerA, playerB;
    private int playerAUnits, playerBUnits; // Number of units remaining
                                            // for each Player
    private AbstractView view;

    private boolean turn; // Controls which Player is playing at the moment

    /*
     * Initializes a Game. - 'nameA' and 'nameB' are first and second Player's
     * names. - 'terrainFile' contains valid Terrains for the Map. - 'mapFile'
     * contains the Map itself.
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
        playerAUnits = playerBUnits = 0;
        turn = false;

        view = new CLI(map);
    }

    // Main loop. Runs the game until a Player wins.
    public void run() {
        // TODO: Place units on the Map
        while (playerAUnits > 0 && playerBUnits > 0) {
            Player current = (turn ? playerA : playerB);
            view.promptForAction();
            // TODO: Parse action!
            // TODO: Only do below command if Player used an 'end' command
            turn = !turn;
        }
    }
}
