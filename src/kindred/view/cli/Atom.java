package kindred.view.cli;

class Atom {
    public final char character;
    public final Colour backgroundColour, foregroundColour;

    public Atom(char character, Colour backgroundColour, Colour foregroundColour) {
        this.character = character;
        this.backgroundColour = backgroundColour;
        this.foregroundColour = foregroundColour;
    }
}