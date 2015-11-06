package kindred.network;

public class ServerDatabase {

    public static String parse(String message) {
        if (message.startsWith("host ")) {
            String[] split = message.split(" ");
            if (split.length < 2)
                return "Not enough arguments";

        }

        return null;
    }
}
