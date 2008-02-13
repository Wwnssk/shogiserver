package server.services.connection;

import java.util.Properties;

import server.services.event.Event;

/**
 * This event occurs whenever a client closes a logged-in session
 * and disconnects from the server.
 * 
 * @author Adrian Petrescu
 *
 */
public class UserDisconnectedEvent extends Event {

	private static final String NAME = "USER_DISCONNECT";

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Called when a client disconnects from the server.
	 * 
	 * @param userName The name of the user who disconnected.
	 */
	void occured(String userName) {
		Properties properties = new Properties();
		properties.setProperty("userName", userName);
		super.occured(properties);
	}

}
