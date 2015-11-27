package kindred.client.network;

import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import kindred.common.ClientToServerMessage;

/**
 * @author Kindred Team
 * 
 */
public class ClientQueue extends TimerTask {

    private PrintWriter socketOut;

    public ClientQueue(PrintWriter socketOut) {
        this.socketOut = socketOut;
        queue = new ConcurrentLinkedQueue<ClientToServerMessage>();
    }

    ConcurrentLinkedQueue<ClientToServerMessage> queue;

    public void enqueueMessage(ClientToServerMessage message) {
        queue.add(message);
    }

    @Override
    public void run() {
        ClientToServerMessage msg;
        try {
            msg = queue.remove();

        } catch (NoSuchElementException e) {
            msg = ClientToServerMessage.EMPTY;
        }
        socketOut.println(msg.toEncodedString());
    }

}
