package kindred.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import kindred.model.Game;
import kindred.model.Map;
import kindred.network.Client;
import kindred.network.messages.ServerToClientMessage;
import kindred.view.AbstractView;

/**
 * 
 * Provides a command-line user interface to interact with the user through a
 * terminal.
 * 
 * @author Kindred Team
 * 
 */
public class CLI extends AbstractView {

    /**
     * A map of atoms representing the tiles of the map.
     */
    private Atom[][] atomMap;

    /**
     * Name of the file containing the background colours of the terrains.
     */
    private final String colourFile = "/kindred/data/terrain/terrainColors.txt";

    /**
     * Maps names of terrains to their corresponding background colours.
     */
    private HashMap<String, Colour> colourTypes;

    /**
     * The scanner object to get user's input from the command-line interface.
     */
    private final Scanner scanner;

    /**
     * Creates a command-line user interface for the game.
     */
    public CLI() {
        super();
        scanner = new Scanner(System.in);

    }

    @Override
    protected void readLanguageData() {
        menuMsgBundle = ResourceBundle.getBundle(
                "kindred.lang.CLI-MessageBundle_Menu", locale);
        gameMsgBundle = ResourceBundle.getBundle(
                "kindred.lang.CLI-MessageBundle_Game", locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String askForString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGame(Game game) {
        super.setGame(game);
        atomMap = new Atom[height][width];
        try {
            colourTypes = CLITerrainParser.parseFile(colourFile);
        } catch (FileNotFoundException e) {
            System.err.format("File '%s' not found!\n", colourFile);
            System.exit(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayMap() {
        Map map = game.getMap();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!colourTypes.containsKey(map.getTile(i, j).getTerrain()
                        .getName())) {
                    System.err.println("Terrain not found in CLI colour file '"
                            + map.getTile(i, j).getTerrain().getName() + "'");
                    // new MessageFormat(
                    // msgBundle.getString("terrain_colour_not_found"), locale);
                    System.exit(1);
                }
                Colour foregroundColour = Colour.LIGHT_RED;
                Colour backgroundColour = colourTypes.get(map.getTile(i, j)
                        .getTerrain().getName());
                char character = ' ';
                atomMap[i][j] = new Atom(character, backgroundColour,
                        foregroundColour);
            }
        }

        TerminalColourHelper.drawMatrix(atomMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String promptForIP() {
        System.out.println(menuMsgBundle.getString("enter_ip"));
        if (scanner.hasNextLine())
            return scanner.nextLine().trim();
        else
            return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean promptForMenuAction(Client client) {
        System.out.println(menuMsgBundle.getString("type_in_your_command"));
        printPrompt();

        for (; scanner.hasNextLine(); printPrompt()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] separate = line.trim().split("\\s+");
            switch (separate[0].toUpperCase()) {
            case "JOIN":
                if (separate.length != 2) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                String host = separate[1];
                client.join(host);
                break;

            case "NICK":
                if (separate.length > 2) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                } else if (separate.length == 1) {
                    client.nick();
                } else {
                    String nickname = separate[1];
                    client.nick(nickname);
                }
                break;

            case "MAPS":
                if (separate.length != 1) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                client.maps();
                break;

            case "ROOMS":
                if (separate.length != 1) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                client.rooms();
                break;

            case "HELP":
                if (separate.length != 1) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                if (!printFileContent("kindred/view/cli/menuHelp.txt")) {
                    System.err.println(menuMsgBundle.getString("help_not_found"));
                    continue;
                }
                break;

            case "QUIT":
            case "EXIT":
                if (separate.length != 1) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                client.quit();
                break;

            case "HOST":
                if (separate.length != 2) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                String mapName = separate[1];
                client.host(mapName);
                break;

            case "UNHOST":
                if (separate.length != 1) {
                    System.out.println(menuMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                client.unhost();
                break;

            default:
                System.out.println(menuMsgBundle.getString("unrecognised_command"));
                continue;
            }
            return true;
        }
        client.quit();
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean promptForGameAction() {
        System.out.println(gameMsgBundle.getString("type_in_your_command"));
        printPrompt();

        for (; scanner.hasNextLine(); printPrompt()) {
            int[] positions;

            String line = scanner.nextLine().trim().toLowerCase();
            String[] separate = line.split("\\s+");
            switch (separate[0]) {
            case "move":
            case "mv":
                if (separate.length != 5) {
                    System.out.println(gameMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    game.move(positions[0], positions[1], positions[2], positions[3]);
                break;
            case "attack":
            case "atk":
                if (separate.length != 5) {
                    System.out.println(gameMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null)
                    game.attack(positions[0], positions[1], positions[2],
                            positions[3]);
                break;
            case "info":
                if (separate.length != 3) {
                    System.out.println(gameMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                positions = parsePosition(separate);
                if (positions != null) {
                    String message = game.getMap().getTileInfo(positions[0],
                            positions[1]);
                    System.out.println(message);
                }
                break;
            case "end":
                if (separate.length != 1) {
                    System.out.println(gameMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                return true;
            case "surrender":
                if (separate.length != 1) {
                    System.out.println(gameMsgBundle
                            .getString("invalid_argument_for_command"));
                    continue;
                }
                // TODO: send to server
                return true;
            case "help":
                if (!printFileContent("kindred/view/cli/gameHelp.txt")) {
                    System.err.println(gameMsgBundle.getString("help_not_found"));
                    continue;
                }
                break;
            default:
                System.out.println(gameMsgBundle.getString("unrecognised_command"));
                continue;
            }
        }
        return false;
    }

    /**
     * Parses an array of strings containing numbers, except for the first
     * element, which is supposedly a command (ignored by this method). As the
     * numbers represent positions (row, column), there must be an even quantity
     * of numbers. This method also checks if the position is valid (i.e., is
     * inside the bounds of the map)
     * 
     * @param separate
     *            an array of strings containing anything in the first position
     *            and an even quantity of numbers in the rest of it
     * @return an array of {@code int}s representing the positions given in the
     *         strings of {@code separate}, or {@code null} in case of errors
     */
    private int[] parsePosition(String[] separate) {
        Map map = game.getMap();
        int[] positions = new int[separate.length - 1];
        try {
            for (int i = 1; i < separate.length; i++) {
                positions[i - 1] = Integer.parseInt(separate[i]) - 1;
            }
        } catch (NumberFormatException ex) {
            System.out.println(gameMsgBundle.getString("position_not_a_number"));
            return null;
        }

        for (int i = 1; i < separate.length; i += 2) {
            if (!map.validPosition(positions[i - 1], positions[i])) {
                System.out
                        .println(gameMsgBundle.getString("position_out_of_bounds"));
                return null;
            }
        }

        return positions;
    }

    /**
     * Displays a prompt message to the user.
     */
    private static void printPrompt() {
        System.out.print(">> ");
    }

    @Override
    public void close() {
        System.out.println("\n...");
        scanner.close();
    }

    private boolean printFileContent(String filename) {
        URL url = CLI.class.getResource(filename);
        if (url == null)
            return false;
        File file = new File(url.getPath());
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionResult(boolean success, String serverIP) {
        String key = success ? "connection_established" : "connection_error";
        String message = format(menuMsgBundle, key, new Object[] { serverIP });
        System.out.println(message);
    }

    /**
     * {@inheritDoc}
     */
    public void menuEvent(ServerToClientMessage msg) {
        String argument = msg.getArgument();
        Object[] arg = null;
        String key = "";
        String complement = "";
        switch (msg) {
        case ERR_CANNOT_ENTER_OWN_ROOM:
            key = "cannot_enter_own_room";
            arg = new Object[] {};
            break;
        case ERR_CANNOT_UNHOST_WITHOUT_HOST:
            key = "cannot_unhost_without_host";
            arg = new Object[] {};
            break;
        case ERR_INVALID_COMMAND_OR_ARGUMENTS:
            key = "unrecognised_command";
            arg = new Object[] {};
            break;
        case ERR_MAP_NOT_FOUND:
            key = "map_not_found";
            arg = new Object[] { argument };
            break;
        case ERR_NICKNAME_IS_INVALID:
            key = "invalid_nickname";
            arg = new Object[] { argument };
            break;
        case ERR_NICKNAME_IS_IN_USE:
            key = "unavailable_nickname";
            arg = new Object[] { argument };
            break;
        case ERR_NICKNAME_IS_UNDEFINED:
            key = "undefined_nickname";
            arg = new Object[] {};
            break;
        case ERR_ROOM_NOT_FOUND:
            key = "room_not_found";
            arg = new Object[] { argument };
            break;
        case ERR_UNREGISTERED_USER:
            key = "unregistered_user";
            arg = new Object[] {};
            break;
        case INFO_AVAILABLE_MAPS:
            String[] maps = argument.split("\\|");
            String message = "";
            if (argument.isEmpty()) {
                key = "no_map";
                arg = new Object[] {};
            } else {
                key = "available_maps";
                for (String m : maps)
                    message += "\n- " + m;
                arg = new Object[] {};
                complement = message;
            }
            break;
        case INFO_AVAILABLE_ROOMS:
            String[] rooms = argument.split("\\|");
            message = "";
            if (argument.isEmpty()) {
                key = "no_room";
                arg = new Object[] {};
            } else {
                key = "available_rooms";
                for (String r : rooms) {
                    String[] roomParts = r.split(">");
                    message += String.format("\n- %-10s -> %s", roomParts[0],
                            roomParts[1]);
                }
                arg = new Object[] {};
                complement = message;
            }
            break;
        case INFO_NICKNAME:
            key = "current_nickname";
            arg = new Object[] { argument };
            break;
        case SUCC_HOST:
            key = "created_room";
            arg = new Object[] { argument };
            break;
        case INFO_SOMEONE_ENTERED_ROOM:
            key = "someone_entered_room";
            arg = new Object[] { argument };
            complement = "\n" + gameStartedMessage(argument);
            break;
        case SUCC_JOIN:
            key = "successfully_joined_room";
            arg = new Object[] { argument };
            complement = "\n" + gameStartedMessage(argument);
            break;
        case SUCC_LEAVE:
            key = "leaving";
            arg = new Object[] {};
            break;
        case SUCC_NICKNAME_CHANGED:
            key = "changed_nickname";
            arg = new Object[] { argument };
            break;
        case INFO_LEAVE_HOSTED_ROOM: // after entering another room
        case SUCC_UNHOST: // after asking to unhost
            key = "left_hosted_room";
            arg = new Object[] {};
            break;
        }
        System.out.println(format(menuMsgBundle, key, arg) + complement);
    }

    private String gameStartedMessage(String argument) {
        String[] parts = argument.split("\\|");
        Integer playerNo = Integer.parseInt(parts[1]);
        String mapName = parts[2];
        Object[] arg = new Object[] { playerNo, mapName };
        return format(menuMsgBundle, "game_start", arg);
    }
}
