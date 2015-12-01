package kindred.common;

/**
 * Contains an enum number for every type of message the Server can send to the
 * Client. Can also encode and decode these values to shorten and simplify
 * Server to Client messages.
 * 
 * @author Kindred Team
 */
public enum ServerToClientEnum {
    /**
     * Error informing that the user's given command is not supported. Also
     * occurs when an existing command is sent with the wrong number or
     * arguments.
     */
    ERR_INVALID_COMMAND_OR_ARGUMENTS,

    /**
     * Error informing that the user currently has no nickname defined.
     */
    ERR_NICKNAME_IS_UNDEFINED,

    /**
     * Error that occurs when the user tries to define a nickname with invalid
     * chars or a number of chars outside the specified.
     */
    ERR_NICKNAME_IS_INVALID,

    /**
     * Error that occurs when the user tries to define a nickname that is
     * already being used by another Client.
     */
    ERR_NICKNAME_IS_IN_USE,

    /**
     * Error informing that the user hasn't registered their nickname yet and
     * therefore cannot use a certain Server command.
     */
    ERR_UNREGISTERED_USER,

    /**
     * Error informing that the user's desired map to host a room on was not
     * found.
     */
    ERR_MAP_NOT_FOUND,

    /**
     * Error informing that the user tried to connect to a nickname that isn't
     * hosting a room.
     */
    ERR_ROOM_NOT_FOUND,

    /**
     * Error informing that the user cannot join their own room.
     */
    ERR_CANNOT_ENTER_OWN_ROOM,

    /**
     * Error informing that the user cannot leave a room if they weren't even
     * hosting one.
     */
    ERR_CANNOT_UNHOST_WITHOUT_HOST,

    /**
     * Confirmation that the user has successfully changed their nickname.
     */
    SUCC_NICKNAME_CHANGED,

    /**
     * Confirmation that the user is now hosting a room on their chosen map.
     */
    SUCC_HOST,

    /**
     * Confirmation that the user has successfully left the room they were
     * hosting.
     */
    SUCC_UNHOST,

    /**
     * Confirmation that the user has successfully joined the room. The argument
     * is a pipe-separated string containing:
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

    /**
     * Informs available maps to play on.
     */
    INFO_AVAILABLE_MAPS,

    /**
     * Informs all available rooms, i.e., the nicknames of each user hosting a
     * room and their chosen map to play on.
     */
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
     * <li>Opponent's name.</li>
     * <li>Order of turns (1 if the player starts, or 2 otherwise).</li>
     * <li>Map name.</li>
     * </ul>
     */
    INFO_SOMEONE_ENTERED_ROOM,

    /**
     * Game-related command that the Server only passes forward to the Client.
     * See {@link kindred.client.network.GameAction} class for more info.
     */
    GAME_ACTION;

}
