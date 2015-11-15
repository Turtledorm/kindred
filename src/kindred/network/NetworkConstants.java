package kindred.network;

/**
 * Contains constant values shared by the network related classes.
 * 
 * @author Kindred Team
 */
public class NetworkConstants {

    /**
     * Default port used on the Kindred server.
     */
    public static final int KINDRED_PORT = 8000;

    /**
     * Character that separates a special message prefix from the normal
     * client/server message.
     */
    public static final String PREFIX_CHAR = "%";

    /**
     * Size of each prefix identifier.
     */
    public static final int PREFIX_SIZE = 3;

    /**
     * Indicates that the prefix does nothing.
     */
    public static final String PREF_NULL = "NUL";

    /**
     * Indicates that after this prefix command is the Client's nickname.
     */
    public static final String PREF_NICK = "NCK";

    /**
     * Indicates that after this prefix command is the nickname of the opponent
     * with whom the Client with play against.
     */
    public static final String PREF_OPPONENT = "OPP";

    /**
     * Indicates that after this prefix command is the name of the map to be
     * played on.
     */
    public static final String PREF_MAP = "MAP";

    /**
     * Indicates that the Client will be the first player during the game.
     */
    public static final String PREF_P1 = "PL1";

    /**
     * Indicates that the Client will be the second player during the game.
     */
    public static final String PREF_P2 = "PL2";

    /**
     * Indicates that the game the Client is playing on has ended.
     */
    public static final String PREF_GAME_END = "GED";

    /**
     * Confirmation prefix command to indicate that the Client can safely close.
     */
    public static final String PREF_BYE = "BYE";
}
