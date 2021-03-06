package kindred.client.network;

import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import kindred.common.ClientToServerEnum;
import kindred.common.ClientToServerMessage;

/**
 * Works parallel to the client. Contains a queue for controlling messages to be
 * sent to the server.
 * 
 * @author Kindred Team
 */
public class ClientMessageSender extends TimerTask {

    /**
     * Socket output, used for writing to the Server. Same as the one used by
     * the Client.
     */
    private PrintWriter socketOut;

    /**
     * Queue containing messages to be sent to the Server.
     */
    private ConcurrentLinkedQueue<ClientToServerMessage> queue;

    /**
     * Constructs a ClientMessageSender.
     * 
     * @param socketOut
     *            output, used for writing to the Server
     */
    public ClientMessageSender(PrintWriter socketOut) {
        this.socketOut = socketOut;
        queue = new ConcurrentLinkedQueue<ClientToServerMessage>();
    }

    /**
     * Puts the specified message in the Client's queue.
     * 
     * @param msg
     *            message to be put in the Client's queue
     */
    public void enqueueMessage(ClientToServerMessage msg) {
        queue.add(msg);
    }

    @Override
    public void run() {
        ClientToServerMessage msg;
        try {
            msg = queue.remove();
            socketOut.println(msg.toEncodedString());
        } catch (NoSuchElementException e) {
            socketOut.println(new ClientToServerMessage(ClientToServerEnum.EMPTY)
                    .toEncodedString());
        }

    }

}
