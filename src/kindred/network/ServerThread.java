package kindred.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kindred.network.messages.ServerToClientMessage;

/**
 * Runs as a thread for each connected client. Reads and sends data to the
 * client.
 * 
 * @author Kindred Team
 */
class ServerThread extends Thread {

    /**
     * Name of the file containing information on how the client can interact
     * with the server.
     */
    private final String helpFile = "/kindred/network/help.txt";

    /**
     * Client's socket that is connected to the server.
     */
    private Socket socket;

    /**
     * Client's IP and port address, stored as "IP:Port".
     */
    private final String addr;

    /**
     * Client's nickname, is null if not yet defined.
     */
    private String nick = null;

    /**
     * If true, then the socket connected to the client will close the
     * connection to the server.
     */
    private boolean quitServer = false;

    /**
     * Thread-safe HashMap that stores client nicknames and their socket. Shared
     * among all threads.
     */
    private static ConcurrentHashMap<String, Socket> users = new ConcurrentHashMap<String, Socket>();

    /**
     * Thread-safe HashMap that stores nicknames of clients and the map on which
     * they are hosting a room. Shared among all threads.
     */
    private static ConcurrentHashMap<String, String> rooms = new ConcurrentHashMap<String, String>();

    /**
     * Thread-safe, stores Rooms of all games currently being played. Shared
     * among all threads.
     */
    private static Vector<Room> currentGames = new Vector<Room>();

    /**
     * Thread-safe HashMap that stores a thread-safe message queue for each
     * client socket. The queue is used to send messages to client in a
     * controlled fashion. Shared among all threads.
     */
    private static ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>> clientQueue = new ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>>();

    /**
     * Constructs a ServerThread.
     * 
     * @param socket
     *            socket connected to the client
     */
    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;

        // Adds client to hashMap (queue initially empty -> null)
        clientQueue.put(socket, new ConcurrentLinkedQueue<String>());

