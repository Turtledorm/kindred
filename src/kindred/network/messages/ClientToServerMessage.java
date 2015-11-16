package kindred.network.messages;

public enum ClientToServerMessage {
    // TODO: comment every item below (with javadoc)
    NICK,
    MAPS,
    ROOMS,
    HOST,
    UNHOST,
    JOIN,
    QUIT;

    private final static ClientToServerMessage[] values = values();
    private String argument;

    ClientToServerMessage() {
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

    public static ClientToServerMessage fromEncodedString(String s) {
        String[] parts = s.split("\\|", 2);
        ClientToServerMessage msg = values[Integer.parseInt(parts[0])];
        msg.setArgument(parts[1]);
        return msg;
    }
}
