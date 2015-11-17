package kindred.view.cli;

/**
 * 
 * Represents an Atom of a matrix to be shown in a command-line user interface.
 * <p>
 * Each Atom contains a Character, a background colour and a foreground colour.
 * 
 * @author Kindred Team
 * 
 */
class Atom {

    /**
     * Character of this Atom.
     */
    public final char character;

    /**
     * Background colour of this Atom.
     */
    public final Colour backgroundColour;

    /**
     * Foreground colour of this Atom.
     */
    public final Colour foregroundColour;

    /**
     * Instantiates an Atom.
     * 
     * @param character
     *            the character of the atom
     * @param backgroundColour
     *            the background colour of the atom
     * @param foregroundColour
     *            the foreground colour of the atom
     */
    public Atom(char character, Colour backgroundColour, Colour foregroundColour) {
        this.character = character;
        this.backgroundColour = backgroundColour;
        this.foregroundColour = foregroundColour;
    }
}
