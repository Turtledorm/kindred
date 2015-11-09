package kindred;

import java.util.Scanner;

import kindred.model.Game;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nameA, nameB;

        System.out.print("Please enter player A's name: ");
        nameA = input.next();

        System.out.print("Please enter player B's name: ");
        nameB = input.next();

        // TODO: Add path to 'data/' directory?
        // TODO: Give option to select desired map (maybe list them?)
        Game game = new Game(nameA, nameB, "src/kindred/data/terrain.txt",
                "src/kindred/data/simpleMap.txt");
        game.run();
        input.close();
    }
}
