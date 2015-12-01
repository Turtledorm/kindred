package kindred.client.network;

/**
 * Represents a game-related message that the Client can send to the Server.
 * Contains a GameActionEnum object (representing the type of the action) and an
 * optional String argument.
 * 
 * 
 * @author Kindred Team
 */
public class GameActionMessage {
    /**
     * The type of the message.
     */
    public final GameActionEnum msg;

    /**
     * The argument of the message (optionally empty).
     */
    public final String argument;

    /**
     * Creates a new GameActionMessage of type {@code msg} without argument.
     * 
     * @param msg
     *            the type of the message
     */
    public GameActionMessage(GameActionEnum msg) {
        this.msg = msg;
        this.argument = "";
    }

    /**
     * Creates a new GameActionMessage of type {@code msg} with argument
     * {@code argument}.
     * 
     * @param msg
     *            the type of the message
     * @param argument
     *            the argument of the message
     */
    public GameActionMessage(GameActionEnum msg, String argument) {
        this.msg = msg;
        this.argument = argument;
    }

    /**
     * Contains the corresponding enum value of each possible GameActionEnum
     * type. Stored in a static variable to save time, avoiding calling
     * {@code values()} many times.
     */
    private static final GameActionEnum[] values = GameActionEnum.values();

    /**
     * Encodes the message in a single String containing its enum number and
     * argument, these two separated with the char '|'.
     * 
     * @return an encoded GameActionEnum message
     */
    public String toEncodedString() {
        return msg.ordinal() + "|" + argument;
    }

    /**
     * Decodes a String, converting it to its corresponding GameActionMessage
     * format.
     * 
     * @param str
     *            argument contained in the message
     * @return a decoded GameActionMessage message
     */
    public static GameActionMessage fromEncodedString(String str) {
        String[] parts = str.split("\\|", 2);
        GameActionEnum msg = values[Integer.parseInt(parts[0])];
        String arg = parts[1];
        return new GameActionMessage(msg, arg);
    }
}
