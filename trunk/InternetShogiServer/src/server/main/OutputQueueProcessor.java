package server.main;

import server.services.connection.ClientConnection;
import server.services.connection.ConnectionManager;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;

/**
 * A thread that constantly monitors the GlobalOutputMessageQueue for
 * new outgoing messages, and passes them to the appropriate ClientConnection
 * for transfer over the network.
 * 
 * @author Adrian Petrescu
 *
 */
public class OutputQueueProcessor implements Runnable {

	private GlobalOutputMessageQueue outputQueue;
	private ConnectionManager connectionManager;
	private boolean keepSending;
	
	protected OutputQueueProcessor(ConnectionManager connectionManager,
			GlobalOutputMessageQueue outputQueue) {
		this.connectionManager = connectionManager;
		this.outputQueue = outputQueue;
		keepSending = true;
	}
	
	/**
	 * Brings down the OutputQueueProcessor. No more messages will be sent,
	 * and the GlobalOutputMessageQueue will only grow, not shrink. Once
	 * this is called, the thread will die and will have to be recreated
	 * before it can start working again.
	 */
	protected void stopSending() {
		keepSending = false;
	}
	
	/**
	 * Begin processing the OutputQueue. Note that this thread does not
	 * block on I/O; it is not actually responsible for doing the network send.
	 * Once it has delegated the output to the appropriate ClientConnection,
	 * it returns and goes to the next one and lets the ClientConnection waste
	 * time doing the network transfer.
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		while (keepSending) {
			if (outputQueue.isEmpty()) {
				Thread.currentThread().suspend();
			} else {
				OutputMessageQueue output = outputQueue.dequeue();
				while (!output.isEmpty()) {
					boolean messageSent;
					ProtocolMessage outputMessage = output.dequeue();
					ClientConnection conn = connectionManager.getUserConnection(outputMessage.getUser());
					messageSent = conn.sendMessage(outputMessage);
					if (messageSent) {
						//TODO: Do some error-checking here.
					}
				}
			}
		}
	}
}
