package server.services.user;

import java.util.Properties;
import java.util.HashMap;

import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;

public class UserManager implements GlobalService {
	
	private HashMap<String, User> userTable;
	
	public static final String SERVICE_NAME = "UserManager";
	
	
	public String getIdentifier() {
		return SERVICE_NAME;
	}
	
	public void initialize(Properties properties) throws InvalidServiceConfigurationException {
		userTable = new HashMap<String, User>();
	}
	
	public void shutdown() {
		
	}
	
	public User getUser(String userName) throws NoSuchUserException {
		//TODO: Get user information from database
		return new User(userName);
	}

}
