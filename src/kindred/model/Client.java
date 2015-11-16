package kindred.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    public void send(String message) {
        if (game != null) {
        }
        serverOut.println(message);
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
                // TODO: properly handle every item below
                // TODO: move help to client
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
                case INFO_DISCONNECTED:
                    socket.close();
                    break;
                case ERR_CANNOT_ENTER_OWN_ROOM:
                    break;
                case ERR_CANNOT_UNHOST_WITHOUT_HOST:
                    break;
                case ERR_INVALID_COMMAND_OR_ARGUMENTS:
                    break;
                case ERR_MAP_NOT_FOUND:
                    break;
                case ERR_NICKNAME_IS_INVALID:
                    break;
                case ERR_NICKNAME_IS_IN_USE:
                    break;
                case ERR_NICKNAME_IS_UNDEFINED:
                    break;
                case ERR_ROOM_NOT_FOUND:
                    break;
                case ERR_UNREGISTERED_USER:
                    break;
                case INFO_AVAILABLE_MAPS:
                    break;
                case INFO_AVAILABLE_ROOMS:
                    break;
                case INFO_LEAVE_HOSTED_ROOM:
                    break;
                case SUCC_HOST:
                    break;
                case SUCC_LEAVE:
                    break;
                case SUCC_NICKNAME_CHANGED:
                    break;
                case SUCC_UNHOST:
                    break;
                default:
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
}
