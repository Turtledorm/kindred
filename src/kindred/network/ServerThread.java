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
 * Runs as a thread for each connected client. Reads and sends data to the
 * client.
 * 
 * @author Kindred Team
 */
class ServerThread extends Thread {

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
     * Reads data sent by the client and responds according the message queue.
     */
    @Override
    public void run() {
        // TODO: separate producer and consumer
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
        ClientToServerMessage receivedMsg = ClientToServerMessage
                .fromEncodedString(message);
        String arg = receivedMsg.getArgument();
        ServerToClientMessage sentMsg;
        switch (receivedMsg) {

        // NICK [nickname] : Sets client's nickname as the specified value.
        // If no argument is given, returns client's nickname.
        case NICK:
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
            // Nickname must contain 3 to 10 alphanumeric characters, the first
            // one strictly being a letter
            if (!newNickname.matches("^[a-zA-Z]\\w{2,9}$")) {
                sentMsg = ServerToClientMessage.ERR_NICKNAME_IS_INVALID;
                sentMsg.setArgument(newNickname);
                queueMessage(socket, sentMsg);
                return;
            }

            // Nickname already exists
            if (users.containsKey(newNickname)) {
                sentMsg = ServerToClientMessage.ERR_NICKNAME_IS_IN_USE;
                sentMsg.setArgument(newNickname);
                queueMessage(socket, sentMsg);
                return;
            }

            // After changing the nickname, the old one is deleted
            if (nick != null && users.containsKey(nick))
                users.remove(nick);

            // Changes user's nickname in the room they are hosting, if any
            if (nick != null && rooms.containsKey(nick)) {
                String map = rooms.remove(nick);
                rooms.put(newNickname, map);
            }

            // Sets client's nickname
            nick = newNickname;
            users.put(nick, socket);
            sentMsg = ServerToClientMessage.SUCC_NICKNAME_CHANGED;
            sentMsg.setArgument(newNickname);
            queueMessage(socket, sentMsg);
            break;

        // MAPS : Returns all available maps
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

        // ROOMS : Shows all valid rooms in the server
        case ROOMS:
            String roomStr = "";
            for (String r : rooms.keySet()) {
                roomStr += "|" + r + ">" + rooms.get(r);
            }
            sentMsg = ServerToClientMessage.INFO_AVAILABLE_ROOMS;
            if (roomStr.length() >= 1)
                sentMsg.setArgument(roomStr.substring(1)); // remove initial '|'
            queueMessage(socket, sentMsg);
            break;

        // HOST <map> : Creates a room to play on the specified map
        case HOST:
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }
            String mapName = arg;
            if (mapName.contains("..") || mapName.contains("/"))
                return;
            url = ServerThread.class.getResource("/kindred/data/map/" + mapName
                    + ".txt");
            if (url == null) {
                sentMsg = ServerToClientMessage.ERR_MAP_NOT_FOUND;
                sentMsg.setArgument(mapName);
                queueMessage(socket, sentMsg);
                return;
            }

            // Creates a new room!
            rooms.put(nick, mapName);
            sentMsg = ServerToClientMessage.SUCC_HOST;
            sentMsg.setArgument(mapName);
            queueMessage(socket, sentMsg);
            break;

        // UNHOST : Removes room if client is hosting one
        case UNHOST:
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
        case JOIN:
            // Unregistered user cannot join a room
            if (nick == null) {
                queueMessage(socket, ServerToClientMessage.ERR_UNREGISTERED_USER);
                return;
            }

            String host = arg;

            // Non-existent room
            if (!rooms.containsKey(host)) {
                sentMsg = ServerToClientMessage.ERR_ROOM_NOT_FOUND;
                sentMsg.setArgument(host);
                queueMessage(socket, sentMsg);
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
            // room, that they will be the second player, and inform the map
            // name
            sentMsg = ServerToClientMessage.SUCC_JOIN;
            mapName = rooms.get(host);
            sentMsg.setArgument(host + "|2|" + mapName);
            queueMessage(socket, sentMsg);

            // Let the host user know that someone has entered their room, that
            // they will be the first player, and inform the map name
            sentMsg = ServerToClientMessage.INFO_SOMEONE_ENTERED_ROOM;
            sentMsg.setArgument(nick + "|1|" + mapName);
            queueMessage(users.get(host), sentMsg);

            // Create the room
            Room room = new Room(host, nick);

            // Remove room from the list of the available rooms
            rooms.remove(host);

            // Add this room to the list of games
            currentGames.add(room);
            break;

        // QUIT : Makes client leave the server
        case QUIT:
            queueMessage(socket, ServerToClientMessage.SUCC_LEAVE);
            quitServer = true;
            break;

        // GAME_ACTION: Just pass forwards the message
        case GAME_ACTION:
            ServerToClientMessage msgToSend = ServerToClientMessage.GAME_ACTION;
            msgToSend.setArgument(arg);
            for (Room r : currentGames)
                if (r.hasNick(nick)) {
                    host = r.getOtherNick(nick);
                    queueMessage(users.get(host), msgToSend);
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
