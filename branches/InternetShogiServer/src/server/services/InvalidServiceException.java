package server.services;

public class InvalidServiceException extends Throwable {

	private String name;
	private String message;

	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifices this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	public InvalidServiceException(String name) {
		this.name = name;
	}
	
	public InvalidServiceException(String name, String message) {
		this.name = name;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getName() { 
		return name;
	}
	
}
