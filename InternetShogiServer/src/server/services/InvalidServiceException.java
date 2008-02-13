package server.services;

/**
 * This Exception is thrown when some process attempts to call a GlobalService
 * which has not been successfully loaded.
 * 
 * @author Adrian Petrescu
 *
 */
public class InvalidServiceException extends Throwable {

	private String name;
	private String message;

	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifies this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	/**
	 * Constructs a new InvalidServiceException.
	 * 
	 * @param name The name of the GlobalService that the errant process
	 * tried to invoke.
	 */
	public InvalidServiceException(String name) {
		this.name = name;
	}
	
	/**
	 * Constructs a new InvalidServiceException. Includes a human-readable
	 * message explaining why the named GlobalService is not available.
	 * 
	 * @param name The name of the GlobalService that the errant process
	 * tried to invoke.
	 * @param message A human-readable message explaining why the named
	 * GlobalService is not available.
	 */
	public InvalidServiceException(String name, String message) {
		this.name = name;
		this.message = message;
	}
	
	/**
	 * Get a human-readable message explaining why the named GlobalService
	 * is not available.
	 * 
	 * @return A human-readable message explaining why the named GlobalService
	 * is not available.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Get the name of the GlobalService that the errant process tried to invoke.
	 * @return The name of the GlobalService that the errant process tried to invoke.
	 */
	public String getName() { 
		return name;
	}
	
}
