package kindred.model;


/**
 * Handles events related to user info and control over the Game.
 * 
 * @author Kindred Team
 */
public class Player {

    /**
     * Player's name.
     */
    private String name;

    /**
     * Constructs a Player.
     * 
     * @param name
     *            Player's name.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Returns the Player's name.
     * 
     * @return the Player's name.
     */
    public String getName() {
        return name;
    }
}
