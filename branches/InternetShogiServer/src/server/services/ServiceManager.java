package server.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

import server.services.connection.ConnectionManager;
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
	
	/**
	 * Get a GlobalService by name. This method should only be used for third-party 
	 * GlobalServices. The "core" services each have their own special accessor.
	 * 
	 * @param serviceName The unique string identifying the desired GlobalService.
	 * @return The named GlobalService.
	 * @throws InvalidServiceException thrown if the named GlobalService has not been
	 * succesfully registered.
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
				connectionManager.initialize(new Properties());
			} catch (InvalidServiceConfigurationException e) {}
			serviceList.put(ConnectionManager.SERVICE_NAME, connectionManager);
		}
		return (ConnectionManager) serviceList.get(ConnectionManager.SERVICE_NAME);
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
				userManager.initialize(new Properties());
			} catch (InvalidServiceConfigurationException e) {}
			serviceList.put(UserManager.SERVICE_NAME, userManager);
		}
		return (UserManager) serviceList.get(UserManager.SERVICE_NAME);
	}
	
}
