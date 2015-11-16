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
    /**
     * Confirmation that the player has successfully joined the room. The
     * argument is a pipe-separated string containing:
     * <ul>
     * <li>opponent's name</li>
     * <li>order of turns (1 if the player starts, or 2 otherwise)</li>
     * <li>map name</li>
     * </ul>
     */
    SUCC_JOIN,
    /**
     * Indicates that the Client can safely close.
     */
    SUCC_LEAVE,
    /**
     * Informs the Client's nickname.
     */
    INFO_NICKNAME,
    INFO_AVAILABLE_MAPS,
    INFO_AVAILABLE_ROOMS,
    /**
     * Tells the user that they are no longer hosting a room after joining
     * another user's room.
     */
    INFO_LEAVE_HOSTED_ROOM,
    /**
     * Confirmation that someone has successfully entered the room that the user
     * hosts. The argument is a pipe-separated string containing:
     * <ul>
     * <li>opponent's name</li>
     * <li>order of turns (1 if the player starts, or 2 otherwise)</li>
     * <li>map name</li>
     * </ul>
     */
    INFO_SOMEONE_ENTERED_ROOM, ;

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
        String[] parts = s.split("\\|", 2);
        ServerToClientMessage msg = values[Integer.parseInt(parts[0])];
        msg.setArgument(parts[1]);
        return msg;
    }
}
