package kindred.common;

/**
 * Represents a message from a client to the server. Contains a
 * ClientToServerEnum object (representing the type of the message) and a String
 * argument, optionally empty. Can also encode and decode these values to
 * shorten and simplify Client to Server messages.
 * 
 * @author Kindred Team
 */
public class ClientToServerMessage {

    /**
     * The type of the message.
     */
    public final ClientToServerEnum msg;

    /**
     * The argument of the message (optionally empty).
     */
    public final String argument;

    /**
     * Contains the corresponding enum value of each possible ClientToServerEnum
     * type. Stored in a static variable to save time, avoiding calling
     * {@code values()} many times.
     */
    private static final ClientToServerEnum[] values = ClientToServerEnum.values();

    /**
     * Creates a new ClientToServerMessage of type {@code msg} without argument.
     * 
     * @param msg
     *            the type of the message
     */
    public ClientToServerMessage(ClientToServerEnum msg) {
        this.msg = msg;
        this.argument = "";
    }

    /**
     * Creates a new ClientToServerMessage of type {@code msg} with argument
     * {@code argument}.
     * 
     * @param msg
     *            the type of the message
     * @param argument
     *            the argument of the message
     */
    public ClientToServerMessage(ClientToServerEnum msg, String argument) {
        this.msg = msg;
        this.argument = argument;
    }

    /**
     * Encodes the message in a single String containing its enum number and
     * argument, these two separated with the char '|'.
     * 
     * @return an encoded ClientToServerEnum message
     */
    public String toEncodedString() {
        return msg.ordinal() + "|" + argument;
    }

    /**
     * Decodes a String, converting it to its corresponding
     * ClientToServerMessage format.
     * 
     * @param str
     *            argument contained in the message
     * @return a decoded ClientToServerMessage message
     */
    public static ClientToServerMessage fromEncodedString(String str) {
        String[] parts = str.split("\\|", 2);
        ClientToServerEnum msg = values[Integer.parseInt(parts[0])];
        String argument = parts[1];
        return new ClientToServerMessage(msg, argument);
    }

}
