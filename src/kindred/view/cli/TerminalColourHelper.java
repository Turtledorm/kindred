package kindred.view.cli;

/**
 * Provides methods to manage colours in a command-line interface.
 * 
 * @author Kindred Team
 * 
 */
public class TerminalColourHelper {

    /**
     * Do not instantiate!
     */
    private TerminalColourHelper() {
    }

    /**
     * Returns an ANSI escape string that changes the colours in a terminal.
     * 
     * @param bg
     * @param fg
     * @return
     */
    public static String getFormattedString(Colour bg, Colour fg) {
        return String.format("\u001B[%s;%sm", fg.getValueAsForeground(),
                bg.getValueAsBackground());
    }

    /**
     * Resets the terminal background and foreground colours to their default
     * values.
     */
    public static void resetColours() {
        System.out.printf("\u001B[%sm", Colour.RESET.getValueAsForeground());
    }

    /**
     * Prints a matrix of atoms. Each atom is printed with its corresponding
     * character using their background and foreground colours. Additionally,
     * column and row headers are printed to show their coordinates to the user.
     * 
     * @param atoms
     *            a matrix of atoms to be printed
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
                System.out.print(getFormattedString(atom.backgroundColour,
                        atom.foregroundColour) + atom.character);
            }
            resetColours();
            System.out.println();
        }
    }
}
