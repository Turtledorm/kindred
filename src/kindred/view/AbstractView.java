package kindred.view;

import java.util.Locale;
import java.util.ResourceBundle;

import kindred.model.Map;

public abstract class AbstractView {

    /**
     * The Map of the game.
     */
    protected Map map;

    /**
     * The number of rows of the map.
     */
    protected int height;

    /**
     * The number of columns of the map.
     */
    protected int width;

    /**
     * A ResourceBundle used to get messages in different languages
     */
    protected ResourceBundle msgBundle;

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
    public void setMap(Map map) {
        this.map = map;
        height = map.getHeight();
        width = map.getWidth();
    }

    /**
     * Displays the map to the user.
     */
    public abstract void displayMap();

    /**
     * Prompts the user for an action in the game and (if the user chooses a
     * valid action) calls the corresponding method of {@code map}.
     * 
     * @return true if the user has finished their turn, false otherwise
     */
    public abstract boolean promptForAction();

    /**
     * Closes the user inteface.
     */
    public abstract void close();

}
