package kindred.view;

import java.util.Locale;
import java.util.ResourceBundle;

import kindred.model.Game;
import kindred.model.Map;
import kindred.network.Client;

public abstract class AbstractView {
    /**
     * The game itself.
     */
    protected Game game;

    /**
     * The number of rows of the map.
     */
    protected int height;

    /**
     * The number of columns of the map.
     */
    protected int width;

    /**
     * A ResourceBundle used to get game messages in different languages
     */
    protected ResourceBundle gameMsgBundle;

    /**
     * A ResourceBundle used to get menu messages in different languages
     */
    protected ResourceBundle menuMsgBundle;

    /**
     * The locale used by the program to show messages to the client.
     */
    protected Locale locale;

    /**
     * Creates an interface to the user.
     * <p>
     * The implementing subclasses must implement methods to display a map to
     * the users and to get their actions.
     */
    public AbstractView() {
        this.locale = new Locale("pt", "BR");
    }

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
     * Defines the map of the game.
     * 
     * @param map
     *            the map of the game
     */
    public void setGame(Game game) {
        this.game = game;
        Map map = game.getMap();
        height = map.getHeight();
        width = map.getWidth();
    }

    /**
     * Displays the map to the user.
     */
    public abstract void displayMap();

    /**
     * Prompts the user for an action in the menu (i.e. not while in a room).
     * 
     * @param client
     *            the user's client object whose methods will be called
     * 
     */

    public abstract void promptForMenuAction(Client client);

    /**
     * Prompts the user for an action in the game and (if the user chooses a
     * valid action) calls the corresponding method of {@code map}.
     * 
     * @return true if the user has finished their turn, false otherwise
     */
    public abstract boolean promptForGameAction();

    /**
     * Closes the user interface.
     */
    public abstract void close();

}
