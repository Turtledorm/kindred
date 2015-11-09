package kindred.view.cli;

import java.util.Scanner;

import kindred.model.Map;
import kindred.view.AbstractView;

public class CLI extends AbstractView {

    private Atom[][] atomMap;
    private int height, width;

    public CLI(Map map) {
        super(map);
        height = map.getHeight();
        width = map.getWidth();
        atomMap = new Atom[height][width];
    }

    @Override
    public void displayMap() {
        // TODO: find correspondence between map and atomMap, i.e., fill autoMap
        // with bg and fg colours and characters
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // atomMap[i][j].backgroundColour = ?;
                // atomMap[i][j].foregroundColour = ?;
                // atomMap[i][j].character = ?;
            }
        }
        TerminalColourHelper.drawMatrix(atomMap);

    }

    @Override
    public void promptForAction() {
        // TODO: check how to return the action. Create a class "Action"?
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
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
            }
            // TODO: Define syntax and parse the input.
        }
        scanner.close();
    }

    private int[] parsePosition(String[] separate) {
        // Convert positions to ints
        int[] positions = new int[separate.length - 1];
        try {
            for (int i = 1; i <= 4; i++) {
                positions[i] = Integer.parseInt(separate[i]) - 1;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Position specified is not a number!");
            return null;
        }

        for (int i = 1; i < separate.length; i += 2) {
            if (!map.validPosition(positions[i], positions[i + 1])) {
                System.out.println("Specified position is out of bounds!");
                return null;
            }
        }

        return positions;
    }
}
