package kindred;

import java.io.File;
import java.util.Scanner;

import kindred.model.Game;
import kindred.view.AbstractView;
import kindred.view.cli.CLI;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nameA, nameB;
        AbstractView view = new CLI();
        nameA = view.askForString("Player A's name");
        nameB = view.askForString("Player B's name");

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
        Game game = new Game(nameA, nameB, "/kindred/data/terrain/terrain.txt",
                "/kindred/data/map/" + map, view);
        game.run();
        input.close();
    }
}
