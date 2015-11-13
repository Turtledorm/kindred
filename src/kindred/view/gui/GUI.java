package kindred.view.gui;

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
    public boolean promptForAction() {
        return false;
    }

    @Override
    public void close() {
    }

}
