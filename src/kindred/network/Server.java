package kindred.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server implements Runnable {

    private ServerSocket serverSocket;

    public void loop(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Couldn't bind server to port " + port + "!");
            System.exit(1);
        }

        System.out.println("Server successfully opened on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                new ServerThread(serverSocket.accept()).start();
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Error when treating client I/O!");
                    System.exit(1);
                }
            }
        }
    }

    public void run() {
        Scanner input = new Scanner(System.in);

        if (input.next().toUpperCase().equals("CLOSE"))
            try {
                serverSocket.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        input.close();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Must specify port as argument!");
            System.exit(1);
        }

        try {
            Server server = new Server();
            (new Thread(server)).start();
            server.loop(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            System.out.println("Specified port is invalid!");
        }
    }
}
