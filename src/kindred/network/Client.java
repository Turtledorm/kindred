package kindred.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import kindred.model.Game;
import kindred.network.messages.ClientToServerMessage;
import kindred.network.messages.ServerToClientMessage;
import kindred.view.AbstractView;
import kindred.view.cli.CLI;

/**
 * Implements the main loop of a TCP socket Client. Connects to the Server and
 * sends user's data.
 * 
 * @author Kindred Team
 */
public class Client implements Runnable {

    /**
     * Default port used by the server.
     */
    public static final int DEFAULT_PORT = 8000;

    /**
     * Client's TCP socket, used for connecting to the Server.
     */
    private Socket socket;

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
            socketIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
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
            } else if (!game.isOver())
                view.promptForGameAction();
            else
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
                    String[] parts = arg.split("|");
                    opponent = parts[0];
                    team = Integer.parseInt(parts[1]);
                    String mapFilename = parts[2];
                    game = new Game(nickname, opponent,
                            "/kindred/data/maps/" + mapFilename + ".txt", team,
                            view);
                    break;
                case SUCC_LEAVE:
                    System.exit(0);
                    socket.close();
                    return;
                case GAME_ACTION:
                    // TODO: Handle this
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

                view.menuEvent(msg);
                synchronized (view) {
                    view.notify();
                }
            }
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            quitClient = true;
            System.exit(1);
        }

        // Unexpected 'null' from socket; server suddenly closed
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
