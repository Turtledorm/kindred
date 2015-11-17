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

import kindred.network.messages.ClientToServerMessage;
import kindred.network.messages.ServerToClientMessage;

/**
 * Runs as a Server thread for a connected Client, being responsible for
 * exchanging data with the user.
 * 
 * @author Kindred Team
 */
class ServerThread extends Thread {

    /**
     * Client's socket that is connected to the Server.
     */
    private Socket socket;

    /**
     * Client's IP and port address, stored as "IP:Port".
     */
    private final String addr;

    /**
     * Client's nickname, equals {@code null} if not yet defined.
     */
    private String nick = null;

    /**
     * If {@code true}, then the socket connected to the Client will close the
     * connection to the Server.
     */
    private boolean quitServer = false;

    /**
     * Thread-safe HashMap that stores a thread-safe message queue for each
     * Client socket. The queue is used to send messages to Clients in a
     * controlled fashion. Shared among all threads.
     */
    private static ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>> clientQueue = new ConcurrentHashMap<Socket, ConcurrentLinkedQueue<String>>();

    /**
     * Thread-safe HashMap that stores Client nicknames and their socket. Shared
     * among all threads.
     */
    private static ConcurrentHashMap<String, Socket> nicksToSocks = new ConcurrentHashMap<String, Socket>();

    /**
     * Thread-safe HashMap that stores the nicknames of Clients that are hosting
     * a room, and the name of the map they wish to play on. Shared among all
     * threads.
     */
    private static ConcurrentHashMap<String, String> hostRooms = new ConcurrentHashMap<String, String>();

    /**
     * Thread-safe Vector that stores Rooms of all games currently being played.
     * Shared among all threads.
     */
    private static Vector<Room> currentGames = new Vector<Room>();

    /**
     * Constructs a ServerThread.
     * 
     * @param socket
     *            socket connected to the Client
     */
    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;

        // Creates an empty queue for the new connected Client
        clientQueue.put(socket, new ConcurrentLinkedQueue<String>());

