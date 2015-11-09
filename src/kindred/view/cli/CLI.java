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
        // TODO: check how to return the action. Create a class "Acttion"?
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // TODO: Define syntax and parse the input.
        }
        scanner.close();
    }
}
