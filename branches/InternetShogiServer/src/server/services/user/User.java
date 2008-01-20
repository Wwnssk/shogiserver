package server.services.user;

public class User {

	private String userName;
	private UserInformation info;
	private boolean isGuest;
	
	private User() {}
	
	protected User(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public UserInformation getUserInformation() {
		if (info == null) {
			info = new UserInformation(userName);
		}
		return info;
	}
	
	public boolean isGuest() {
		return isGuest;
	}
}
