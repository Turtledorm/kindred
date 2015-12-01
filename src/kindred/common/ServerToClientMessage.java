package kindred.common;

public class ServerToClientMessage {

    public final ServerToClientEnum msg;

    public final String argument;

    public ServerToClientMessage(ServerToClientEnum msg) {
        this.msg = msg;
        this.argument = "";
    }

    public ServerToClientMessage(ServerToClientEnum msg, String argument) {
        this.msg = msg;
        this.argument = argument;
    }

    /**
     * Contains the corresponding enum value of each possible ClientToServerEnum
     * type. Stored in a static variable to save time from calling
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
