package server.services.user;

import java.util.Properties;
import java.util.HashMap;

import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;

/**
 * This GlobalService is one of the core services of the server. It maintains
 * a table of all registered users, both those who are currently logged in, and
 * those who are not. It is able to retrieve and update their information, register
 * new users, and remove old ones.
 * 
 * @author Adrian Petrescu
 *
 */
public class UserManager implements GlobalService {
	
	private HashMap<String, User> userTable;
	
	public static final String SERVICE_NAME = "UserManager";
	
	
	public String getIdentifier() {
		return SERVICE_NAME;
	}
	
	/**
	 * Called when information about Users becomes relevant (i.e, after the database
	 * is up). It constructs a table of every user registered with the server.
	 * 
	 * @param properties A Properties structure containing configuration data for the
	 * UserManager.
	 * <br>
	 * <b>Required configuration options:</b>
	 * <br>
	 * user_table (the table containing the user records).
	 */
	public void initialize(Properties properties) throws InvalidServiceConfigurationException {
		userTable = new HashMap<String, User>();
	}
	
	/**
	 * Called when the server is shutting down. The connection to the database is closed
	 * and any further requests for data will be met with refusal.
	 */
	public void shutdown() {
		
	}
	
	/**
	 * Get information about a User registered with the system.
	 * 
	 * @param userName The user name of the desired User.
	 * @return A registered User corresponding to the given name.
	 * @throws NoSuchUserException Thrown if no User with the given name has registered
	 * with the server.
	 */
	public User getUser(String userName) throws NoSuchUserException {
		//TODO: Get user information from database
		// For now, it just creates the user. THIS WILL NOT LAST!
		
		userTable.put(userName, new User(userName));
		return userTable.get(userName);
	}

}
