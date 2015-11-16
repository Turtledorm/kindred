package kindred.view.gui;

import kindred.network.Client;
import kindred.view.AbstractView;

public class GUI extends AbstractView {

    public GUI() {
        super();
    }

    @Override
    public String askForString(String message) {
        return "";
    }

    @Override
    public void displayMap() {
    }

    @Override
    public void promptForMenuAction(Client client) {

    }

    @Override
    public boolean promptForGameAction() {
        return false;
    }

    @Override
    public void close() {
    }

}
