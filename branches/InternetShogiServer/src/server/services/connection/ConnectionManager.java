package server.services.connection;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import server.services.GlobalService;
import server.services.ServiceManager;
import server.services.InvalidServiceConfigurationException;
import server.services.user.NoSuchUserException;
import server.services.user.User;

public class ConnectionManager implements GlobalService {
	
	class ConnectionListener implements Runnable {
		
		private int port;
		private boolean connected;
		private ServerSocket serverSocket;
		
		public ConnectionListener(int port) {
			this.port = port;
			connected = true;
		}
		
		public void disconnect() {
			try {
				connected = false;
				serverSocket.close();
			} catch (IOException e) {}
		}
		
		public void run() {
				while(connected) {
					try {
						serverSocket = new ServerSocket(port);
						Socket s = serverSocket.accept();
						ServiceManager.getConnectionManager().newConnection(s);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
		}
	}
	
	public static final String SERVICE_NAME = "ConnectionManager";
	private static final int MAX_RETRIES = 3;
	private int port;
	private ConnectionListener listener;
	private ConcurrentHashMap<User, ClientConnection> connectionTable;
	
	public String getIdentifier() {
		return SERVICE_NAME;
	}
	
	public void shutdown() {
		listener.disconnect();
		while (!connectionTable.isEmpty()) {
			Collection<ClientConnection> connections = connectionTable.values();
			Iterator<ClientConnection> connectionsIterator = connections.iterator();
			while (connectionsIterator.hasNext()) {
				ClientConnection connection = connectionsIterator.next();
				connection.disconnect();
				connections.remove(connection);
			}
		}
	}
	
	public void initialize(Properties properties) throws InvalidServiceConfigurationException {
		try {
			port = (Integer) properties.get("port");
		} catch (NullPointerException e) {
			throw new InvalidServiceConfigurationException(SERVICE_NAME, properties, "port");
		}
		connectionTable = new ConcurrentHashMap<User, ClientConnection>();
		ConnectionListener listener = new ConnectionListener(port);
		new Thread(listener).start();
	}
	
	private void newConnection(Socket socket) {
		// TODO: Make sure user isn't logging in twice.
		try {
			int retries = 0;
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			String loginString = in.readLine();
			
			// Try MAX_RETRIES time to get the proper 'ping' connection request.
			while (!loginString.equals("ping") && retries < MAX_RETRIES) {
				loginString = in.readLine();
				retries++;
			}
			
			if (retries >= MAX_RETRIES) {
				in.close();
				out.close();
				socket.close();
			} else {
				out.println("pong");
				loginString = in.readLine();
				if (loginString.startsWith("login ") && loginString.split(" ").length == 3) {
					//TODO: Check password.
					in.close();
					out.close();
					User user = ServiceManager.getUserManager().getUser(loginString.split(" ")[1]);
					ClientConnection c = new ClientConnection(user, socket);
					connectionTable.put(user, c);
				} else {
					in.close();
					out.close();
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			// TODO: Register the new user
			e.printStackTrace();
		}
	}

}
