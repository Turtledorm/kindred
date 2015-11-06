package kindred.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server successfully opened on port " + port);
        while (true) {
            new ServerThread(serverSocket.accept()).start();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
