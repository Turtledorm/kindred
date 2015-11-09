package kindred.view;

import kindred.model.Map;

public abstract class AbstractView {

    protected Map map;

    public AbstractView(Map map) {
        this.map = map;
    }

    public abstract void displayMap();

    public abstract void promptForAction();
}
