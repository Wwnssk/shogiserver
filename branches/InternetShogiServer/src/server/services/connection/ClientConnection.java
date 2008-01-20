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

public class ClientConnection {

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
					clientConnection.messageRecieved(line);
				} catch (IOException e) {
					// An error occured, we'll let the server decide whether we
					// should keep trying.
					connected = clientConnection.connectionErrorOccured(e);
				}
			}
		}
	}

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

	public ClientConnection(User user, Socket socket) throws IOException {
		inputListener = new InputListener(this,
				new BufferedReader(new InputStreamReader(socket
						.getInputStream())));
		out = new PrintWriter(socket.getOutputStream());
		currentlyWriting = false;
		this.user = user;
		this.socket = socket;
		keepConnected = true;
		new Thread(inputListener).start();
	}

	private void messageRecieved(String message) {
		ProtocolMessage pMessage = new ProtocolMessage(message);
		InputMessageQueue messageQueue = new InputMessageQueue(pMessage);
		GlobalInputMessageQueue.getGlobalInputMessageQueue().enqueue(
				messageQueue);
	}

	private void messageSent() {
		currentlyWriting = false;
	}

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

	private boolean connectionErrorOccured(IOException e) {
		return keepConnected;
	}
	
	protected void disconnect() {
		keepConnected = false;
		try {
			socket.close();
		} catch (IOException e) {}
	}
	
	public User getUser() {
		return user;
	}

}
