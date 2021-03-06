package kindred.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;

import kindred.client.model.Game;
import kindred.client.view.AbstractView;
import kindred.client.view.cli.CLI;
import kindred.common.ClientToServerEnum;
import kindred.common.ClientToServerMessage;
import kindred.common.ServerToClientMessage;

/**
 * Implements the main loop of a TCP socket Client. Connects to the Server and
 * sends user's data.
 * 
 * @author Kindred Team
 */
public class Client implements Runnable {

    /**
     * Default port used by the kindred.server.
     */
    public static final int DEFAULT_PORT = 60001;

    /**
     * Interval between each sent message in milliseconds.
     */
    private static final int SEND_INTERVAL = 200;

    /**
     * Client's TCP socket, used for connecting to the Server.
     */
    private Socket socket;

    /**
     * IP of the Server that the Client will connect to.
     */
    private String serverIP;

    /**
     * Socket output, used for writing to the Server.
     */
    private PrintWriter socketOut;

    /**
     * Socket input, used for reading from the Server.
     */
    private BufferedReader socketIn;

    /**
     * Client's interaction with the program (CLI or GUI).
     */
    private AbstractView view;

    /**
     * Client's nickname on the Server.
     */
    private String nickname;

    /**
     * Nickname of the user's opponent during a game. Equals {@code null} if not
     * yet defined.
     */
    private String opponent;

    /**
     * Instance of a Game being played by the Client.
     */
    private Game game;

    /**
     * User's identifier number for all Units on his team.
     */
    private int team;

    /**
     * Special queue-related class that controls messages sent to the Server.
     */
    private ClientMessageSender messageSender;

    /**
     * If {@code false}, then this Client's socket is not connected to the
     * server.
     */
    private boolean connected;

    /**
     * If {@code true}, then this client is hosting a room.
     */
    public boolean isHostingRoom;

    /**
     * Constructs a Client.
     * 
     * @param serverIP
     *            the Server's IP address; if it is {@code null}, the user is
     *            prompted to give a Server IP
     * @param view
     *            AbstractView to be used by the Client (a CLI or GUI)
     */
    public Client(String serverIP, AbstractView view) {
        connected = false;
        isHostingRoom = false;
        this.serverIP = serverIP;
        this.view = view;

        if (serverIP == null)
            serverIP = view.promptForIP();
        if (serverIP.isEmpty())
            serverIP = "localhost";

        // Create socket and connect to Server
        try {
            socket = new Socket(serverIP, DEFAULT_PORT);
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            throw new RuntimeException();
        }
        view.connectionResult(true, serverIP);

        // Define socket I/O
        try {
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            throw new RuntimeException();
        }
        connected = true;
        messageSender = new ClientMessageSender(socketOut);

        Timer timer = new Timer();
        timer.schedule(messageSender, SEND_INTERVAL, SEND_INTERVAL);
    }

