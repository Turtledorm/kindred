package kindred.network;

public class Room {

    public String nickA;
    public String nickB;

    public Room(String nickA, String nickB) {
        this.nickA = nickA;
        this.nickB = nickB;
    }

    public boolean hasNick(String nick) {
        return (nick.equals(nickA) || nick.equals(nickB));
    }

    public String getOtherNick(String nick) {
        if (nick.equals(nickA))
            return nickB;
        if (nick.equals(nickB))
            return nickA;
        return null;
    }
}
