package kindred.common;

public class ClientToServerMessage {

    public final ClientToServerEnum msg;

    public final String argument;

    /**
     * Contains the corresponding enum value of each possible ClientToServerEnum
     * type. Stored in a static variable to save time from calling
     * {@code values()} many times.
     */
    private static final ClientToServerEnum[] values = ClientToServerEnum.values();

    public ClientToServerMessage(ClientToServerEnum msg) {
        this.msg = msg;
        this.argument = "";
    }

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
