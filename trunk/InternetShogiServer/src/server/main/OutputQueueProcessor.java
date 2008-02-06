package server.main;

import server.services.connection.ClientConnection;
import server.services.connection.ConnectionManager;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;

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
	
	protected void stopSending() {
		keepSending = false;
	}
	
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
