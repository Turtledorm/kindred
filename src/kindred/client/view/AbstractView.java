package kindred.client.view;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import kindred.client.model.Game;
import kindred.client.model.Map;
import kindred.client.network.Client;
import kindred.common.ServerToClientMessage;

/**
 * Abstract class for a user interaction.
 * 
 * @author Kindred Team
 */
public abstract class AbstractView {
    /**
     * The game itself.
     */
    protected Game game;

    /**
     * Number of rows of the Map.
     */
    protected int height;

    /**
     * Number of columns of the Map.
     */
    protected int width;

    /**
     * A ResourceBundle used to get game messages in different languages.
     */
    protected ResourceBundle gameMsgBundle;

    /**
     * A ResourceBundle used to get menu messages in different languages.
     */
    protected ResourceBundle menuMsgBundle;

    /**
     * The locale used by the program to show messages to the Client.
     */
    protected Locale locale;

    /**
     * Creates an interface to the user.
     * <p>
     * The implementing subclasses must implement methods to display a Map to
     * the users and to get their actions.
     */
    public AbstractView() {
        this.locale = new Locale("pt", "BR");
        try {
            readLanguageData();
            return;
        } catch (MissingResourceException e) {
            System.err.println("Could not find language data for the locale: "
                    + locale.toString());
        }
        this.locale = new Locale("en", "GB");
        try {
            readLanguageData();
            return;
        } catch (MissingResourceException e) {
            System.err
                    .println("Also, the default language ("
                            + locale.toString()
                            + ") could not be found. I'm sorry! :'(\nPlease reinstall this program and be happy!");
            System.exit(1);
        }
    }

    /**
     * Reads the files containing the messages in the specified language.
     */
    protected abstract void readLanguageData();

    /**
     * Asks the user to enter a string.
     * 
     * @param message
     *            the message to be shown to the user
     * @return the string provided by the used after removing trailing
     *         whitespace characters
     */
    public abstract String askForString(String message);

    /**
     * Defines the Game during which this class will interact with the user.
     * 
     * @param game
     *            the Game during which this class will interact with the user.
     */
    public void setGame(Game game) {
        this.game = game;
        Map map = game.getMap();
        height = map.getMapHeight();
        width = map.getMapWidth();
    }

    /**
     * Prompts the user for the kindred.server IP.
     * 
     * @return the kindred.server IP
     */
    public abstract String promptForIP();

    /**
     * Informs the user whether the connection to the Server was successful or
     * not.
     * 
     * @param success
     *            {@code true} if the connection was successful or {@code false}
     *            otherwise
     * @param serverIP
     *            the kindred.server IP
     */
    public abstract void connectionResult(boolean success, String serverIP);

    /**
     * Informs the user that the connection to the Server has been lost.
     */
    public abstract void connectionLost();

    /**
     * Prompts the user for an action in the menu, i.e., while not in a room.
     * 
     * @param client
     *            the user's Client object whose methods will be called
     * @return {@code false} if the action is quit, or {@code true} otherwise
     */
    public abstract boolean promptForMenuAction(Client client);

    /**
     * Prompts the user for an action in the game and (if the user chooses a
     * valid action) calls the corresponding method of map.
     * 
     * @param client
     *            the user's Client object whose methods will be called
     * @return {@code true} if the user has finished their turn, or
     *         {@code false} otherwise
     */
    public abstract boolean promptForGameAction(Client client);

    /**
     * Describes a received message from the kindred.server to the
     * kindred.client.
     * 
     * @param msg
     *            message received from the kindred.server
     */
    public abstract void remoteEvent(ServerToClientMessage msg);

    /**
     * Displays the Map to the user.
     */
    public abstract void displayMap();

    /**
     * Retrieves a message from the ResourceBundle language according to the
     * specified key and its arguments.
     * 
     * @param rb
     *            the ResourceBundle containing messages for a certain language
     * @param key
     *            key to access the language message
     * @param arg
     *            arguments for the specified key
     * @return the formatted String
     */
    protected String format(ResourceBundle rb, String key, Object[] arg) {
        String pattern = rb.getString(key);
        MessageFormat formatter = new MessageFormat(pattern, locale);
        return formatter.format(arg);
    }

    /**
     * Closes the user interface.
     */
    public abstract void close();
}
