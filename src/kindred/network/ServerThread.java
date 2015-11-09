package kindred.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class ServerThread extends Thread {

    private Socket socket; // Socket connected to client
    private final String addr; // Client's 'IP:Port' address

    // Map containing a client that created a game room and the map's name
    private static ConcurrentHashMap<Socket, String> rooms = new ConcurrentHashMap<Socket, String>();

    // Queues containing messages to be sent to a Client
    private static ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>> clientQueue = new ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>>();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;

        // Adds client to hashMap (queue initially empty -> null)
        clientQueue.put(socket, new ConcurrentLinkedQueue<String>());

        addr = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        System.out.println("New connection: '" + addr + "'");
    }

    @Override
    public void run() {
        // Initialize socket input/output objects
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("'" + addr + "' error: Socket I/O definiton");
            e.printStackTrace();
        }

        // Main client loop
        try {
            // Read and parse client message
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("[" + addr + "]: " + inputLine);
                parse(inputLine);
            }
            // TODO: Send message if there is any in queue
        } catch (IOException e) {
            System.out.println("'" + addr
                    + "' error: Received null when reading socket");
        }

        // Close connection
        try {
            System.out.println("'" + addr + "' disconnected");
            clientQueue.remove(socket);
            socket.close();
        } catch (IOException e) {
            // TODO: Make a log file?
            e.printStackTrace();
            return;
        }
    }

    private String parse(String message) {
        /*
         * host <MAP> Creates a room to play on the specified map
         */
        if (message.startsWith("host ")) {
            String[] split = message.split(" ");
            if (split.length < 2)
                queueMessage("Not enough arguments");
            // TODO: Create room!
        }

        // Temporary
        return null;
    }

    private void queueMessage(String message) {
        // TODO: Add message to queue
    }

}
