package kindred.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import kindred.model.UnitFileParser;

/**
 * Provides a method to parse a file that contains the description of Units in a
 * command-line user interface.
 * 
 * @author Kindred Team
 */

public class CLIUnitParser {

    /**
     * Do not instantiate!
     */
    private CLIUnitParser() {
        // Do not instantiate!
    }

    /**
     * Parses a file that describes the display of a Unit in a terminal
     * interface.
     * <p>
     * The file must have lines containing the name of each Unit and a Character
     * representing it separated by whitespaces.
     * <p>
     * The name of the Unit must be exactly the same as the one in the file
     * parsed by {@link UnitFileParser}.
     * 
     * @param filename
     *            name of the file informing symbols for each Unit
     * @return HashMap mapping each Unit name to a Character representing it
     * @throws FileNotFoundException
     *             if the specified file is not found
     */
    public static HashMap<String, Character> parseFile(String filename)
            throws FileNotFoundException {
        File f = new File(CLIUnitParser.class.getResource(filename).getPath());
        Scanner scanner = new Scanner(f);
        HashMap<String, Character> hashMap = new HashMap<String, Character>();

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
                hashMap.put(tokens[0], tokens[1].charAt(0));
            }
        }

        scanner.close();
        return hashMap;
    }
}