        this.addr = socket.getInetAddress().getHostAddress() + ":"
                + socket.getPort();
        System.out.println("New connection: " + this.addr);
    }

    /**
     * Reads data sent by the client and responds according the message queue. *
     */
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
                    out.println(msg);
                }
            }

        } catch (IOException e) {
            System.out.println("'" + addr
                    + "' error: Received null when reading socket");
        }

        // Close connection
        try {
            System.out.println("Disconnected: " + addr);

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
            e.printStackTrace();
            return;
        }
    }

    /**
     * Parses a given message sent by the client, putting an answer to be sent
     * to the client on their message queue.
     * 
     * @param message
     *            message to be parsed, received from the client
     */
    private void parse(String message) {
        // TODO: change the parameter to a ClientToServerMessage object and
        // handle it
        String[] splitStr = message.trim().split("\\s+");
        ServerToClientMessage msg;
        switch (splitStr[0].toUpperCase()) {

        // NICK [nickname] : Sets client's nickname as the specified value.
        // If no argument is given, returns client's nickname.
        case ("NICK"):
            if (splitStr.length != 2) {
                if (nick == null) {
                    msg = ServerToClientMessage.ERR_NICKNAME_IS_UNDEFINED;
                } else {
                    msg = ServerToClientMessage.INFO_NICKNAME;
                    msg.setArgument(nick);
                }
                queueMessage(socket, msg);
                return;
            }

            // Nickname must contain 3 to 10 alphanumeric characters, the first
            // one strictly being a letter
            if (!splitStr[1].matches("^[a-zA-Z]\\w{2,9}$")) {
                msg = ServerToClientMessage.ERR_NICKNAME_IS_INVALID;
                msg.setArgument(splitStr[1]);
                queueMessage(socket, msg);
                return;
            }
            if (users.containsKey(splitStr[1])) {
                msg = ServerToClientMessage.ERR_NICKNAME_IS_IN_USE;
                msg.setArgument(splitStr[1]);
                queueMessage(socket, msg);
                return;
            }

            // Prevents error when changing nickname
            if (nick != null && users.containsKey(nick))
                users.remove(nick);

            // Sets client's nickname
            nick = splitStr[1];
            users.put(nick, socket);
            msg = ServerToClientMessage.SUCC_NICKNAME_CHANGED;
            queueMessage(socket, msg);
            break;

        // MAPS : Returns all available maps
        case ("MAPS"):
            String maps = "";
            URL url = ServerThread.class.getResource("/kindred/data/map");
            File folder = new File(url.getPath());
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    String name = f.getName();
                    // Removes '.txt' from end of filename
                    name = name.substring(0, name.length() - 4);
                    maps += "|" + name;
                }
            }
            msg = ServerToClientMessage.INFO_AVAILABLE_MAPS;
            if (maps.length() >= 1)
                msg.setArgument(maps.substring(1)); // remove initial '|'
            queueMessage(socket, msg);
            break;

        // ROOMS : Shows all valid rooms in the server
        case ("ROOMS"):
            String roomStr = "";
            for (String r : rooms.keySet()) {
                roomStr += "|" + r + ">" + rooms.get(r);
            }
            msg = ServerToClientMessage.INFO_AVAILABLE_ROOMS;
            if (roomStr.length() >= 1)
                msg.setArgument(roomStr.substring(1)); // remove initial '|'
            queueMessage(socket, msg);
            break;

        // HOST <map> : Creates a room to play on the specified map
        case ("HOST"):
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }
            if (splitStr.length != 2) {
                msg = ServerToClientMessage.ERR_INVALID_COMMAND_OR_ARGUMENTS;
                queueMessage(socket, msg);
                return;
            }

            String mapName = splitStr[1];
            if (mapName.contains("..") || mapName.contains("/"))
                return;
            url = ServerThread.class.getResource("/kindred/data/map/" + mapName
                    + ".txt");
            if (url == null) {
                msg = ServerToClientMessage.ERR_MAP_NOT_FOUND;
                msg.setArgument(mapName);
                queueMessage(socket, msg);
                return;
            }

            // Creates a new room!
            rooms.put(nick, mapName);
            msg = ServerToClientMessage.SUCC_HOST;
            msg.setArgument(mapName);
            queueMessage(socket, msg);
            break;

        // UNHOST : Removes room if client is hosting one
        case ("UNHOST"):
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            // Removes room
            if (rooms.containsKey(nick)) {
                rooms.remove(nick);
                queueMessage(socket, ServerToClientMessage.SUCC_UNHOST);
            } else {
                queueMessage(socket,
                        ServerToClientMessage.ERR_CANNOT_UNHOST_WITHOUT_HOST);
            }
            break;

        // JOIN <nickname> : Connects to a room hosted by the specified user
        case ("JOIN"):
            if (splitStr.length != 2) {
                queueMessage(socket,
                        ServerToClientMessage.ERR_INVALID_COMMAND_OR_ARGUMENTS);
                return;
            }

            // Unregistered user cannot join a room
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            String host = splitStr[1];

            // Non-existent room
            if (!rooms.containsKey(host)) {
                msg = ServerToClientMessage.ERR_ROOM_NOT_FOUND;
                msg.setArgument(host);
                queueMessage(socket, msg);
                return;
            }

            // Own room
            if (nick.equals(host)) {
                queueMessage(socket, ServerToClientMessage.ERR_CANNOT_ENTER_OWN_ROOM);
                return;
            }

            // Entering another user's room => leaving their own room (if it
            // exists)
            if (rooms.containsKey(nick))
                queueMessage(socket, ServerToClientMessage.INFO_LEAVE_HOSTED_ROOM);

            // Lets the guest user know that they have successfully entered a
            // room
            msg = ServerToClientMessage.SUCC_JOIN;
            msg.setArgument(host);
            queueMessage(socket, msg);

            // Let the host user know that someone has entered their room
            msg = ServerToClientMessage.INFO_SOMEONE_ENTERED_ROOM;
            msg.setArgument(nick);
            queueMessage(users.get(host), msg);

            // Let the host client know that they will be the second player
            queueMessage(socket, ServerToClientMessage.INFO_SECOND_PLAYER);

            // Let the guest client know that they will be the first player
            queueMessage(users.get(host), ServerToClientMessage.INFO_FIRST_PLAYER);

            // Let both players know which map will be used
            msg = ServerToClientMessage.INFO_MAP;
            msg.setArgument(rooms.get(host));
            queueMessage(socket, msg);
            queueMessage(users.get(host), msg);

            // Create the room
            Room room = new Room(splitStr[1], nick);

            // Remove room from the list of the available rooms
            rooms.remove(host);

            // Add this room to the list of games
            currentGames.add(room);
            break;

        // QUIT : Makes client leave the server
        case ("QUIT"):
        case ("EXIT"):
            quitServer = true;
            queueMessage(socket, ServerToClientMessage.INFO_DISCONNECTED);
            break;

        // Something not understood
        default:
            queueMessage(socket,
                    ServerToClientMessage.ERR_INVALID_COMMAND_OR_ARGUMENTS);
            break;
        }
    }

    /**
     * Inserts a message in the queue of the corresponding socket which will
     * receive it. Escapes backslashes and newline characters.
     * 
     * @param socket
     *            socket whose message queue will receive the message to be
     *            sent.
     * @param message
     *            message to be queued
     */
    private void queueMessage(Socket socket, ServerToClientMessage msg) {
        String message = msg.toEncodedString();
        message = message.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n");
        clientQueue.get(socket).add(message);
    }

}
