package kindred.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import kindred.network.NetworkConstants;
import kindred.view.AbstractView;

public class Client implements Runnable {

    // Socket and port to connect to server
    private Socket socket;
    private final int port = 8000;

    // Client's interaction with the program
    private AbstractView view;

    // I/O for server socket
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    private String nickname, opponent;
    private Game game; // TODO: Create game if CONNECT worked
    private boolean player1;

    public Client(String ipServer, AbstractView view) {
        // Create socket and connect to server
        try {
            socket = new Socket(ipServer, port);
        } catch (IOException e) {
            System.out
                    .println("Couldn't connect to server (IP = " + ipServer + ")!");
            System.exit(1);
        }

        // Define socket I/O
        try {
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error when defining I/O with server!");
            System.exit(1);
        }

    }

    public void send(String message) {
        serverOut.println(message);
    }

    public void run() {
        String response;

        try {
            while ((response = serverIn.readLine()) != null) {
                response = response.replaceAll("\\\\n", "\n").replaceAll("\\\\\\\\",
                        "\\\\");

                int prefixPos = response.indexOf(NetworkConstants.PREFIX_CHAR);
                String prefix = response.substring(0, prefixPos - 1);

                String message = response.substring(prefixPos + 1,
                        response.length());

                // Analyzes prefix
                switch (prefix.substring(0, prefixPos - 1)) {
                case NetworkConstants.PREF_NICK:
                    nickname = prefix.substring(prefixPos - 1);
                    break;
                case NetworkConstants.PREF_OPPONENT:
                    opponent = prefix.substring(prefixPos - 1);
                    break;
                case NetworkConstants.PREF_P1:
                    player1 = true;
                    break;
                case NetworkConstants.PREF_P2:
                    player1 = false;
                    break;
                case NetworkConstants.PREF_MAP:
                    String mapFilename = prefix.substring(prefixPos - 1);
                    game = new Game(nickname, opponent,
                            "/kindred/data/maps/" + mapFilename + ".txt",
                            "/kindred/data/terrain/terrainColors.txt", view);
                    break;
                case NetworkConstants.PREF_BYE:
                    socket.close();
                    break;
                }

                // TODO: Print according to CLI
                if (!message.trim().equals(""))
                    System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Error when connecting to server!");
            System.exit(1);
        }

        // Unexpected 'null' from socket; server suddenly closed
        if (!socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Connection with server has been lost!");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
