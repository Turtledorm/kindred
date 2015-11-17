package kindred.client.view.cli;

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
    /**
     * Resets the colours.
     */
    RESET("0"),
    /**
     * Black colour. Available to both background and foreground.
     */
    BLACK("0;30"),
    /**
     * Red colour. Available to both background and foreground.
     */
    RED("0;31"),
    /**
     * Green colour. Available to both background and foreground.
     */
    GREEN("0;32"),
    /**
     * Brown colour. Available to both background and foreground.
     */
    BROWN("0;33"),
    /**
     * Blue colour. Available to both background and foreground.
     */
    BLUE("0;34"),
    /**
     * Purple colour. Available to both background and foreground.
     */
    PURPLE("0;35"),
    /**
     * Cyan colour. Available to both background and foreground.
     */
    CYAN("0;36"),
    /**
     * Light grey colour. Available to both background and foreground.
     */
    LIGHT_GREY("0;37"),
    /**
     * Dark grey colour. Available only to foreground.
     */
    DARK_GREY("1;30"),
    /**
     * Light red colour. Available only to foreground.
     */
    LIGHT_RED("1;31"),
    /**
     * Light green colour. Available only to foreground.
     */
    LIGHT_GREEN("1;32"),
    /**
     * Yellow colour. Available only to foreground.
     */
    YELLOW("1;33"),
    /**
     * Light blue colour. Available only to foreground.
     */
    LIGHT_BLUE("1;34"),
    /**
     * Light purple colour. Available only to foreground.
     */
    LIGHT_PURPLE("1;35"),
    /**
     * Light cyan colour. Available only to foreground.
     */
    LIGHT_CYAN("1;36"),
    /**
     * White colour. Available only to foreground.
     */
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
