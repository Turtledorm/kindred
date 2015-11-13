package kindred.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    // Socket and port to connect to server
    private Socket socket;
    private final int port = 8000;

    // I/O for server socket
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    private Game game; // TODO: Create game if CONNECT worked

    public Client(String ipServer) {
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
            serverIn = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
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
                // Debug for now...
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Error when connecting to server!");
            System.exit(1);
        }

        // Received 'null' from socket; server is closed
        try {
            socket.close();
            System.out.println("Connection with server has been lost!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
