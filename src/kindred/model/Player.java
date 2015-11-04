package kindred.model;

import java.util.Scanner;

// Handles events related to user info and control over the game
public class Player {

    private String name;
    private Scanner input; // Reads data from stdin

    // Creates a Player with the given name
    public Player(String name) {
        this.name = name;
        input = new Scanner(System.in);
    }

    // Returns the Player's name
    public String getName() {
    	return name;
    }

    // Reads a command from the Player, returning it afterwards
    public String readCommand() {
        System.out.print("Digite a ação que gostaria de fazer: ");
        return input.next();
    }
}
