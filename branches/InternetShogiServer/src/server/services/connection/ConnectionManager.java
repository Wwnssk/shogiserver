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

/**
 * This GlobalService is one of the core services of the server. It maintains
 * a table of all active connections, listens for new incoming connections, and
 * is responsible for terminating and checking existing connections.
 * 
 * @author Adrian Petrescu
 *
 */
public class ConnectionManager implements GlobalService {
	
	/**
	 * This thread establishes a ServerSocket on the given port and listens for
	 * new incoming connections. When they are received, the ConnectionManager is
	 * notified.
	 * 
	 * @author Adrian Petrescu
	 *
	 */
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
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
				while(connected) {
					try {
						Socket s = serverSocket.accept();
						ServiceManager.getConnectionManager().newConnection(s);
					} catch (IOException e) {
						e.printStackTrace();
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
	
	/**
	 * Called when the server is shutting down. The ConnectionManager stops
	 * accepting any more incoming connections, and iterates through the active 
	 * connections, closing them cleanly.
	 */
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
	
	/**
	 * Called when the server is ready to accept new incoming connections. A
	 * ConnectionListener thread is started up that listens on a given port for
	 * new incoming connections, and establishes a ClientConnection with them.
	 * 
	 * @param properties A Properties structure containing configuration data
	 * for the ConnectionManager.
	 * <br>
	 * <b>Required configuration options:</b>
	 * <br>
	 * port (the port to listen on).
	 */
	public void initialize(Properties properties) throws InvalidServiceConfigurationException {
		try {
			port = (Integer) properties.get("port");
		} catch (NullPointerException e) {
			throw new InvalidServiceConfigurationException(SERVICE_NAME, properties, "port");
		}
		connectionTable = new ConcurrentHashMap<User, ClientConnection>();
		ConnectionListener listener = new ConnectionListener(port);
		new Thread(listener, "ConnectionListener").start();
	}
	
	/**
	 * Called by the ConnectionManager when it has established a socket connection with
	 * a prospective client. This method is responsible for validating the connection's
	 * login credentials, creating an associated ClientConnection, and adding it to the
	 * global connection table.
	 * 
	 * @param socket A socket connection to the prospective client.
	 */
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
				out.flush();
				loginString = in.readLine();
				if (loginString.startsWith("login ") && loginString.split(" ").length == 3) {
					//TODO: Check password.
					User user = ServiceManager.getUserManager().getUser(loginString.split(" ")[1]);
					ClientConnection c = new ClientConnection(user, in, out, socket);
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
	
	/**
	 * Get the connection status of a particular User; the ConnectionManager checks
	 * to see if the client associated with the given User has an active connection
	 * established with the server.
	 * 
	 * @param user The user to check.
	 * @return <code>true</code> if the given User is currently logged in, and
	 * <code>false</code> if it is not.
	 */
	public boolean checkUserLoggedIn(User user) {
		return connectionTable.containsKey(user);
	}
	
	/**
	 * Get a connection channel with the specified User. This ClientConnection object
	 * can be used to send data to the particular client.
	 * ProtocolModules should <b>not</b> use this; it is for the output queue manager
	 * to use only.
	 * 
	 * @param user The User to get a connection with.
	 * @return A ClientConnection object if the user is currently logged in, aor
	 * <code>null</code> if not.
	 */
	public ClientConnection getUserConnection(User user) {
		ClientConnection conn = connectionTable.get(user);
		if (conn == null) {
			try {
				Thread.sleep(100);
				conn = connectionTable.get(user);
			} catch (InterruptedException e) {}
		}
		return conn;
	}
	
	/**
	 * Get the total number of active connections to the server at the time when the
	 * method is called.
	 * @return The total number of active users.
	 */
	public int getNumberOfUsersLoggedIn() {
		return connectionTable.size();
	}
	
	/**
	 * Log the user out of the server. All further communications will cease, and
	 * you will no longer be able to get a ClientConnection from the user until
	 * they log in again.
	 * @param user The User to log out.
	 */
	public void disconnectUser(User user) {
		ClientConnection userConnection = connectionTable.get(user);
		connectionTable.remove(user);
		userConnection.disconnect();
	}

}
