package kindred.view.cli;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

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
     * The number of rows of the map.
     */
    private int height;

    /**
     * The number of columns of the map.
     */
    private int width;

    /**
     * Name of the file containing the background colours of the terrains.
     */
    private final String colourFile = "/kindred/data/terrain/terrainColors.txt";

    /**
     * Maps names of terrains to their corresponding background colours.
     */
    private HashMap<String, Colour> colourTypes;

    /**
     * Creates a command-line user interface for the game.
     * 
     * @param map
     *            the map of the current game
     */
    public CLI(Map map) {
        super(map);
        height = map.getHeight();
        width = map.getWidth();
        atomMap = new Atom[height][width];
        try {
            colourTypes = CLITerrainParser.parseFile(colourFile);
        } catch (FileNotFoundException e) {
            System.err.format("File '%s' not found!\n", colourFile);
            System.exit(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!colourTypes.containsKey(map.getTile(i, j).getTerrain()
                        .getName())) {
                    System.err.println("Terrain not found in CLI colour file '"
                            + map.getTile(i, j).getTerrain().getName() + "'");
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
    @Override
    public boolean promptForAction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type in your command.");
        printPrompt();

        for (; scanner.hasNextLine(); printPrompt()) {
            int[] positions;

            String line = scanner.nextLine().trim();
            String[] separate = line.split("\\s+");
            switch (separate[0]) {
            case "move":
            case "mv":
                if (separate.length != 5) {
                    System.out
                            .println("Invalid argument for the specified command!");
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    map.move(positions[0], positions[1], positions[2], positions[3]);
                break;
            case "attack":
            case "atk":
                if (separate.length != 5) {
                    System.out
                            .println("Invalid argument for the specified command!");
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    map.attack(positions[0], positions[1], positions[2],
                            positions[3]);
                break;
            case "info":
                if (separate.length != 3) {
                    System.out
                            .println("Invalid argument for the specified command!");
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null) {
                    String message = map.getTileInfo(positions[0], positions[1]);
                    System.out.println(message);
                }
                break;
            case "end":
                if (separate.length != 1) {
                    System.out
                            .println("Invalid argument for the specified command!");
                    continue;
                }
                return true;
            default:
                System.out.println("Command not recognized!");
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
        // Convert positions to ints
        int[] positions = new int[separate.length - 1];
        try {
            for (int i = 1; i < separate.length; i++) {
                positions[i - 1] = Integer.parseInt(separate[i]) - 1;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Position specified is not a number!");
            return null;
        }

        for (int i = 1; i < separate.length; i += 2) {
            if (!map.validPosition(positions[i - 1], positions[i])) {
                System.out.println("Specified position is out of bounds!");
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
}
