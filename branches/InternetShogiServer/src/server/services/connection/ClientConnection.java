package server.services.connection;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import server.protocol.ProtocolMessage;
import server.protocol.InputMessageQueue;
import server.protocol.OutputMessageQueue;
import server.services.user.User;

/**
 * This class represents a unique network connection to a client, associated
 * with a particular User. In general, there will be exactly one instance of
 * <code>ClientConnection</code> for every user connected to the server at any
 * given moment.
 * 
 * @author Adrian Petrescu
 *
 */
public class ClientConnection {

	/**
	 * This thread listens constantly on the given port for input from the
	 * client. Once it recieves a full line, the <code>ClientListener</code>
	 * notifies its parent <code>ClientConnection</code>.
	 * 
	 * @author Adrian Petrescu
	 *
	 */
	class InputListener implements Runnable {
		private BufferedReader in;
		private boolean connected;
		private ClientConnection clientConnection;

		private InputListener(ClientConnection clientConnection,
				BufferedReader in) {
			this.clientConnection = clientConnection;
			this.in = in;
			this.connected = true;
		}

		public void connect() {
			connected = true;
		}

		public void disconnect() {
			connected = false;
		}

		public void run() {
			while (connected) {
				try {
					String line = in.readLine();
					if (line == null) {
						connected = false;
					} else {
						clientConnection.messageRecieved(line);
					}
				} catch (IOException e) {
					// An error occured, we'll let the server decide whether we
					// should keep trying.
					connected = clientConnection.connectionErrorOccured(e);
				}
			}
		}
	}

	/**
	 * This thread is instantiated every time the server needs to send data
	 * to a client. It blocks so that the server doesn't have to.
	 * 
	 * @author Adrian Petrescu
	 *
	 */
	class OutputListener implements Runnable {
		private PrintWriter out;
		private ClientConnection clientConnection;
		private OutputMessageQueue messages;

		private OutputListener(ClientConnection clientConnection,
				PrintWriter out, OutputMessageQueue messages) {
			this.clientConnection = clientConnection;
			this.out = out;
			this.messages = messages;
		}

		public void run() {
			clientConnection.currentlyWriting = true;
			while (!messages.isEmpty()) {
				String line = messages.dequeue().getMessage();
				out.println(line);
			}
			clientConnection.messageSent();
		}
	}

	private boolean currentlyWriting;
	private InputListener inputListener;
	private PrintWriter out;
	private User user;
	private Socket socket;
	private boolean keepConnected;

	/**
	 * Constructs a new ClientConnection over the given socket, associated with
	 * the given user.
	 * 
	 * @param user The User who has logged in over this connection.
	 * @param socket The socket over which the connection was established.
	 * @throws IOException Thrown in case the ClientConnection was unable to
	 * re-open the input or output streams from the socket.
	 */
	public ClientConnection(User user, BufferedReader in, PrintWriter out, Socket socket) throws IOException {
		inputListener = new InputListener(this, in);
		this.out = out;
		currentlyWriting = false;
		this.user = user;
		this.socket = socket;
		keepConnected = true;
		new Thread(inputListener).start();
	}

	/**
	 * A <code>ClientConnection</code>'s associated InputListener calls
	 * this method whenever it has received a full line of input from
	 * the client.
	 * The ClientConnection wraps it up in an InputMessageQueue and adds it
	 * to the global message queue to be processed by the server.
	 * 
	 * @param message The message sent by the client. 
	 */
	private synchronized void messageRecieved(String message) {
		ProtocolMessage pMessage = new ProtocolMessage(message);
		InputMessageQueue messageQueue = new InputMessageQueue(pMessage);
		GlobalInputMessageQueue.getGlobalInputMessageQueue().enqueue(
				messageQueue);
	}

	/**
	 * Called by a ClientConnection's associated OutputListener to indicate
	 * that it has completed sending the data to the client, and the output
	 * channel is ready for a new message to be written.
	 */
	private void messageSent() {
		currentlyWriting = false;
	}

	/**
	 * Sends an OutputMessageQueue to the client that this ClientConnection is
	 * connected to.
	 * The messages are guaranteed to be sent consecutively in exactly the order
	 * they were added to the OutputMessageQueue.
	 * 
	 * @param messages 
	 * @return Returns <code>false</code> if the connection was lost before the message 
	 * could be sent, and <code>true</code> otherwise. Note that a return value of
	 * <code>try</code> does <i>not</i> necessarily mean that the message was succesfully
	 * sent, only that the connection was still alive when it began sending.
	 */
	public synchronized boolean sendMessage(OutputMessageQueue messages) {
		while (currentlyWriting) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return false;
			}
		}
		OutputListener clientWriter = new OutputListener(this, out, messages);
		new Thread(clientWriter).start();
		return true;
	}

	/**
	 * Called by a ClientConnection's associated InputListener if an IOException occurred
	 * while it was listening for input.
	 * The ClientConnection is responsible for deciding whether the InputListener should
	 * maintain the connection.
	 * 
	 * @param e The exception thrown by the Socket. The ClientConnection can decide what
	 * to do based on what went wrong.
	 * @return <code>true</code> if the InputListener should keep trying to listen for
	 * more input, and <code>false</code> if it should give up and close the input stream.
	 */
	private boolean connectionErrorOccured(IOException e) {
		return keepConnected;
	}
	
	/**
	 * Disconnects the associated client. No more input will be read, and no more
	 * output will be sent.
	 */
	protected void disconnect() {
		keepConnected = false;
		try {
			socket.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Get the User which the associated client has logged in as.
	 * @return The User which the associated client has logged in as.
	 */
	public User getUser() {
		return user;
	}

}
