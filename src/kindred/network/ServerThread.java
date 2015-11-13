package kindred.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class ServerThread extends Thread {

    private final String helpFile = "/kindred/network/help.txt";
    private final int gamePort = 8000;

    private Socket socket; // Socket connected to client
    private final String addr; // Client's 'IP:Port' address

    private String nick = null;
    private boolean quitServer = false;

    // Name of each client and his socket
    private static ConcurrentHashMap<String, Socket> users = new ConcurrentHashMap<String, Socket>();

    // Map containing a client that created a game room and the map's name
    private static ConcurrentHashMap<String, String> rooms = new ConcurrentHashMap<String, String>();

    // All occurring games
    private static Vector<Room> currentGames = new Vector<Room>();

    // Queues containing messages to be sent to a Client
    private static ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>> clientQueue = new ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>>();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;

        // Adds client to hashMap (queue initially empty -> null)
        clientQueue.put(socket, new ConcurrentLinkedQueue<String>());

        this.addr = socket.getInetAddress().getHostAddress() + ":"
                + socket.getPort();
        System.out.println("New connection: '" + this.addr + "'");
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
            while ((!quitServer && (inputLine = in.readLine()) != null)) {
                if (inputLine.trim().equals(""))
                    continue;
                System.out.println("[" + addr + "]: " + inputLine);
                parse(inputLine);
                while (!clientQueue.get(socket).isEmpty()) {
                    String msg = clientQueue.get(socket).remove();
                    System.out.println(msg);
                    out.println(msg);
                }
            }

            // TODO: Send message if there is any in queue
        } catch (IOException e) {
            System.out.println("'" + addr
                    + "' error: Received null when reading socket");
        }

        // Close connection
        try {
            System.out.println("'" + addr + "' disconnected");

            // Remove all data related to client
            if (nick != null) {
                for (Room r : currentGames) {
                    if (r.hasNick(nick)) {
                        currentGames.remove(r);
                        break;
                    }
                }
                rooms.remove(nick);
                users.remove(nick);
            }
            clientQueue.remove(socket);

            socket.close();
        } catch (IOException e) {
            // TODO: Make a log file?
            e.printStackTrace();
            return;
        }
    }

    private void parse(String message) {
        String[] splitStr = message.trim().split("\\s+");

        switch (splitStr[0].toUpperCase()) {

        // NICK <nickname> : Sets client's nickname as the specified value
        case ("NICK"):
            if (splitStr.length != 2) {
                queueMessage(socket, "Invalid argument for 'NICK' command!");
                return;
            }

            // Nickname must contain 3 to 10 alphanumeric characters, the first
            // one strictly being a letter
            if (!splitStr[1].matches("^[a-zA-Z]\\w{2,9}$")) {
                queueMessage(socket, "Nickname '" + splitStr[1]
                        + "' contains invalid characters!");
                return;
            }
            if (users.containsKey(splitStr[1])) {
                queueMessage(socket, "Nickname '" + splitStr[1]
                        + "' already in use!");
                return;
            }

            // Prevents error when changing nickname
            if (nick != null && users.containsKey(nick))
                users.remove(nick);

            // Sets client's nickname
            nick = splitStr[1];
            users.put(nick, socket);
            queueMessage(socket, "Nickname successfully changed to '" + splitStr[1]
                    + "'!");
            break;

        // MAPS : Returns all available maps
        case ("MAPS"):
            String maps = "";
            File folder = new File("./kindred/data/map");
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    String name = f.getName();
                    maps += "- " + name + '\n';
                }
            }
            queueMessage(socket, maps.trim());
            break;

        // ROOMS : Shows all valid rooms in the server
        case ("ROOMS"):
            String roomStr = "";
            for (String r : rooms.keySet()) {
                roomStr += "[" + r + "] -> " + rooms.get(r);
            }
            queueMessage(socket, roomStr);
            break;

        // HOST <map> : Creates a room to play on the specified map
        case ("HOST"):
            if (nick == null) {
                queueMessage(socket, "You must be registered to use 'HOST' command!");
                return;
            }
            if (splitStr.length != 2) {
                queueMessage(socket, "Invalid argument for 'HOST' command!");
                return;
            }

            String mapName = splitStr[1];
            File file = new File("./kindred/data/map/" + mapName + ".txt");
            if (!file.exists()) {
                queueMessage(socket, "Map '" + mapName + "' not found!");
                return;
            }

            // Creates a new room!
            rooms.put(nick, mapName);
            queueMessage(socket, "Created room to play on map '" + mapName + "'!");
            break;

        // UNHOST : Removes room if client is hosting one
        case ("UNHOST"):
            if (nick == null) {
                queueMessage(socket,
                        "You must be registered to use 'UNHOST' command!");
                return;
            }

            // Removes room
            if (rooms.containsKey(nick)) {
                rooms.remove(nick);
                queueMessage(socket, "You aren't hosting a room anymore.");
            } else {
                queueMessage(socket, "You aren't even hosting a room!");
            }
            break;

        // CONNECT <nickname> : Connects to a room hosted by the specified user
        case ("CONNECT"):
            if (nick == null) {
                queueMessage(socket,
                        "You must be registered to use 'CONNECT' command!");
                return;
            }

            if (splitStr.length != 2) {
                queueMessage(socket, "Invalid argument for 'CONNECT' command!");
                return;
            }

            if (!rooms.containsKey(splitStr[1])) {
                queueMessage(socket, "Room hosted by '" + splitStr[1]
                        + "' not found!");
                return;
            }

            if (rooms.containsKey(nick))
                queueMessage(socket, "You have left your hosted room.");

            Room room = new Room(splitStr[1], nick);
            rooms.remove(splitStr[1]);
            currentGames.add(room);
            queueMessage(socket, "Entered room created by '" + splitStr[1] + "'!");
            queueMessage(users.get(splitStr[1]), "'" + nick
                    + "' has entered the room!");
            // TODO: Send map
            break;

        // HELP : Shows commands supported by server
        case ("HELP"):
            String helpStr = help();
            queueMessage(socket, helpStr);
            break;

        // QUIT : Makes client leave the server
        case ("QUIT"):
        case ("EXIT"):
            quitServer = true;
            queueMessage(socket, "You have disconnected from the server.");
            break;

        // Something not understood
        default:
            queueMessage(socket, "Command not recognized!");
            break;
        }
    }

    private void queueMessage(Socket socket, String message) {
        clientQueue.get(socket).add(message);
    }

    private String help() {
        File file = new File(ServerThread.class.getResource(helpFile).getPath());
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return "Help file not found!";
        }

        String msg = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return msg;
    }
}
