package kindred;

import java.io.File;
import java.util.Scanner;

import kindred.model.Game;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nameA, nameB;

        // TODO: Move this input type to CLI?
        System.out.print("Please enter player A's name: ");
        nameA = input.next();

        System.out.print("Please enter player B's name: ");
        nameB = input.next();

        System.out.println("Available maps:");
        File directory = new File(Main.class.getResource("/kindred/data/map/")
                .getPath());
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                System.out.println("- " + name.substring(0, name.length() - 4));
            }
        }

        String map;
        while (true) {
            System.out.print("Please choose an existing map: ");
            map = input.next() + ".txt";
            File file = new File(directory + "/" + map);
            if (file.exists())
                break;
        }

        // TODO: Add path to 'data/' directory?
        // TODO: Give option to select desired map (maybe list them?)
        Game game = new Game(nameA, nameB, "/kindred/data/terrain/terrain.txt",
                "/kindred/data/map/simpleMap.txt");
        game.run();
        input.close();
    }
}
