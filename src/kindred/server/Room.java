package kindred.server;

/**
 * Holds the nicknames of two players participating in a game.
 * 
 * @author Kindred Team
 */
public class Room {

    /**
     * Player A's nickname.
     */
    public String nickA;

    /**
     * Player B's nickname.
     */
    public String nickB;

    /**
     * Constructs a Room.
     * 
     * @param nickA
     *            nickname of the first player
     * @param nickB
     *            nickname of the second player
     */
    public Room(String nickA, String nickB) {
        this.nickA = nickA;
        this.nickB = nickB;
    }

    /**
     * Returns {@code true} if a player with the specified nickname is in the
     * room, or {@code false} otherwise.
     * 
     * @param nick
     *            nickname of the player to be checked
     * @return {@code true}, if the nick exists in the room, or {@code false}
     *         otherwise
     */
    public boolean hasNick(String nick) {
        return nick.equals(nickA) || nick.equals(nickB);
    }

    /**
     * Returns the nickname of the specified nick's opponent. If {@code nick}
     * isn't in the room, returns {@code null}.
     * 
     * @param nick
     *            nickname of the player whose opponent's nick is desired
     * @return nickname of the opponent, if {@code nick} exists in the room, or
     *         {@code null} otherwise
     */
    public String getOtherNick(String nick) {
        if (nick.equals(nickA))
            return nickB;
        if (nick.equals(nickB))
            return nickA;
        return null;
    }
}
