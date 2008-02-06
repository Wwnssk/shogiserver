package server.services.user;

/**
 * This Exception is thrown whenever a process attempts to access
 * information about a User who does not exist on the server.
 * 
 * @author Adrian Petrescu
 *
 */
public class NoSuchUserException extends Throwable {
	
	private String userName;

	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifices this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	/**
	 * Constructs a new NoSuchUserException.
	 * 
	 * @param userName The user name which was accessed, but which
	 * does not exist on the server.
	 */
	public NoSuchUserException(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Get the accessed user name.
	 * 
	 * @return The user name which was accessed, but which
	 * does not exist on the server.
	 */
	public String getName() { 
		return userName;
	}
	
}
