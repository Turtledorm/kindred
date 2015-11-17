package kindred.view;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import kindred.model.Game;
import kindred.model.Map;
import kindred.network.Client;
import kindred.network.messages.ServerToClientMessage;

// TODO: Check Javadoc for this class
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
            System.err.println("Also, the default language (" + locale.toString()
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
     * Defines the Map of the game.
     * 
     * @param map
     *            the Map of the game
     */
    public void setGame(Game game) {
        this.game = game;
        Map map = game.getMap();
        height = map.getMapHeight();
        width = map.getMapWidth();
    }

    /**
     * Displays the Map to the user.
     */
    public abstract void displayMap();

    /**
     * Prompts the user for an IP.
     */
    public abstract String promptForIP();

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
     * valid action) calls the corresponding method of {@code map}.
     * 
     * @return {@code true} if the user has finished their turn, or
     *         {@code false} otherwise
     */
    public abstract boolean promptForGameAction();

    /**
     * Closes the user interface.
     */
    public abstract void close();

    /**
     * 
     * @param key
     * @param arg
     * @return
     */
    protected String format(ResourceBundle rb, String key, Object[] arg) {
        String pattern = rb.getString(key);
        MessageFormat formatter = new MessageFormat(pattern, locale);
        return formatter.format(arg);
    }

    public abstract void connectionResult(boolean success, String serverIP);

    public abstract void menuEvent(ServerToClientMessage msg);

    public abstract void connectionLost();
}
