package server.services.user;

import server.services.ServiceManager;

/**
 * This class represents a unique User that has registered
 * with the server, and has an associated name, personal 
 * information, game history, rank, etc.
 * 
 * This class does <b>not</b> represent a connection to the
 * user (that is a <code>ClientConnection</code>), and indeed
 * a User object can be instantiated for users who are not even
 * currently logged in.
 * 
 * @author Adrian Petrescu
 *
 */
public class User {

	private String userName;
	private UserInformation info;
	
	/** Private constructor - only the UserManager can get information
	  * about users.
	  */
	private User() {}
	
	public boolean equals(Object obj) {
		return ((obj instanceof User)
				&& ((User) obj).getUserName().equals(this.getUserName()));
	}
	
	public int hashCode() {
		return getUserName().hashCode();
	}
	
	/**
	 * Constructs a new instance of User.
	 * 
	 * @param userName The user name of the user.
	 */
	protected User(String userName) {
		this.userName = userName;
		this.info = ServiceManager.getDatabaseManager().getUserInfo(this);
	}
	
	/**
	 * Gets the user's user name.
	 * 
	 * @return The user's user name.
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Returns a UserInformation structure containing all stored
	 * data about the user.
	 * @return A UserInformation structure containing all stored
	 * data about the user.
	 */
	public UserInformation getUserInformation() {
		return info;
	}
	
}
