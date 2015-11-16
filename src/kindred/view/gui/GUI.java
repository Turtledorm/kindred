package kindred.view.gui;

import kindred.network.Client;
import kindred.network.messages.ServerToClientMessage;
import kindred.view.AbstractView;

public class GUI extends AbstractView {

    public GUI() {
        super();
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
    public boolean promptForGameAction() {
        return false;
    }

    @Override
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionResult(boolean success, String serverIP) {
    }

    public void menuEvent(ServerToClientMessage msg) {

    }

    @Override
    public void connectionLost() {

    }

}
