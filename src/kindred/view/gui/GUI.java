package kindred.view.gui;

import kindred.network.Client;
import kindred.network.messages.ServerToClientMessage;
import kindred.view.AbstractView;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Graphical User Interface view for the user. Not implemented yet!
 * 
 * @author Kindred Team
 */
public class GUI extends AbstractView {

    /**
     * Constructs a GUI. Since this class isn't implemented yet, so it throws an
     * exception.
     * 
     * @throws NotImplementedException
     *             since it isn't implemented
     */
    public GUI() {
        super();
        throw new NotImplementedException();
    }

    @Override
    protected void readLanguageData() {
    }

    @Override
    public String askForString(String message) {
        return "";
    }

    @Override
    public void displayMap() {
    }

    @Override
    public String promptForIP() {
        return "";
    }

    @Override
    public boolean promptForMenuAction(Client client) {
        return false;
    }

    @Override
    public boolean promptForGameAction(Client client) {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public void connectionResult(boolean success, String serverIP) {
    }

    @Override
    public void remoteEvent(ServerToClientMessage msg) {
    }

    @Override
    public void connectionLost() {
    }

}
