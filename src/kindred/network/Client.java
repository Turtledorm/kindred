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

    public Client(String ipServer, AbstractView view) {
        // Create socket and connect to server
        try {
            socket = new Socket(ipServer, port);
        } catch (IOException e) {
            System.err
                    .println("Couldn't connect to server (IP = " + ipServer + ")!");
            System.exit(1);
        }

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
                    opponent = arg;
                    break;
                case INFO_SOMEONE_ENTERED_ROOM:
                    opponent = arg;
                    break;
                case INFO_FIRST_PLAYER:
                    team = 1;
                    break;
                case INFO_SECOND_PLAYER:
                    team = 2;
                    break;
                case INFO_MAP:
                    String mapFilename = arg;
                    game = new Game(nickname, opponent, "/kindred/data/maps/"
                            + mapFilename + ".txt", team, view);
                    break;
                case SUCC_LEAVE:
                    socket.close();
                    break;
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
            }
        } catch (IOException e) {
            System.out.println("Error when connecting to server!");
            System.exit(1);
        }

        // Unexpected 'null' from socket; server suddenly closed
        if (!socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Connection with server has been lost!");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void nick() {
        send(ClientToServerMessage.NICK);
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
        send(ClientToServerMessage.QUIT);
    }
}
