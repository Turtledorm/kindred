package kindred.view.cli;

import java.util.Random;

enum Colour {
    // Reference: http://www.tldp.org/HOWTO/Bash-Prompt-HOWTO/x329.html
    RESET("0"),
    // Colours available to both background and foreground
    BLACK("0;30"), RED("0;31"), GREEN("0;32"), BROWN("0;33"), BLUE("0;34"), PURPLE(
            "0;35"), CYAN("0;36"), LIGHT_GREY("0;37"),
    // Colours available only to foreground
    DARK_GREY("1;30"), LIGHT_RED("1;31"), LIGHT_GREEN("1;32"), YELLOW("1;33"), LIGHT_BLUE(
            "1;34"), LIGHT_PURPLE("1;35"), LIGHT_CYAN("1;36"), WHITE("1;37");

    private final String value;
    private static final Colour[] VALUES = values();

    Colour(String value) {
        this.value = value;
    }

    // does not generate RESET
    public static Colour randomForegroundColour() {
        int index = 1 + new Random().nextInt(VALUES.length - 1);
        return VALUES[index];
    }

    // does not generate RESET
    public static Colour randomBackgroundColour() {
        int index = 1 + new Random().nextInt((VALUES.length - 1) / 2);
        return VALUES[index];
    }

    public String getValueAsForeground() {
        return value;
    }

    public String getValueAsBackground() {
        String[] parts = value.split(";");
        if (parts.length == 1)
            return value;
        if (parts[0].equals("1"))
            throw new IllegalArgumentException(
                    "This colour cannot be used as background.");
        return "" + (10 + Integer.parseInt(parts[1]));
    }
}