package kindred.view.cli;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import kindred.model.Game;
import kindred.model.Map;
import kindred.view.AbstractView;

/**
 * 
 * Provides a command-line user interface to interact with the user through a
 * terminal.
 * 
 * @author Kindred Team
 * 
 */
public class CLI extends AbstractView {

    /**
     * A map of atoms representing the tiles of the map.
     */
    private Atom[][] atomMap;

    /**
     * Name of the file containing the background colours of the terrains.
     */
    private final String colourFile = "/kindred/data/terrain/terrainColors.txt";

    /**
     * Maps names of terrains to their corresponding background colours.
     */
    private HashMap<String, Colour> colourTypes;

    /**
     * The scanner object to get user's input from the command-line interface.
     */
    private final Scanner scanner;

    /**
     * Creates a command-line user interface for the game.
     */
    public CLI() {
        super();
        scanner = new Scanner(System.in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String askForString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * {@inheritDoc}
     */
    public void setGame(Game game) {
        super.setGame(game);
        atomMap = new Atom[height][width];
        try {
            colourTypes = CLITerrainParser.parseFile(colourFile);
        } catch (FileNotFoundException e) {
            System.err.format("File '%s' not found!\n", colourFile);
            System.exit(1);
        }
        msgBundle = ResourceBundle.getBundle("kindred.lang.CLI-MessageBundle",
                locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayMap() {
        Map map = game.getMap();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!colourTypes.containsKey(map.getTile(i, j).getTerrain()
                        .getName())) {
                    System.err.println("Terrain not found in CLI colour file '"
                            + map.getTile(i, j).getTerrain().getName() + "'");
                    // new MessageFormat(
                    // msgBundle.getString("terrain_colour_not_found"), locale);
                    System.exit(1);
                }
                Colour foregroundColour = Colour.LIGHT_RED;
                Colour backgroundColour = colourTypes.get(map.getTile(i, j)
                        .getTerrain().getName());
                char character = ' ';
                atomMap[i][j] = new Atom(character, backgroundColour,
                        foregroundColour);
            }
        }

        TerminalColourHelper.drawMatrix(atomMap);
    }

    /**
     * {@inheritDoc}
     */
    public void promptForMenuAction() {
        // TODO: parse the commands and their arguments, then build a
        // ClientToServerMessage to send
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean promptForAction() {

        System.out.println(msgBundle.getString("type_in_your_command"));
        printPrompt();

        for (; scanner.hasNextLine(); printPrompt()) {
            int[] positions;

            String line = scanner.nextLine().trim().toLowerCase();
            String[] separate = line.split("\\s+");
            switch (separate[0]) {
            case "move":
            case "mv":
                if (separate.length != 5) {
                    System.out.println(msgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    game.move(positions[0], positions[1], positions[2], positions[3]);
                break;
            case "attack":
            case "atk":
                if (separate.length != 5) {
                    System.out.println(msgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    game.attack(positions[0], positions[1], positions[2],
                            positions[3]);
                break;
            case "info":
                if (separate.length != 3) {
                    System.out.println(msgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null) {
                    String message = game.getMap().getTileInfo(positions[0],
                            positions[1]);
                    System.out.println(message);
                }
                break;
            case "end":
                if (separate.length != 1) {
                    System.out.println(msgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                return true;
            default:
                System.out.println(msgBundle.getString("unrecognised_command"));
            }
        }
        scanner.close();
        return false;
    }

    /**
     * Parses an array of strings containing numbers, except for the first
     * element, which is supposedly a command (ignored by this method). As the
     * numbers represent positions (row, column), there must be an even quantity
     * of numbers. This method also checks if the position is valid (i.e., is
     * inside the bounds of the map)
     * 
     * @param separate
     *            an array of strings containing anything in the first position
     *            and an even quantity of numbers in the rest of it
     * @return an array of {@code int}s representing the positions given in the
     *         strings of {@code separate}, or {@code null} in case of errors
     */
    private int[] parsePosition(String[] separate) {
        Map map = game.getMap();
        int[] positions = new int[separate.length - 1];
        try {
            for (int i = 1; i < separate.length; i++) {
                positions[i - 1] = Integer.parseInt(separate[i]) - 1;
            }
        } catch (NumberFormatException ex) {
            System.out.println(msgBundle.getString("position_not_a_number"));
            return null;
        }

        for (int i = 1; i < separate.length; i += 2) {
            if (!map.validPosition(positions[i - 1], positions[i])) {
                System.out.println(msgBundle.getString("position_out_of_bounds"));
                return null;
            }
        }

        return positions;
    }

    /**
     * Displays a prompt message to the user.
     */
    private static void printPrompt() {
        System.out.print(">> ");
    }

    @Override
    public void close() {
        scanner.close();
    }
}
