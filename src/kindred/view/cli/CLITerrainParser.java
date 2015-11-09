package kindred.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class CLITerrainParser {

    private CLITerrainParser() {
        // Do not instantiate!
    }

    public static HashMap<String, Colour> parseFile(String filename)
            throws FileNotFoundException {
        File f = new File(filename);
        Scanner scanner = new Scanner(f);
        HashMap<String, Colour> hashMap = new HashMap<String, Colour>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Skips empty lines
            if (line.length() == 0)
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length != 2) {
                // Line contains more or less information than necessary
                System.err.format("Invalid line in '%s'\n", filename);
            } else {
                Colour colour = null;
                try {
                    colour = Colour.valueOf(tokens[1]);
                } catch (IllegalArgumentException ex) {
                    System.err.format("Color not found in '%s'\n", filename);
                    System.exit(1);
                }
                hashMap.put(tokens[0], colour);
            }
        }

        scanner.close();
        return hashMap;
    }
}
