package kindred.client.view.gui;

import kindred.client.network.Client;
import kindred.client.view.AbstractView;
import kindred.common.ServerToClientMessage;

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
     * @throws Exception
     *             since it isn't implemented
     */
    public GUI() throws Exception {
        super();
        throw new Exception("This class will be implemented in a future release.");
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
