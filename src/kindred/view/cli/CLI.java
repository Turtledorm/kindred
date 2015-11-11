package kindred.view.cli;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import kindred.model.Map;
import kindred.view.AbstractView;

public class CLI extends AbstractView {

    private Atom[][] atomMap;
    private int height, width;

    private final String colourFile = "./kindred/data/terrain/terrainColors.txt";
    private HashMap<String, Colour> colourTypes;

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

    @Override
    public void displayMap() {
        // TODO: find correspondence between map and atomMap, i.e., fill autoMap
        // with bg and fg colours and characters
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

    private static void printPrompt() {
        System.out.print(">> ");
    }
}
