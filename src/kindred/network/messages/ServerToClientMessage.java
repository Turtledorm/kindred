package kindred.network.messages;

public enum ServerToClientMessage {
    // TODO: comment every item below (with javadoc)
    ERR_INVALID_COMMAND_OR_ARGUMENTS,
    ERR_NICKNAME_IS_UNDEFINED,
    ERR_NICKNAME_IS_INVALID,
    ERR_NICKNAME_IS_IN_USE,
    ERR_UNREGISTERED_USER,
    ERR_MAP_NOT_FOUND,
    ERR_ROOM_NOT_FOUND,
    ERR_CANNOT_ENTER_OWN_ROOM,
    ERR_CANNOT_UNHOST_WITHOUT_HOST,
    SUCC_NICKNAME_CHANGED,
    SUCC_HOST,
    SUCC_UNHOST,
    SUCC_JOIN,
    /**
     * Confirmation prefix command to indicate that the Client can safely close.
     */
    SUCC_LEAVE,
    /**
     * Informs the Client's nickname.
     */
    INFO_NICKNAME,
    INFO_AVAILABLE_MAPS,
    INFO_AVAILABLE_ROOMS,
    INFO_LEAVE_HOSTED_ROOM,
    INFO_SOMEONE_ENTERED_ROOM,
    /**
     * Indicates that the Client will be the first player during the game.
     */
    INFO_FIRST_PLAYER,
    /**
     * Indicates that the Client will be the second player during the game.
     */
    INFO_SECOND_PLAYER,
    INFO_MAP, ;

    private final static ServerToClientMessage[] values = values();
    private String argument;

    ServerToClientMessage() {
        setArgument("");
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public String toEncodedString() {
        return ordinal() + "|" + argument;
    }

    public static ServerToClientMessage fromEncodedString(String s) {
        String[] parts = s.split("|");
        ServerToClientMessage msg = values[Integer.parseInt(parts[0])];
        msg.setArgument(parts[1]);
        return msg;
    }
}
