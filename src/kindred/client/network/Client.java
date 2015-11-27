package kindred.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import kindred.client.model.Game;
import kindred.client.view.AbstractView;
import kindred.client.view.cli.CLI;
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
     * If {@code true}, then the Client's socket will close the connection to
     * the Server.
     */
    private boolean quitClient;

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
     * Constructs a Client.
     * 
     * @param serverIP
     *            the Server's IP address; if it is {@code null}, the user is
     *            prompted to give a Server IP
     * @param view
     *            AbstractView to be used by the Client (a CLI or GUI)
     */
    public Client(String serverIP, AbstractView view) {
        this.serverIP = serverIP;
        this.view = view;
        quitClient = false;

        if (serverIP == null)
            serverIP = view.promptForIP();
        if (serverIP.isEmpty())
            serverIP = "localhost";

        // Create socket and connect to Server
        try {
            socket = new Socket(serverIP, DEFAULT_PORT);
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            System.exit(1);
        }
        view.connectionResult(true, serverIP);

        // Define socket I/O
        try {
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            System.exit(1);
        }

    }

    /**
     * Client's main loop. Prompts user and sends menu or game data to the
     * Server.
     */
    public void mainLoop() {
        while (!quitClient) {
            if (game == null) {
                view.promptForMenuAction(this);
                try {
                    synchronized (view) {
                        view.wait();
                    }
                } catch (InterruptedException e) {
                }
            } else if (!game.isOver()) {
                view.displayMap();
                view.promptForGameAction(this);
            } else
                game = null;
        }
    }

    /**
     * Run by a thread. Receives and manages data from the Server.
     */
    public void run() {
        String response;

        try {
            while ((response = socketIn.readLine()) != null) {
                response = response.replaceAll("\\\\n", "\n").replaceAll("\\\\\\\\",
                        "\\\\");
                ServerToClientMessage msg = ServerToClientMessage
                        .fromEncodedString(response);

                // Analyse message
                String arg = msg.getArgument();
                switch (msg) {
                case INFO_NICKNAME:
                case SUCC_NICKNAME_CHANGED:
                    nickname = arg;
                    break;
                case SUCC_JOIN:
                case INFO_SOMEONE_ENTERED_ROOM:
                    String[] parts = arg.split("\\|");
                    opponent = parts[0];
                    team = Integer.parseInt(parts[1]);
                    String mapFilename = parts[2];
                    game = new Game(nickname, opponent, "/kindred/common/data/map/"
                            + mapFilename + ".txt", team);
                    view.setGame(game);
                    break;
                case SUCC_LEAVE:
                    System.exit(0);
                    socket.close();
                    return;
                case GAME_ACTION:
                    receiveGameAction(GameAction.fromEncodedString(arg));
                    break;
                // Nothing to do here in the following cases
                case SUCC_HOST:
                case SUCC_UNHOST:
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
            }
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            quitClient = true;
            System.exit(1);
        }

        // Unexpected 'null' from socket; kindred.server suddenly closed
        if (!socket.isClosed()) {
            try {
                socket.close();
                view.connectionLost();
                quitClient = true;
                System.exit(1);
            } catch (IOException e) {
                ; // Ignore if socket couldn't be closed
            }
        }
    }

    /**
     * Sends the specified message to the Server.
     * 
     * @param msg
     *            message to be sent to the Server
     */
    public void send(ClientToServerMessage msg) {
        socketOut.println(msg.toEncodedString());
    }

    /**
     * Sends a NICK message with no argument to the Server.
     */
    public void nick() {
        ClientToServerMessage msg = ClientToServerMessage.NICK;
        msg.setArgument("");
        send(msg);
    }

    /**
     * Sends a NICK message with the user's new desired nickname to the Server.
     * 
     * @param nickname
     *            user's desired new nickname to be sent to the Server
     */
    public void nick(String nickname) {
        ClientToServerMessage msg = ClientToServerMessage.NICK;
        msg.setArgument(nickname);
        send(msg);
    }

    /**
     * Sends a MAPS message to the Server.
     */
    public void maps() {
        send(ClientToServerMessage.MAPS);
    }

    /**
     * Sends a ROOMS message to the Server.
     */
    public void rooms() {
        send(ClientToServerMessage.ROOMS);
    }

    /**
     * Sends a HOST message with the user's desired map name to play on to the
     * Server.
     * 
     * @param mapName
     *            user's desired map name to host a room on
     */
    public void host(String mapName) {
        ClientToServerMessage msg = ClientToServerMessage.HOST;
        msg.setArgument(mapName);
        send(msg);
    }

    /**
     * Sends an UNHOST message to the Server.
     */
    public void unhost() {
        send(ClientToServerMessage.UNHOST);
    }

    /**
     * Sends a JOIN message with the desired opponent's nickname to the Server.
     * 
     * @param host
     *            nickname of the player whose run the user wants to join
     */
    public void join(String host) {
        ClientToServerMessage msg = ClientToServerMessage.JOIN;
        msg.setArgument(host);
        send(msg);
    }

    /**
     * Sends a QUIT message to the Server and prepares to close this Client.
     */
    public void quit() {
        quitClient = true;
        send(ClientToServerMessage.QUIT);
    }

    /**
     * Sends a game MOVE command to the Server. Makes the user's Unit on (
     * {@code positions[0]}, {@code positions[1]}) move to Tile
     * {@code positions[2]}, {@code positions[3]}).
     * 
     * @param positions
     *            array containing the first two values as the (x, y) position
     *            of the user's Unit. The third and fourth values are the (x, y)
     *            position of the Tile that the Unit will move to.
     */
    public void move(int[] positions) {
        GameAction cmd = GameAction.MOVE;
        String arg = "";
        for (int i = 0; i < positions.length; i++)
            arg = "|" + positions[i];
        cmd.setArgument(arg.substring(1));

        ClientToServerMessage msg = ClientToServerMessage.GAME_ACTION;
        msg.setArgument(cmd.toEncodedString());
        send(msg);
    }

    /**
     * Sends a game ATTACK command to the Server. Makes the opponent's Unit on
     * (x, y) suffer the specified amount of damage.
     * 
     * @param x
     *            x coordinate of the opponent's Unit
     * @param y
     *            y coordinate of the opponent's Unit
     * @param damage
     *            damage caused to the opponent's Unit
     */
    public void attack(int x, int y, int damage) {
        GameAction cmd = GameAction.ATTACK;
        String arg = x + "|" + y + "|" + damage;
        cmd.setArgument(arg.substring(1));

        ClientToServerMessage msg = ClientToServerMessage.GAME_ACTION;
        msg.setArgument(cmd.toEncodedString());
        send(msg);
    }

    /**
     * Sends a game END command to the Server, indicating that the user has
     * ended their current turn.
     */
    public void endTurn() {
        GameAction cmd = GameAction.END_TURN;
        ClientToServerMessage msg = ClientToServerMessage.GAME_ACTION;
        msg.setArgument(cmd.toEncodedString());
        send(msg);
    }

    /**
     * Sends a game SURRENDER command to the Server, indicating that the user
     * has forfeited the match.
     */
    public void surrender() {
        GameAction cmd = GameAction.SURRENDER;
        ClientToServerMessage msg = ClientToServerMessage.GAME_ACTION;
        msg.setArgument(cmd.toEncodedString());
        send(msg);
    }

    /**
     * Parses a GameAction message sent by the opponent, resulting in an action
     * in the user's Game.
     * 
     * @param message
     *            GameAction message sent by the opponent
     */
    private void receiveGameAction(GameAction message) {
        String[] partsString = message.getArgument().split("\\|");
        Integer[] parts = new Integer[partsString.length];
        for (int i = 0; i < parts.length; i++)
            parts[i] = Integer.parseInt(partsString[i]);

        switch (message) {
        // MOVE: xi yi xf yf
        case MOVE:
            game.move(parts[0], parts[1], parts[2], parts[3]);
            break;

        // ATTACK: x y damage
        case ATTACK:
            game.causeDamage(parts[0], parts[1], parts[2]);
            break;

        // END_TURN
        case END_TURN:
            game.endTurn();
            break;

        // SURRENDER
        case SURRENDER:
            game.surrender();
            break;
        }
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
        Thread thread = new Thread(client);
        thread.start();
        client.mainLoop();
    }
}
