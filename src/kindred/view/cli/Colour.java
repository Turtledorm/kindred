package kindred.view.cli;

import java.util.Random;

/**
 * Represents one of the possible colours in the terminal using ANSI escape
 * sequences.
 * 
 * @author Kindred Team
 * @see <a href="http://www.tldp.org/HOWTO/Bash-Prompt-HOWTO/x329.html">ANSI
 *      Escape Sequences: Colours and Cursor Movement</a>
 */

enum Colour {
    RESET("0"),
    // Colours available to both background and foreground
    BLACK("0;30"),
    RED("0;31"),
    GREEN("0;32"),
    BROWN("0;33"),
    BLUE("0;34"),
    PURPLE("0;35"),
    CYAN("0;36"),
    LIGHT_GREY("0;37"),
    // Colours available only to foreground
    DARK_GREY("1;30"),
    LIGHT_RED("1;31"),
    LIGHT_GREEN("1;32"),
    YELLOW("1;33"),
    LIGHT_BLUE("1;34"),
    LIGHT_PURPLE("1;35"),
    LIGHT_CYAN("1;36"),
    WHITE("1;37");

    /**
     * The inner part of the ANSI escape sequence of this colour.
     */
    private final String value;

    /**
     * Contains the corresponding enum value of each possible
     * ClientToServerMessage type. Stored in a static variable to save time from
     * calling {@code values()} many times.
     */
    private static final Colour[] VALUES = values();

    /**
     * Initialises a new colour from its value.
     * 
     * @param value
     *            the inner part of the ANSI escape sequence of this colour
     */
    Colour(String value) {
        this.value = value;
    }

    /**
     * Creates a random colour that can serve as the foreground of an
     * {@link Atom}. Does not generate {@link Colour#RESET}.
     * 
     * @return a random colour that can be used as foreground
     */
    public static Colour randomForegroundColour() {
        int index = 1 + new Random().nextInt(VALUES.length - 1);
        return VALUES[index];
    }

    /**
     * Creates a random colour that can serve as the background of an
     * {@link Atom}. Does not generate {@link Colour#RESET}.
     * 
     * @return a random colour that can be used as background
     */
    public static Colour randomBackgroundColour() {
        int index = 1 + new Random().nextInt((VALUES.length - 1) / 2);
        return VALUES[index];
    }

    /**
     * Gives the number that can be used in an ANSI escape sequence to use the
     * colour as foreground.
     * 
     * @return the inner part of the ANSI escape sequence of this colour to be
     *         used as foreground
     */
    public String getValueAsForeground() {
        return value;
    }

    /**
     * Gives the number that can be used in an ANSI escape sequence to use the
     * colour as background.
     * 
     * @return the inner part of the ANSI escape sequence of this colour to be
     *         used as background
     */
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
