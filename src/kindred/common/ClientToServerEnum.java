package kindred.common;

/**
 * Contains an enum number for every type of menu-related message the Client can
 * send to the Server. Can also encode and decode these values to shorten and
 * simplify Client to Server messages.
 * 
 * @author Kindred Team
 */
public enum ClientToServerEnum {
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
     * Sends an action-related to the game being played. The action is forwarded
     * by the Server to the user's opponent in the game.
     */
    GAME_ACTION,

    /**
     * Dummy message used mainly for pinging the server.
     */
    EMPTY;

}