        this.addr = socket.getInetAddress().getHostAddress() + ":"
                + socket.getPort();
        System.out.println("New connection: " + this.addr);
    }

    /**
     * Run by a thread. Loop that reads data sent by the Client and responds
     * according the message queue.
     */
    @Override
    public void run() {
        // TODO: Separate producer and consumer

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

        // Main Client loop
        try {
            // Read and parse Client message
            String inputLine;
            while (!quitServer && (inputLine = in.readLine()) != null) {
                if (inputLine.trim().equals(""))
                    continue;
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
            System.out.println("Disconnected:   " + addr);

            // Remove all data related to Client
            if (nick != null) {
                hostRooms.remove(nick);
                nicksToSocks.remove(nick);
            }
            clientQueue.remove(socket);
            socket.close();
        } catch (IOException e) {
            // Ignore if socket couldn't be closed
            return;
        }
    }

    /**
     * Parses a given message sent by the Client, putting a response on their
     * message queue.
     * 
     * @param message
     *            message, received from the Client, to be parsed
     */
    private void parse(String message) {
        ClientToServerMessage receivedMsg = ClientToServerMessage
                .fromEncodedString(message);
        String arg = receivedMsg.getArgument();
        ServerToClientMessage sentMsg;
        switch (receivedMsg) {

        // NICK [nickname] : Set Client's nickname as the specified value.
        // If no argument is given, return Client's nickname.
        case NICK:
            // No argument given; return Client's nickname
            if (arg.isEmpty()) {
                if (nick == null) {
                    sentMsg = ServerToClientMessage.ERR_NICKNAME_IS_UNDEFINED;
                } else {
                    sentMsg = ServerToClientMessage.INFO_NICKNAME;
                    sentMsg.setArgument(nick);
                }
                queueMessage(socket, sentMsg);
                return;
            }

            String newNickname = arg;
            // Nickname must contain 3 to 10 alphanumeric characters;
            // the first one must be a letter
            if (!newNickname.matches("^[a-zA-Z]\\w{2,9}$")) {
                sentMsg = ServerToClientMessage.ERR_NICKNAME_IS_INVALID;
                sentMsg.setArgument(newNickname);
                queueMessage(socket, sentMsg);
                return;
            }

            // Nickname already exists
            if (nicksToSocks.containsKey(newNickname)) {
                sentMsg = ServerToClientMessage.ERR_NICKNAME_IS_IN_USE;
                sentMsg.setArgument(newNickname);
                queueMessage(socket, sentMsg);
                return;
            }

            // After changing the nickname, the old one is deleted
            if (nick != null && nicksToSocks.containsKey(nick))
                nicksToSocks.remove(nick);

            // Change user's nickname in the room they are hosting, if any
            if (nick != null && hostRooms.containsKey(nick)) {
                String map = hostRooms.remove(nick);
                hostRooms.put(newNickname, map);
            }

            // Set Client's nickname
            nick = newNickname;
            nicksToSocks.put(nick, socket);
            sentMsg = ServerToClientMessage.SUCC_NICKNAME_CHANGED;
            sentMsg.setArgument(newNickname);
            queueMessage(socket, sentMsg);
            break;

        // MAPS : Return all available maps
        case MAPS:
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
            sentMsg = ServerToClientMessage.INFO_AVAILABLE_MAPS;
            if (maps.length() >= 1)
                sentMsg.setArgument(maps.substring(1)); // remove initial '|'
            queueMessage(socket, sentMsg);
            break;

        // ROOMS : Show all valid rooms in the server
        case ROOMS:
            String roomStr = "";
            for (String r : hostRooms.keySet()) {
                roomStr += "|" + r + ">" + hostRooms.get(r);
            }
            sentMsg = ServerToClientMessage.INFO_AVAILABLE_ROOMS;
            if (roomStr.length() >= 1)
                sentMsg.setArgument(roomStr.substring(1)); // remove initial '|'
            queueMessage(socket, sentMsg);
            break;

        // HOST <map> : Create a room to play on the specified map
        case HOST:
            // User must be registered
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            String mapName = arg;
            // Disallow path controlling chars
            if (mapName.contains("..") || mapName.contains("/"))
                return;

            url = ServerThread.class.getResource("/kindred/data/map/" + mapName
                    + ".txt");
            // Check if map exists
            if (url == null) {
                sentMsg = ServerToClientMessage.ERR_MAP_NOT_FOUND;
                sentMsg.setArgument(mapName);
                queueMessage(socket, sentMsg);
                return;
            }

            // Create a new room!
            hostRooms.put(nick, mapName);
            sentMsg = ServerToClientMessage.SUCC_HOST;
            sentMsg.setArgument(mapName);
            queueMessage(socket, sentMsg);
            break;

        // UNHOST : Remove room if Client is hosting one
        case UNHOST:
            // User must be registered
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            // Remove room if it exists
            if (hostRooms.containsKey(nick)) {
                hostRooms.remove(nick);
                queueMessage(socket, ServerToClientMessage.SUCC_UNHOST);
            } else {
                queueMessage(socket,
                        ServerToClientMessage.ERR_CANNOT_UNHOST_WITHOUT_HOST);
            }
            break;

        // JOIN <nickname> : Connect to a room hosted by the specified user
        case JOIN:
            // Unregistered user cannot join a room
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            String host = arg;

            // Nonexistent room
            if (!hostRooms.containsKey(host)) {
                sentMsg = ServerToClientMessage.ERR_ROOM_NOT_FOUND;
                sentMsg.setArgument(host);
                queueMessage(socket, sentMsg);
                return;
            }

            // Disallow entering in own room
            if (nick.equals(host)) {
                queueMessage(socket, ServerToClientMessage.ERR_CANNOT_ENTER_OWN_ROOM);
                return;
            }

            // Entering another user's room => leaving their own room (if it
            // exists)
            if (hostRooms.containsKey(nick))
                queueMessage(socket, ServerToClientMessage.INFO_LEAVE_HOSTED_ROOM);

            // Let the guest user know that they have successfully entered a
            // room, that they will be the second player, and inform the map
            // name
            sentMsg = ServerToClientMessage.SUCC_JOIN;
            mapName = hostRooms.get(host);
            sentMsg.setArgument(host + "|2|" + mapName);
            queueMessage(socket, sentMsg);

            // Let the host user know that someone has entered their room, that
            // they will be the first player, and inform the map name
            sentMsg = ServerToClientMessage.INFO_SOMEONE_ENTERED_ROOM;
            sentMsg.setArgument(nick + "|1|" + mapName);
            queueMessage(nicksToSocks.get(host), sentMsg);

            // Create the room
            Room room = new Room(host, nick);

            // Remove room from the list of the available rooms
            hostRooms.remove(host);

            // Add this room to the list of games
            currentGames.add(room);
            break;

        // QUIT : Make client leave the server
        case QUIT:
            queueMessage(socket, ServerToClientMessage.SUCC_LEAVE);
            quitServer = true;
            break;

        // GAME_ACTION: Just pass forward the message
        case GAME_ACTION:
            ServerToClientMessage msgToSend = ServerToClientMessage.GAME_ACTION;
            msgToSend.setArgument(arg);
            for (Room r : currentGames)
                if (r.hasNick(nick)) {
                    host = r.getOtherNick(nick);
                    queueMessage(nicksToSocks.get(host), msgToSend);
                    break;
                }
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
     * receive it. Escapes backslashes and newline characters in the message.
     * 
     * @param socket
     *            Client's socket whose message queue will receive the message
     *            to be sent
     * @param msg
     *            message to be queued
     */
    private void queueMessage(Socket socket, ServerToClientMessage msg) {
        String message = msg.toEncodedString();
        message = message.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n");
        clientQueue.get(socket).add(message);
    }

}
