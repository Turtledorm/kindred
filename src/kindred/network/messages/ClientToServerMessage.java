package kindred.network.messages;

public enum ClientToServerMessage {
    /**
     * If it has no argument, asks the Server for the Client's nickname; if it
     * has an argument, tries to define it as the Client's new nickname.
     */
    NICK,

    /**
     * Asks the Server for a list of available maps to play on.
     */
    MAPS,

    /**
     * Asks the Server for a list of available rooms to join. Each room is made
     * of a hosting Client's nickname and the map name they wish to play on.
     */
    ROOMS,

    /**
     * Tries to host a new room to play on the map specified as argument.
     */
    HOST,

    /**
     * Leaves the room that the Client was previously hosting.
     */
    UNHOST,

    /**
     * Tries to join the room whose host's nickname is specified as argument.
     */
    JOIN,

    /**
     * Informs the Server that the Client will close.
     */
    QUIT,

    /**
     * Sends an action related to the game being played. The action is forwarded
     * by the Server to the user's opponent in the game.
     */
    GAME_ACTION;

    // TODO: Check Javadoc below (not sure what values does...)
    /**
     * Contains the corresponding enum value of each possible
     * ClientToServerMessage type.
     */
    private static final ClientToServerMessage[] values = values();

    /**
     * Message argument. If more than one, they have to be separated with the
     * char '|'.
     */
    private String argument;

    /**
     * Initially sets all ClientToServerMessage messages to have no arguments.
     */
    ClientToServerMessage() {
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
     * @return an encoded ClientToServerMessage message
     */
    public String toEncodedString() {
        return ordinal() + "|" + argument;
    }

    /**
     * Decodes a String, converting it to its corresponding
     * ClientToServerMessage format.
     * 
     * @param argument
     *            argument contained in the message
     * @return a decoded ClientToServerMessage message
     */
    public static ClientToServerMessage fromEncodedString(String s) {
        String[] parts = s.split("\\|", 2);
        ClientToServerMessage msg = values[Integer.parseInt(parts[0])];
        msg.setArgument(parts[1]);
        return msg;
    }
}
