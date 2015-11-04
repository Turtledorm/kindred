package kindred.model;

import java.util.Random;

public class ColourTest {

    /*
     * Black 0;30 Dark Gray 1;30 Blue 0;34 Light Blue 1;34 Green 0;32 Light
     * Green 1;32 Cyan 0;36 Light Cyan 1;36 Red 0;31 Light Red 1;31 Purple 0;35
     * Light Purple 1;35 Brown 0;33 Yellow 1;33 Light Gray 0;37 White 1;37
     * 
     * Source: http://www.tldp.org/HOWTO/Bash-Prompt-HOWTO/x329.html
     */
    private enum Colour {
        RESET(0), BLACK(30), BLUE(34), GREEN(32), CYAN(36), RED(31), PURPLE(35), LIGHT_GREY(
                37), BROWN(33);

        private final int value;
        private static final Colour[] VALUES = values();

        Colour(int value) {
            this.value = value;
        }

        // does not generate RESET
        public static Colour randomColour() {
            int index = 1 + new Random().nextInt(VALUES.length - 1);
            return VALUES[index];
        }

        public int getValueAsForeground() {
            return value;
        }

        public int getValueAsBackground() {
            return value + 10;
        }
    }

    private static void changeForegroundColour(Colour colour) {
        System.out.printf("\u001B[%dm", colour.getValueAsForeground());
    }

    private static void changeBackgroundColour(Colour colour) {
        System.out.printf("\u001B[%dm", colour.getValueAsBackground());
    }

    private static void resetForegroundColour() {
        changeForegroundColour(Colour.RESET);
    }

    private static void resetBackgroundColour() {
        changeBackgroundColour(Colour.RESET);
    }

    private static class Atom {
        public final char character;
        public final Colour backgroundColour, foregroundColour;

        public Atom(char character, Colour backgroundColour, Colour foregroundColour) {
            this.character = character;
            this.backgroundColour = backgroundColour;
            this.foregroundColour = foregroundColour;
        }
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
                changeBackgroundColour(atom.backgroundColour);
                changeForegroundColour(atom.foregroundColour);
                System.out.print(atom.character);
            }
            resetBackgroundColour();
            resetForegroundColour();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int width = Integer.parseInt(args[1]), height = Integer.parseInt(args[0]);
        Atom[][] matrix = new Atom[height][width];
        Random r = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-+-*/.,";
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Colour fg = Colour.randomColour();
                Colour bg = Colour.randomColour();
                char c = alphabet.charAt(r.nextInt(alphabet.length()));
                matrix[i][j] = new Atom(c, bg, fg);
            }
        drawMatrix(matrix);
        resetBackgroundColour();
        resetForegroundColour();
    }

}