    /**
     * Client's main loop. Prompts user and sends menu or game data to the
     * Server.
     */
    public void mainLoop() {
        while (isConnected()) {
            if (game == null) {
                view.promptForMenuAction(this);
                try {
                    synchronized (view) {
                        view.wait();
                    }
                } catch (InterruptedException e) {
                }
            } else {
                view.displayMap();
                if (game.getTurn() == team)
                    view.promptForGameAction(this);
                else {
                    try {
                        synchronized (view) {
                            view.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /**
     * Run by a thread. Receives and manages data from the Server.
     */
    public void run() {
        String response;

        try {
            while ((response = socketIn.readLine()) != null) {
                ServerToClientMessage msg = ServerToClientMessage
                        .fromEncodedString(response);
                // Analyse message
                String arg = msg.argument;
                switch (msg.msg) {
                case INFO_NICKNAME:
                case SUCC_NICKNAME_CHANGED:
                    nickname = arg;
                    break;
                case SUCC_JOIN:
                case INFO_SOMEONE_ENTERED_ROOM:
                    isHostingRoom = false;
                    String[] parts = arg.split("\\|");
                    opponent = parts[0];
                    team = Integer.parseInt(parts[1]);
                    String mapFilename = parts[2];
                    game = new Game(nickname, opponent,
                            "/kindred/common/data/map/" + mapFilename + ".txt",
                            team);
                    view.setGame(game);
                    send(new ClientToServerMessage(ClientToServerEnum.EMPTY));
                    break;
                case SUCC_LEAVE:
                    disconnect();
                    return;
                case GAME_ACTION:
                    receiveGameAction(GameActionEnum.fromEncodedString(arg));
                    break;
                case SUCC_HOST:
                    isHostingRoom = true;
                    break;
                case SUCC_UNHOST:
                    isHostingRoom = false;
                    break;
                // Nothing to do here in the following cases
                case INFO_AVAILABLE_MAPS:
                case INFO_AVAILABLE_ROOMS:
                case INFO_LEAVE_HOSTED_ROOM:
                case ERR_CANNOT_ENTER_OWN_ROOM:
                case ERR_CANNOT_UNHOST_WITHOUT_HOST:
                case ERR_INVALID_COMMAND_OR_ARGUMENTS:
                case ERR_MAP_NOT_FOUND:
                case ERR_NICKNAME_IS_INVALID:
                case ERR_NICKNAME_IS_IN_USE:
                case ERR_NICKNAME_IS_UNDEFINED:
                case ERR_ROOM_NOT_FOUND:
                case ERR_UNREGISTERED_USER:
                    break;
                }

                view.remoteEvent(msg);
                synchronized (view) {
                    view.notify();
                }

                if (game != null && game.isOver())
                    game = null;
            }
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            connected = false;
        }

        // Unexpected 'null' from socket; kindred.server suddenly closed
        if (!socket.isClosed()) {
            disconnect();
            view.connectionLost();
        }
    }

    /**
     * Sends the specified message to the Server.
     * 
     * @param msg
     *            message to be sent to the Server
     */
    public void send(ClientToServerMessage msg) {
        messageSender.enqueueMessage(msg);
    }

    /**
     * Sends a NICK message with no argument to the Server.
     */
    public void nick() {
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.NICK);
        send(msg);
    }

    /**
     * Sends a NICK message with the user's new desired nickname to the Server.
     * 
     * @param nickname
     *            user's desired new nickname to be sent to the Server
     */
    public void nick(String nickname) {
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.NICK, nickname);
        send(msg);
    }

    /**
     * Sends a MAPS message to the Server.
     */
    public void maps() {
        send(new ClientToServerMessage(ClientToServerEnum.MAPS));
    }

    /**
     * Sends a ROOMS message to the Server.
     */
    public void rooms() {
        send(new ClientToServerMessage(ClientToServerEnum.ROOMS));
    }

    /**
     * Sends a HOST message with the user's desired map name to play on to the
     * Server.
     * 
     * @param mapName
     *            user's desired map name to host a room on
     */
    public void host(String mapName) {
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.HOST, mapName);
        send(msg);
    }

    /**
     * Sends an UNHOST message to the Server.
     */
    public void unhost() {
        send(new ClientToServerMessage(ClientToServerEnum.UNHOST));
    }

    /**
     * Sends a JOIN message with the desired opponent's nickname to the Server.
     * 
     * @param host
     *            nickname of the player whose run the user wants to join
     */
    public void join(String host) {
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.JOIN, host);
        send(msg);
    }

    /**
     * Sends a QUIT message to the Server and prepares to close this Client.
     */
    public void quit() {
        send(new ClientToServerMessage(ClientToServerEnum.QUIT));
    }

    /**
     * Tries to move the Unit on {@code positions[0]}, {@code positions[1]}) to
     * Tile {@code positions[2]}, {@code positions[3]}). If successful, sends a
     * game MOVE command to the Server and returns {@code true}. Otherwise, only
     * returns {@code false}.
     * 
     * @param positions
     *            array containing the first two values as the (x, y) position
     *            of a Unit. The third and fourth values are the (x, y) position
     *            of the Tile that the Unit will move to
     * 
     * @return {@code true}, if the movement was successful, or {@code false}
     *         otherwise
     */
    public boolean move(int[] positions) {
        if (!game.move(positions[0], positions[1], positions[2], positions[3]))
            return false;

        GameActionEnum cmd = GameActionEnum.MOVE;
        String arg = "";
        for (int i = 0; i < positions.length; i++)
            arg += "|" + positions[i];
        cmd.setArgument(arg.substring(1));

        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.GAME_ACTION, cmd.toEncodedString());
        send(msg);

        return true;
    }

    /**
     * Tries to attack the Unit on {@code positions[2]}, {@code positions[3]})
     * with the Unit on Tile {@code positions[0]}, {@code positions[1]}). If
     * successful, sends a game ATTACK command to the Server and returns
     * {@code true}. Otherwise, only returns {@code false}.
     * 
     * @param positions
     *            array containing the first two values as the (x, y) position
     *            of the attacking Unit. The third and fourth values are the (x,
     *            y) position of the defending Unit
     * 
     * @return {@code true}, if the attack was successful, or {@code false}
     *         otherwise
     */
    public boolean attack(int[] positions) {
        int damage = game.attack(positions[0], positions[1], positions[2],
                positions[3]);

        if (damage < 0)
            return false;

        // Attack missed if damage = 0, and hit if damage > 0
        game.causeDamage(positions[2], positions[3], damage);

        GameActionEnum cmd = GameActionEnum.ATTACK;
        String arg = "";
        for (int i = 0; i < positions.length; i++)
            arg += "|" + positions[i];
        arg += "|" + damage;
        cmd.setArgument(arg.substring(1));

        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.GAME_ACTION, cmd.toEncodedString());

        if (game.isOver())
            game = null;

        send(msg);

        return true;
    }

