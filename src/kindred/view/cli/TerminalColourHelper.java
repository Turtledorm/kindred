package kindred.view.cli;

import java.util.Random;

public class TerminalColourHelper {

    private TerminalColourHelper() {
        // Do not instantiate!
    }

    private static void changeForegroundColour(Colour colour) {
        System.out.printf("\u001B[%sm", colour.getValueAsForeground());
    }

    private static void changeBackgroundColour(Colour colour) {
        System.out.printf("\u001B[%sm", colour.getValueAsBackground());
    }

    private static String getFormattedString(String text, Colour bg, Colour fg) {
        return String.format("\u001B[%s;%sm%s", fg.getValueAsForeground(),
                bg.getValueAsBackground(), text);
    }

    private static void resetForegroundColour() {
        changeForegroundColour(Colour.RESET);
    }

    private static void resetBackgroundColour() {
        changeBackgroundColour(Colour.RESET);
    }

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
                System.out.print(getFormattedString("" + atom.character,
                        atom.backgroundColour, atom.foregroundColour));
            }
            resetBackgroundColour();
            resetForegroundColour();
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Only for testing
        int width = Integer.parseInt(args[1]), height = Integer.parseInt(args[0]);
        Atom[][] matrix = new Atom[height][width];
        Random r = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-+-*/.,";
        Colour lastBG = null;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Colour fg = Colour.randomForegroundColour();
                Colour bg;
                do {
                    bg = Colour.randomBackgroundColour();
                } while (bg == lastBG);
                lastBG = bg;
                char c = alphabet.charAt(r.nextInt(alphabet.length()));
                matrix[i][j] = new Atom(c, bg, fg);
            }
        drawMatrix(matrix);
        resetBackgroundColour();
        resetForegroundColour();
    }
}
