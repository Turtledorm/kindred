package kindred.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kindred.common.ClientToServerMessage;
import kindred.common.ServerToClientEnum;
import kindred.common.ServerToClientMessage;

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
                // Send all messages in the queue
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
        String arg = receivedMsg.argument;
        ServerToClientMessage sentMsg;
        switch (receivedMsg.msg) {

        // NICK [nickname] : Set Client's nickname as the specified value.
        // If no argument is given, return Client's nickname.
        case NICK:
            // No argument given; return Client's nickname
            if (arg.isEmpty()) {
                if (nick == null) {
                    sentMsg = new ServerToClientMessage(
                            ServerToClientEnum.ERR_NICKNAME_IS_UNDEFINED);
                } else {
                    sentMsg = new ServerToClientMessage(
                            ServerToClientEnum.INFO_NICKNAME, nick);
                }
                queueMessage(socket, sentMsg);
                return;
            }

            String newNickname = arg;
            // Nickname must contain 3 to 10 alphanumeric characters;
            // the first one must be a letter
            if (!newNickname.matches("^[a-zA-Z]\\w{2,9}$")) {
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.ERR_NICKNAME_IS_INVALID, newNickname);
                queueMessage(socket, sentMsg);
                return;
            }

            // Nickname already exists
            if (nicksToSocks.containsKey(newNickname)) {
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.ERR_NICKNAME_IS_IN_USE, newNickname);
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
            sentMsg = new ServerToClientMessage(
                    ServerToClientEnum.SUCC_NICKNAME_CHANGED, newNickname);
            queueMessage(socket, sentMsg);
            break;

        // MAPS : Return all available maps
        case MAPS:
            InputStream is = ServerThread.class
                    .getResourceAsStream("/kindred/common/data/map/list.info");
            Scanner s = new java.util.Scanner(is).useDelimiter("\\Z");
            String maps = s.next();
            try {
                s.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (maps.length() >= 1)
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.INFO_AVAILABLE_MAPS, maps);
            else
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.INFO_AVAILABLE_MAPS);
            queueMessage(socket, sentMsg);
            break;

        // ROOMS : Show all valid rooms in the kindred.server
        case ROOMS:
            String roomStr = "";
            for (String r : hostRooms.keySet()) {
                roomStr += "|" + r + ">" + hostRooms.get(r);
            }
            if (roomStr.length() >= 1)
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.INFO_AVAILABLE_ROOMS,
                        roomStr.substring(1)); // remove initial '|'
            else
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.INFO_AVAILABLE_ROOMS);
            queueMessage(socket, sentMsg);
            break;

        // HOST <map> : Create a room to play on the specified map
        case HOST:
            // User must be registered
            if (nick == null) {
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.ERR_UNREGISTERED_USER));
                return;
            }

            String mapName = arg;
            // Disallow path controlling chars
            if (mapName.contains("..") || mapName.contains("/"))
                return;

            URL url = ServerThread.class.getResource("/kindred/common/data/map/"
                    + mapName + ".txt");
            // Check if map exists
            if (url == null) {
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.ERR_MAP_NOT_FOUND, mapName);
                queueMessage(socket, sentMsg);
                return;
            }

            // Create a new room!
            hostRooms.put(nick, mapName);
            sentMsg = new ServerToClientMessage(ServerToClientEnum.SUCC_HOST,
                    mapName);
            queueMessage(socket, sentMsg);
            break;

        // UNHOST : Remove room if Client is hosting one
        case UNHOST:
            // User must be registered
            if (nick == null) {
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.ERR_UNREGISTERED_USER));
                return;
            }

            // Remove room if it exists
            if (hostRooms.containsKey(nick)) {
                hostRooms.remove(nick);
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.SUCC_UNHOST));
            } else {
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.ERR_CANNOT_UNHOST_WITHOUT_HOST));
            }
            break;

        // JOIN <nickname> : Connect to a room hosted by the specified user
        case JOIN:
            // Unregistered user cannot join a room
            if (nick == null) {
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.ERR_UNREGISTERED_USER));
                return;
            }

            String host = arg;

            // Nonexistent room
            if (!hostRooms.containsKey(host)) {
                sentMsg = new ServerToClientMessage(
                        ServerToClientEnum.ERR_ROOM_NOT_FOUND, host);
                queueMessage(socket, sentMsg);
                return;
            }

            // Disallow entering in own room
            if (nick.equals(host)) {
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.ERR_CANNOT_ENTER_OWN_ROOM));
                return;
            }

            // Entering another user's room => leaving their own room (if it
            // exists)
            if (hostRooms.containsKey(nick))
                queueMessage(socket, new ServerToClientMessage(
                        ServerToClientEnum.INFO_LEAVE_HOSTED_ROOM));

            // Let the guest user know that they have successfully entered a
            // room, that they will be the second player, and inform the map
            // name
            mapName = hostRooms.get(host);
            sentMsg = new ServerToClientMessage(ServerToClientEnum.SUCC_JOIN, host
                    + "|2|" + mapName);
            queueMessage(socket, sentMsg);

            // Let the host user know that someone has entered their room, that
            // they will be the first player, and inform the map name
            sentMsg = new ServerToClientMessage(
                    ServerToClientEnum.INFO_SOMEONE_ENTERED_ROOM, nick + "|1|"
                            + mapName);
            queueMessage(nicksToSocks.get(host), sentMsg);

            // Create the room
            Room room = new Room(host, nick);

            // Remove room from the list of the available rooms
            hostRooms.remove(host);

            // Add this room to the list of games
            currentGames.add(room);
            break;

        // QUIT : Make kindred.client leave the kindred.server
        case QUIT:
            queueMessage(socket, new ServerToClientMessage(
                    ServerToClientEnum.SUCC_LEAVE));
            // quitServer = true;
            break;

        // GAME_ACTION: Just pass forward the message
        case GAME_ACTION:
            sentMsg = new ServerToClientMessage(ServerToClientEnum.GAME_ACTION, arg);
            for (Room r : currentGames)
                if (r.hasNick(nick)) {
                    String opponent = r.getOtherNick(nick);
                    queueMessage(nicksToSocks.get(opponent), sentMsg);
                    break;
                }
            break;

        // EMPTY: Do nothing
        case EMPTY:
            break;

        // Something not understood
        default:
            queueMessage(socket, new ServerToClientMessage(
                    ServerToClientEnum.ERR_INVALID_COMMAND_OR_ARGUMENTS));
            break;
        }
    }

    /**
     * Inserts a message in the queue of the corresponding socket which will
     * receive it.
     * 
     * @param socket
     *            Client's socket whose message queue will receive the message
     *            to be sent
     * @param msg
     *            message to be queued
     */
    private void queueMessage(Socket socket, ServerToClientMessage msg) {
        String message = msg.toEncodedString();
        clientQueue.get(socket).add(message);
    }

}
