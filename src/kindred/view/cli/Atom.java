package kindred.view.cli;

/**
 * 
 * Represents an atom of a matrix to be shown in a command-line user interface.
 * <p>
 * Each atom contains a caracter, a background colour and a foreground colour.
 * 
 * @author Kindred Team
 * 
 */
class Atom {

    /**
     * the character of this atom
     */
    public final char character;

    /**
     * the background colour of this atom
     */
    public final Colour backgroundColour;

    /**
     * the foreground colour of this atom
     */
    public final Colour foregroundColour;

    /**
     * Instantiates an atom.
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