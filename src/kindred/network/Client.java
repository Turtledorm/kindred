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

public class Client implements Runnable {

    // Socket and port to connect to server
    private Socket socket;
    private final int port = 8000;

    // Client's interaction with the program
    private AbstractView view;

    // I/O for server socket
    private PrintWriter serverOut;
    private BufferedReader serverIn;

    private String nickname, opponent;
    private Game game; // TODO: Create game if CONNECT worked
    private int team;
    private boolean quit;

    public Client(String serverIP, AbstractView view) {
        quit = false;
        this.view = view;

        if (serverIP == null)
            serverIP = view.promptForIP();
        if (serverIP.isEmpty())
            serverIP = "localhost";

        // Create socket and connect to server
        try {
            socket = new Socket(serverIP, port);
        } catch (IOException e) {
            view.connectionResult(false, serverIP);
            System.exit(1);
        }
        view.connectionResult(true, serverIP);

        // Define socket I/O
        try {
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error when defining I/O with server!");
            System.exit(1);
        }

    }

    public void send(ClientToServerMessage msg) {
        serverOut.println(msg.toEncodedString());
    }

    public void mainLoop() {
        while (!quit) {
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

    public void run() {
        String response;
        try {
            while ((response = serverIn.readLine()) != null) {
                response = response.replaceAll("\\\\n", "\n").replaceAll("\\\\\\\\",
                        "\\\\");
                ServerToClientMessage msg = ServerToClientMessage
                        .fromEncodedString(response);
                // Analyse message
                String arg = msg.getArgument();
                switch (msg) {
                case INFO_NICKNAME:
                    nickname = arg;
                    break;
                case SUCC_JOIN:
                case INFO_SOMEONE_ENTERED_ROOM:
                    String[] parts = arg.split("|");
                    opponent = parts[0];
                    team = Integer.parseInt(parts[1]);
                    String mapFilename = parts[2];
                    game = new Game(nickname, opponent, "/kindred/data/maps/"
                            + mapFilename + ".txt", team, view);
                    break;
                case SUCC_LEAVE:
                    System.exit(0);
                    socket.close();
                    return;
                case SUCC_NICKNAME_CHANGED:
                    nickname = arg;
                    break;
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
            System.out.println("Error when connecting to server!");
            quit = true;
            System.exit(1);
        }

        // Unexpected 'null' from socket; server suddenly closed
        if (!socket.isClosed()) {
            try {
                socket.close();
                System.out.println(".......!");
                System.out.println("Connection with server has been lost!");
                quit = true;
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void nick() {
        ClientToServerMessage msg = ClientToServerMessage.NICK;
        msg.setArgument("");
        send(msg);
    }

    public void nick(String nickname) {
        ClientToServerMessage msg = ClientToServerMessage.NICK;
        msg.setArgument(nickname);
        send(msg);
    }

    public void join(String host) {
        ClientToServerMessage msg = ClientToServerMessage.JOIN;
        msg.setArgument(host);
        send(msg);
    }

    public void host(String mapName) {
        ClientToServerMessage msg = ClientToServerMessage.HOST;
        msg.setArgument(mapName);
        send(msg);
    }

    public void unhost() {
        send(ClientToServerMessage.UNHOST);
    }

    public void maps() {
        send(ClientToServerMessage.MAPS);
    }

    public void rooms() {
        send(ClientToServerMessage.ROOMS);
    }

    public void quit() {
        quit = true;
        send(ClientToServerMessage.QUIT);
    }

    public static void main(String[] args) {
        String IP = args.length < 1 ? null : args[0];
        AbstractView view = new CLI();
        Client client = new Client(IP, view);
        Thread thread = new Thread(client);
        thread.start();
        client.mainLoop();

    }
}
