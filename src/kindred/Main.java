package kindred;

import kindred.network.Client;
import kindred.view.AbstractView;
import kindred.view.cli.CLI;

public class Main {

    // Currently used for testing Game
    public static void main(String[] args) {
        if (args.length < 1) {
            // TODO: Create an output method on view
            System.out.println("Must call with server IP as first argument!");
            System.exit(1);
        }

        AbstractView view = new CLI();
        Client client = new Client(args[0], view);
        System.out.println("Connection established with server! IP = " + args[0]);
        System.out.println("Type in commands below");
        Thread thread = new Thread(client);
        thread.start();

        while (thread.isAlive()) {
            String msg = view.askForString("");
            // client.send(msg);
        }
    }
}
