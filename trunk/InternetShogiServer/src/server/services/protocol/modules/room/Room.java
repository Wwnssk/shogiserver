package server.services.protocol.modules.room;

import java.util.ArrayList;

import server.services.ServiceManager;
import server.services.user.NoSuchUserException;
import server.services.user.User;

/**
 * A room is an equivalent concept to a channel on IRC. All the participants in
 * a room can send messages to all other participants of the room.
 * 
 * @author Adrian Petrescu
 *
 */
public class Room {

	private ArrayList<User> occupants;
	private ArrayList<User> owners;
	private RoomInformation roomInfo;
	private String roomName;
	
	@SuppressWarnings("unused")
	private Room() { }
	
	Room(String roomName) {
		occupants = new ArrayList<User>();
		owners = new ArrayList<User>();
		
		this.roomName = roomName;
		this.roomInfo = ServiceManager.getDatabaseManager().getRoomInfo(roomName);
		for (String owner : roomInfo.getOwners()) {
			try {
				owners.add(ServiceManager.getUserManager().getUser(owner));
			} catch (NoSuchUserException e) {}
		}
		
		/* Check if we've lost any owners. If we have, make sure the RoomInformation
		 * structure knows about this.
		 */
		if (roomInfo.getOwners().length > owners.size()) {
			String[] newOwners = new String[owners.size()];
			for (int i = 0; i < newOwners.length; i++) {
				newOwners[i] = owners.get(i).getUserName();
			}
			roomInfo.setOwners(newOwners);
		}
	}
	
	/**
	 * Add a new user to the room's occupancy list.
	 * 
	 * @param user The user to add.
	 */
	public void addUser(User user) {
		occupants.add(user);
	}
	
	/**
	 * Remove a user from the room's occupancy list.
	 * 
	 * @param user The user to remove.
	 * @return <code>true</code> if the user was in the room originally, and
	 * <code>false</code> otherwise.
	 */
	public boolean removeUser(User user) {
		return occupants.remove(user);
	}
	
	/**
	 * Add a new owner the room.
	 * 
	 * @param owner The new owner.
	 */
	public void addOwner(User owner) {
		owners.add(owner);
	}
	
	/**
	 * Remove an owner from the room.
	 * 
	 * @param owner The owner to remove.
	 */
	public void removeOwner(User owner) {
		owners.remove(owner);
	}
	
	/**
	 * Get all of the occupants of this room.
	 * 
	 * @return An array containing all of the current occupants of this room.
	 */
	public User[] getOccupants() {
		User[] occupantArray = new User[occupants.size()];
		occupants.toArray(occupantArray);
		return occupantArray;
	}
	
	public RoomInformation getRoomInformation() {
		return this.roomInfo;
	}
}