    /**
     * Sends a game END command to the Server, indicating that the user has
     * ended their current turn.
     */
    public void endTurn() {
        GameActionEnum cmd = GameActionEnum.END_TURN;
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.GAME_ACTION, cmd.toEncodedString());
        game.endTurn();
        send(msg);
    }

    /**
     * Sends a game SURRENDER command to the Server, indicating that the user
     * has forfeited the match.
     */
    public void surrender() {
        GameActionEnum cmd = GameActionEnum.SURRENDER;
        ClientToServerMessage msg = new ClientToServerMessage(
                ClientToServerEnum.GAME_ACTION, cmd.toEncodedString());
        game.surrender();
        game = null;
        send(msg);
    }

    /**
     * Parses a GameActionEnum message sent by the opponent, resulting in an
     * action in the user's Game.
     * 
     * @param message
     *            GameActionEnum message sent by the opponent
     */
    private void receiveGameAction(GameActionEnum message) {
        String[] partsString = message.getArgument().split("\\|");
        Integer[] parts = new Integer[partsString.length];
        if (message == GameActionEnum.ATTACK || message == GameActionEnum.MOVE)
            for (int i = 0; i < parts.length; i++)
                parts[i] = Integer.parseInt(partsString[i]);
        switch (message) {
        // MOVE: xi yi xf yf
        case MOVE:
            game.forceMovement(parts[0], parts[1], parts[2], parts[3]);
            break;

        // ATTACK: x y damage
        case ATTACK:
            game.causeDamage(parts[2], parts[3], parts[4]);
            if (game.isOver())
                game = null;
            break;

        // END_TURN
        case END_TURN:
            game.endTurn();
            break;

        // SURRENDER
        case SURRENDER:
            game.surrender();
            game = null;
            break;
        }

        if (message == GameActionEnum.END_TURN
                || message == GameActionEnum.SURRENDER) {
            synchronized (view) {
                view.notify();
            }
        }
    }

    /**
     * Forces disconnection from the server.
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    /**
     * Checks if the client is connected to the server.
     * 
     * @return {@code true} if connected, {@code false} otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Starts the client thread that receives messages from the server.
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Returns the nickname of this player.
     * 
     * @return this player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Checks if this client is hosting a room.
     * 
     * @return {@code true} if this client is hosting a room, or {@code false}
     *         otherwise
     */
    public boolean isHostingRoom() {
        return isHostingRoom;
    }

    /**
     * Checks if this client is participating in a game.
     * 
     * @return {@code true} if this client is in a game room, or {@code false}
     *         otherwise
     */
    public boolean isPlaying() {
        return game != null;
    }

    /**
     * If this client is participating in a game, returns the identifier of the
     * current player's turn. Otherwise, returns -1.
     * 
     * @return the ID of the current player's turn, if this client is
     *         participating in a game, or -1 otherwise
     */
    public int getGameTurn() {
        return game == null ? -1 : game.getTurn();
    }

    /**
     * Starts a Client. Connects to the Server with IP equal to {@code args[0]},
     * if given; otherwise, later prompts the Client for the Server's IP.
     * 
     * @param args
     *            if {@code args[0]} exists, then it is used as the Server's IP
     */
    public static void main(String[] args) {
        String IP = args.length < 1 ? null : args[0];
        AbstractView view = new CLI();
        Client client = new Client(IP, view);
        client.start();
        client.mainLoop();
    }
}
