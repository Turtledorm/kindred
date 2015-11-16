package kindred;

import kindred.network.Client;
import kindred.view.AbstractView;
import kindred.view.cli.CLI;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        String IP = args.length < 1 ? null : args[0];
        AbstractView view = new CLI();
        Client client = new Client(IP, view);
        Thread thread = new Thread(client);
        thread.start();
        client.mainLoop();

    }
}
