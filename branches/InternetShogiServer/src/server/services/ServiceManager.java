package server.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

import server.services.connection.ConnectionManager;
import server.services.protocol.ProtocolManager;
import server.services.user.UserManager;

/**
 * The ServiceManager is one of the server's key subsystems. It is responsible for
 * registering and loading every GlobalService on the system, and ensuring that they
 * are only loaded once and do not conflict with each other.
 * 
 * @author Adrian Petrescu
 *
 */
public class ServiceManager {

	private static ConcurrentHashMap<String, GlobalService> serviceList = new ConcurrentHashMap<String, GlobalService>();
	private static ConcurrentHashMap<String, Properties> configurationList = new ConcurrentHashMap<String, Properties>();
	
	/**
	 * Loads a Properties file as the configuration for the named service. Note that
	 * this method has no effect if the named service has already been started - this
	 * method <b>must</b> be called before the service is first used.
	 * 
	 * @param serviceName The service to configure.
	 * @param properties The Properties file containing the appropriate configuration.
	 * @return <code>true</code> if the service has not previously been started and loading
	 * the configuration was successful. <code>falst</code> if the service was already started
	 * and this configuration was useless.
	 */
	public static boolean loadConfiguration(String serviceName, Properties properties) {
		if (serviceList.containsKey(serviceName)) {
			return false;
		} else {
			configurationList.put(serviceName, properties);
			return true;
		}
	}
	
	/**
	 * Get a GlobalService by name. This method should only be used for third-party 
	 * GlobalServices. The "core" services each have their own special accessor.
	 * 
	 * @param serviceName The unique string identifying the desired GlobalService.
	 * @return The named GlobalService.
	 * @throws InvalidServiceException thrown if the named GlobalService has not been
	 * successfully registered.
	 */
	public static GlobalService getService(String serviceName) throws InvalidServiceException {
		return serviceList.get(serviceName);
	}
	
	/**
	 * Get access to the ConnectionManager service, which controls the threads for
	 * every individual client connection.
	 * 
	 * If the ConnectionManager has not been accessed before, it will be created now.
	 * 
	 * @return A reference to the ConnectionManager.
	 */
	public static ConnectionManager getConnectionManager() {
		if (!serviceList.containsKey(ConnectionManager.SERVICE_NAME)) {
			ConnectionManager connectionManager = new ConnectionManager();
			try {
				if (configurationList.containsKey(ConnectionManager.SERVICE_NAME)) {
					connectionManager.initialize(configurationList.get(ConnectionManager.SERVICE_NAME));
				} else {
					connectionManager.initialize(new Properties());
				}
			} catch (InvalidServiceConfigurationException e) {}
			serviceList.put(ConnectionManager.SERVICE_NAME, connectionManager);
		}
		return (ConnectionManager) serviceList.get(ConnectionManager.SERVICE_NAME);
	}
	
	/**
	 * Get access to the ProtocolManager service, which controls the ProtocolMap and
	 * each individual ProtocolModule.
	 * 
	 * If the ProtocolManager has not been accessed before, it will be created now.
	 * 
	 * @return A reference to the ProtocolManager.
	 */
	public static ProtocolManager getProtocolManager() {
		if (!serviceList.containsKey(ProtocolManager.SERVICE_NAME)) {
			ProtocolManager protocolManager = new ProtocolManager();
			try {
				if (configurationList.containsKey(ProtocolManager.SERVICE_NAME)) {
					protocolManager.initialize(configurationList.get(ProtocolManager.SERVICE_NAME));
				} else {
					protocolManager.initialize(new Properties());
				}
			} catch (InvalidServiceConfigurationException e) {
				serviceList.put(ProtocolManager.SERVICE_NAME, protocolManager);
			}
		}
		return (ProtocolManager) serviceList.get(ProtocolManager.SERVICE_NAME);
	}
	
	/**
	 * Get access to the UserManager service, which controls the data structures
	 * for each individual registered User.
	 * 
	 * If the UserManager has not been accessed before, it will be created now.
	 * 
	 * @return A reference to the UserManager.
	 */
	public static UserManager getUserManager() {
		if (!serviceList.containsKey(UserManager.SERVICE_NAME)) {
			UserManager userManager = new UserManager();
			try {
				if (configurationList.containsKey(UserManager.SERVICE_NAME)) {
					userManager.initialize(configurationList.get(UserManager.SERVICE_NAME));
				} else {
					userManager.initialize(new Properties());

				}
			} catch (InvalidServiceConfigurationException e) {}
			serviceList.put(UserManager.SERVICE_NAME, userManager);
		}
		return (UserManager) serviceList.get(UserManager.SERVICE_NAME);
	}
	
}
