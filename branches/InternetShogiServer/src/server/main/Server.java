package server.main;

import java.util.Properties;

import server.services.ServiceManager;
import server.services.connection.ConnectionManager;
import server.services.connection.GlobalInputMessageQueue;
import server.services.connection.GlobalOutputMessageQueue;
import server.services.protocol.ProtocolManager;
import server.services.user.UserManager;

public class Server {

	private ConnectionManager connectionManager;
	private UserManager userManager;
	private ProtocolManager protocolManager;
	
	private GlobalInputMessageQueue inputQueue;
	private GlobalOutputMessageQueue outputQueue;
	
	private volatile boolean alive = true;
	
	public Server(Properties databaseConfig, Properties connectionConfig,
			Properties userConfig, Properties protocolConfig) {
		
		inputQueue = GlobalInputMessageQueue.getGlobalInputMessageQueue();
		outputQueue = GlobalOutputMessageQueue.getGlobalOutputMessageQueue();
		
		ServiceManager.loadConfiguration(ConnectionManager.SERVICE_NAME, connectionConfig);
		ServiceManager.loadConfiguration(UserManager.SERVICE_NAME, userConfig);
		ServiceManager.loadConfiguration(ProtocolManager.SERVICE_NAME, protocolConfig);
		
		connectionManager = ServiceManager.getConnectionManager();
		userManager = ServiceManager.getUserManager();
		protocolManager = ServiceManager.getProtocolManager();
	}
	
	public void process() {
		while (alive) {
		}
	}

}
