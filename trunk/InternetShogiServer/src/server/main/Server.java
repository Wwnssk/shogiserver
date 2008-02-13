package server.main;

import java.util.Properties;

import server.services.ServiceManager;
import server.services.connection.ConnectionManager;
import server.services.protocol.InputMessageQueue;
import server.services.protocol.ProtocolManager;
import server.services.user.UserManager;
import server.services.database.DatabaseManager;
import server.services.event.EventManager;

/**
 * This is the main Server component. It is the first piece to be brought
 * up, and it is responsible for instantiating all the various GlobalService's,
 * the global IO queues, and for listening and processing the input queue.
 * 
 * @author Adrian Petrescu
 *
 */
public class Server {

	private ConnectionManager connectionManager;
	private UserManager userManager;
	private ProtocolManager protocolManager;
	private DatabaseManager databaseManager;
	private EventManager eventManager;
	
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
		ServiceManager.loadConfiguration(DatabaseManager.SERVICE_NAME, databaseConfig);
		
		eventManager = ServiceManager.getEventManager();
		databaseManager = ServiceManager.getDatabaseManager();
		connectionManager = ServiceManager.getConnectionManager();
		userManager = ServiceManager.getUserManager();
		protocolManager = ServiceManager.getProtocolManager();
		
		OutputQueueProcessor outputQueueProcessor = new OutputQueueProcessor(connectionManager, outputQueue);
		new Thread(outputQueueProcessor, "OutputQueueProcessor").start();
	}
	
	/**
	 * Begin the cycle of constantly waiting for the input queue to have
	 * data in it, and processing it.
	 */
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
