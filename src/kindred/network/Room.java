package kindred.network;

/**
 * Holds the nicknames of two players participating in a Game.
 * 
 * @author Kindred Team
 */
public class Room {

    /**
     * Both player's nicknames.
     */
    public String nickA, nickB;

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
     * Checks and returns if a player with the specified nickname is in the
     * room.
     * 
     * @param nick
     *            nickname of the player to be checked
     * @return true, if the nickname exists in the room, or false otherwise
     */
    public boolean hasNick(String nick) {
        return (nick.equals(nickA) || nick.equals(nickB));
    }

    /**
     * Returns the nickname of the specified nick's opponent. If 'nick' isn't in
     * the room, returns null.
     * 
     * @param nick
     *            nickname of the player whose opponent's nick is desired
     * @return nickname of the opponent
     */
    public String getOtherNick(String nick) {
        if (nick.equals(nickA))
            return nickB;
        if (nick.equals(nickB))
            return nickA;
        return null;
    }
}
