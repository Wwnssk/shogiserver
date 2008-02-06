package server.main;

import java.util.Properties;

import server.services.ServiceManager;
import server.services.connection.ConnectionManager;
import server.services.protocol.InputMessageQueue;
import server.services.protocol.ProtocolManager;
import server.services.user.UserManager;

public class Server {

	private ConnectionManager connectionManager;
	private UserManager userManager;
	private ProtocolManager protocolManager;
	
	private GlobalInputMessageQueue inputQueue;
	private GlobalOutputMessageQueue outputQueue;
	
	private volatile boolean alive = true;
	
	protected Server(Properties databaseConfig, Properties connectionConfig,
			Properties userConfig, Properties protocolConfig) {
		
		inputQueue = GlobalInputMessageQueue.getGlobalInputMessageQueue();
		outputQueue = GlobalOutputMessageQueue.getGlobalOutputMessageQueue();
		
		ServiceManager.loadConfiguration(ConnectionManager.SERVICE_NAME, connectionConfig);
		ServiceManager.loadConfiguration(UserManager.SERVICE_NAME, userConfig);
		ServiceManager.loadConfiguration(ProtocolManager.SERVICE_NAME, protocolConfig);
		
		connectionManager = ServiceManager.getConnectionManager();
		userManager = ServiceManager.getUserManager();
		protocolManager = ServiceManager.getProtocolManager();
		
		OutputQueueProcessor outputQueueProcessor = new OutputQueueProcessor(connectionManager, outputQueue);
		new Thread(outputQueueProcessor, "OutputQueueProcessor").start();
	}
	
	@SuppressWarnings("deprecation")
	public void process() {
		while (alive) {
			InputMessageQueue input = inputQueue.dequeue();
			if (input == null) {
				Thread.currentThread().suspend();
			} else {
				protocolManager.parseMessages(input);
			}
		}
	}

}
