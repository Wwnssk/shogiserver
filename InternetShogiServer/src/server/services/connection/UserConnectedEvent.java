package server.services.connection;

import java.util.Properties;

import server.services.event.Event;

/**
 * This event occurs whenever a client initiates a new connection
 * to the server and logs in successfully.
 * 
 * @author Adrian Petrescu
 *
 */
public class UserConnectedEvent extends Event {
	
	private static final String NAME = "USER_CONNECT";
	
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Called when a client connects to the server.
	 * 
	 * @param userName The name of the user who connected.
	 */
	void occured(String userName) {
		Properties properties = new Properties();
		properties.setProperty("userName", userName);
		super.occured(properties);
	}

}
