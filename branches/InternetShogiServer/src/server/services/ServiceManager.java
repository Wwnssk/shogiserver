package server.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

import server.services.connection.ConnectionManager;
import server.services.user.UserManager;

public class ServiceManager {

	private static ConcurrentHashMap<String, GlobalService> serviceList = new ConcurrentHashMap<String, GlobalService>();
	
	public static GlobalService getService(String serviceName) throws InvalidServiceException {
		return serviceList.get(serviceName);
	}
	
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
