package kindred.view;

import kindred.model.Map;

public abstract class AbstractView {

    protected Map map;

    /**
     * Creates an interface to the user.
     * <p>
     * The implementing subclasses must implement methods to display a map to
     * the users and to get their actions.
     * 
     * @param map
     *            the map of the game
     */
    public AbstractView(Map map) {
        this.map = map;
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
}
