package kindred.client.view.cli;

/**
 * Provides methods to manage colours in a command-line interface.
 * 
 * @author Kindred Team
 * 
 */
public final class TerminalColourHelper {

    /**
     * Do not instantiate.
     */
    private TerminalColourHelper() {
    }

    /**
     * Returns a formatted ANSI escape sequence to change background and
     * foreground colours. If a colour is {@code null}, it is ignored. If both
     * colours are {@code null}, returns an empty string.
     * 
     * @param bg
     *            the background colour
     * @param fg
     *            the foreground colour
     * @return an ANSI escape string that changes the colours in a terminal
     */
    public static String getEscapeSequence(Colour bg, Colour fg) {
        if (bg == null && fg == null)
            return "";

        String str = "\u001B[";
        if (fg != null)
            str += fg.getValueAsForeground();
        if (bg != null)
            str += ";" + bg.getValueAsBackground();
        return str + "m";
    }

    /**
     * Resets the terminal background and foreground colours to their default
     * values.
     */
    public static void resetColours() {
        System.out.printf("\u001B[%sm", Colour.RESET.getValueAsForeground());
    }

    /**
     * Prints a matrix of Atoms. Each Atom is printed with its corresponding
     * character using their background and foreground colours. Additionally,
     * column and row headers are printed to show their coordinates to the user.
     * 
     * @param atoms
     *            a matrix of Atoms to be printed
     */
    public static void drawMatrix(Atom[][] atoms) {
        Integer h = atoms.length;
        Integer w = atoms[0].length;
        int hLength = h.toString().length();
        int wLength = w.toString().length();
        String rowLabelFormat = " %" + hLength + "d ";
        String emptyCornerFormat = " %" + hLength + "s ";
        int power = (int) Math.pow(10, wLength - 1);

        for (int d = 0; d < wLength; d++) {
            boolean onlyZero = true;
            System.out.printf(emptyCornerFormat, "");
            for (int j = 0; j < w; j++) {
                int digit = ((j + 1) / power) % 10;
                if (digit != 0)
                    onlyZero = false;
                if (!onlyZero)
                    System.out.print(digit);
                else
                    System.out.print(' ');
            }
            System.out.println();
            power /= 10;
        }

        for (int i = 0; i < h; i++) {
            System.out.printf(String.format(rowLabelFormat, 1 + i));
            for (int j = 0; j < w; j++) {
                Atom atom = atoms[i][j];
                System.out.print(getEscapeSequence(atom.backgroundColour,
                        atom.foregroundColour) + atom.character);
            }
            resetColours();
            System.out.println();
        }
    }
}
