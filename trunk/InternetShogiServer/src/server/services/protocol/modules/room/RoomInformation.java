package server.services.protocol.modules.room;

public class RoomInformation {

	private String description;
	private String name;
	private String[] owners;
	
	/**
	 * Constructs a new instance of a RoomInformation structure.
	 * 
	 * @param name The name of the corresponding room.
	 */
	public RoomInformation(String name) {
		this.name = name;
	}
	
	/**
	 * Fetch the publicly-viewable description string of the room.
	 * 
	 * @return The description of the room.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the room's description. There is a 500-character limit to the room description.
	 * If a longer string is passed in, it will be automatically shortened to 500 characters.
	 * 
	 * @param description The new description for the room.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Fetch the name of the room.
	 * 
	 * @return The room's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Fetch the owners of the room.
	 * 
	 * @return An array containing the userNames of the owners of this room.
	 */
	public String[] getOwners() {
		return owners;
	}
	
	/**
	 * Set the owners of the room.
	 * 
	 * @param owners An array containing the userNames of the new owners of this room.
	 */
	public void setOwners(String[] owners) {
		this.owners = owners;
	}
	
}
