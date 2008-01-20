package server.services.user;

/**
 * A class representing a table of data about the user. The information
 * is accessed and cached on a per-need basis, so constant time is not
 * guaranteed at all for many of these operations. Often the structure is
 * going to the database to obtain them (although they will likely be
 * cached on future accesses).
 * 
 * @author Adrian Petrescu
 *
 */
public class UserInformation {
	private String userName;
	private int userID;
	private String email;
	private String description;
	
	/**
	 * Constructs a new UserInformation structure for the given user. It
	 * will be populated on a per-need basis.
	 *  
	 * @param userName The user name of the associated User.
	 */
	public UserInformation(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Get the user's user name.
	 * 
	 * @return The user name of the associated User.
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Get the UserID, a unique integer identifying every user on the
	 * server, sorted (ascending) by join date.
	 * 
	 * @return The userID of the associated User.
	 */
	public int getUserID() {
		return userID;
	}


	/**
	 * Get the user's stored email address.
	 * 
	 * @return The user's stored email address.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Set the user's stored email address.
	 * 
	 * @param email The new email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Get the user's stored profile description.
	 * 
	 * @return The user's stored profile description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the user's stored profile description.
	 * 
	 * @param description The new profile description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Forces the UserInformation to load all the available data
	 * for the user at once. It will be cached so that future
	 * accesses are much faster.
	 */
	public void reload() {
		
	}
	
}
