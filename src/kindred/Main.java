package kindred;

import java.util.Scanner;
import kindred.model.Game;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String nameA, nameB;

        System.out.print("Digite o nome do jogador A: ");
        nameA = input.next();

        System.out.print("Digite o nome do jogador B: ");
        nameB = input.next();

        // TODO: Add path to 'data/' directory?
        // TODO: Give option to select desired map (maybe list them?)
        Game game = new Game(nameA, nameB, "kindred/data/terrain.txt",
                    "kindred/data/simpleMap.txt");
        game.run();
    }
}
