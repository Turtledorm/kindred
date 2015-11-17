package kindred.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * Implements the main loop of a TCP listening socket Server.
 * 
 * @author Kindred Team
 */
public class Server implements Runnable {

    /**
     * Default port used by the Server.
     */
    public static final int DEFAULT_PORT = 8000;

    /**
     * Server's listening TCP socket.
     */
    private ServerSocket serverSocket;

    /**
     * Initializes this Server's socket and accepts connections from Clients,
     * creating a new socket and thread to treat each connected user.
     */
    public void loop() {
        int port = DEFAULT_PORT;

        // Tries to initialize the Server's socket
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Couldn't bind server to port " + port + "!");
            System.exit(1);
        }

        System.out.println("Server successfully opened on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                // Creates a thread for each accepted connection
                new ServerThread(serverSocket.accept()).start();
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Error when treating client I/O!");
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Thread used for verifying if Server must be closed (this happens when
     * "CLOSED" is typed).
     */
    public void run() {
        Scanner input = new Scanner(System.in);

        while (!input.nextLine().trim().toUpperCase().equals("CLOSE")) {
            ; // Do nothing!
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        input.close();
        System.out.println("Until next time!");
        System.exit(0);
    }

    /**
     * Starts a Server.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
        try {
            Server server = new Server();
            (new Thread(server)).start();
            server.loop();
        } catch (NumberFormatException e) {
            System.out.println("Specified port is invalid!");
        }
    }
}
