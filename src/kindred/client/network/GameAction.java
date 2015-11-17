package kindred.client.network;

/**
 * Contains an enum number for every type of game-related message the Client can
 * send to the Server. Can also encode and decode these values to shorten and
 * simplify Client to Server messages.
 * 
 * @author Kindred Team
 */
public enum GameAction {
    /**
     * Moves a Unit the player controls to a different Tile, if possible.
     */
    MOVE,

    /**
     * Makes a Unit controlled by the player attack an opponent's Unit, if
     * possible.
     */
    ATTACK,

    /**
     * Ends the player's turn, passing the game control to the user's opponent.
     */
    END_TURN,

    /**
     * Informs that the user wishes to give up, resulting in their opponent
     * winning the game.
     */
    SURRENDER;

    /**
     * Contains the corresponding enum value of each possible GameAction type.
     * Stored in a static variable to save time from calling {@code values()}
     * many times.
     */
    private static final GameAction[] values = values();

    /**
     * Message argument. If more than one, they have to be separated with the
     * char '|'.
     */
    private String argument;

    /**
     * Initially sets all GameAction messages to have no arguments.
     */
    GameAction() {
        setArgument("");
    }

    /**
     * Sets the message argument as the specified value.
     * 
     * @param argument
     *            argument to be given to the message
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * Returns the argument contained in the message.
     * 
     * @return argument contained in the message
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Encodes the message in a single String containing its enum number and
     * argument, these two separated with the char '|'.
     * 
     * @return an encoded GameAction message
     */
    public String toEncodedString() {
        return ordinal() + "|" + argument;
    }

    /**
     * Decodes a String, converting it to its corresponding GameAction format.
     * 
     * @param str
     *            argument contained in the message
     * @return a decoded GameAction message
     */
    public static GameAction fromEncodedString(String str) {
        String[] parts = str.split("\\|", 2);
        GameAction msg = values[Integer.parseInt(parts[0])];
        msg.setArgument(parts[1]);
        return msg;
    }
}
