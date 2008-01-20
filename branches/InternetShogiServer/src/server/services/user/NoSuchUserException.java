package server.services.user;

public class NoSuchUserException extends Throwable {

	private String userName;
	private String message;

	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifices this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	public NoSuchUserException(String userName) {
		this.userName = userName;
	}
	
	public NoSuchUserException(String userName, String message) {
		this.userName = userName;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getName() { 
		return userName;
	}
	
}
