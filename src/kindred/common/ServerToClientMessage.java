package kindred.common;

/**
 * Represents a message from the server to a client. Contains a
 * ClientToServerEnum object (representing the type of the message) and a String
 * argument, optionally empty. Can also encode and decode these values to
 * shorten and simplify Server to Client messages.
 * 
 * @author Kindred Team
 */
public class ServerToClientMessage {

    /**
     * The type of the message.
     */
    public final ServerToClientEnum msg;

    /**
     * The argument of the message (optionally empty).
     */
    public final String argument;

    /**
     * Creates a new ServerToClientMessage of type {@code msg} without argument.
     * 
     * @param msg
     *            the type of the message
     */
    public ServerToClientMessage(ServerToClientEnum msg) {
        this.msg = msg;
        this.argument = "";
    }

    /**
     * Creates a new ServerToClientMessage of type {@code msg} with argument
     * {@code argument}.
     * 
     * @param msg
     *            the type of the message
     * @param argument
     *            the argument of the message
     */
    public ServerToClientMessage(ServerToClientEnum msg, String argument) {
        this.msg = msg;
        this.argument = argument;
    }

    /**
     * Contains the corresponding enum value of each possible ServerToClientEnum
     * type. Stored in a static variable to save time, avoiding calling
     * {@code values()} many times.
     */
    private static final ServerToClientEnum[] values = ServerToClientEnum.values();

    /**
     * Encodes the message in a single String containing its enum number and
     * argument, these two separated with the char '|'.
     * 
     * @return an encoded ServerToClientEnum message
     */
    public String toEncodedString() {
        return msg.ordinal() + "|" + argument;
    }

    /**
     * Decodes a String, converting it to its corresponding
     * ServerToClientMessage format.
     * 
     * @param str
     *            argument contained in the message
     * @return a decoded ServerToClientMessage message
     */
    public static ServerToClientMessage fromEncodedString(String str) {
        String[] parts = str.split("\\|", 2);
        ServerToClientEnum msg = values[Integer.parseInt(parts[0])];
        String arg = parts[1];
        return new ServerToClientMessage(msg, arg);
    }
}
