package server.services.protocol.modules.room;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import server.services.ServiceManager;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;
import server.services.protocol.modules.InvalidProtocolConfigurationException;
import server.services.protocol.modules.ProtocolModule;
import server.services.user.User;

//TODO: Add Javadoc to room-related classes
//TODO: Handle multi-token room names

public class RoomManager implements ProtocolModule {

	private static final String name = "Room";
	private static final String protocolKey = "room";
	private static final String version = "0.01";
	private static final String[] dependencies = { "Tell 0.1" };

	private ConcurrentHashMap<String, Room> roomTable;

	@Override
	public String[] getDependencies() {
		return dependencies;
	}

	@Override
	public String getKey() {
		return protocolKey;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}
	
	/**
	 * Fetch a Room by name.
	 * 
	 * @param roomName The name of the Room to get.
	 * @return The corresponding Room, or <code>null</code> if no such room exists.
	 */
	Room getRoom(String roomName) {
		return roomTable.get(roomName);
	}

	@Override
	public void initialize(Properties properties)
			throws InvalidProtocolConfigurationException {
		roomTable = new ConcurrentHashMap<String, Room>();

		// Load up every room from the database
		String[] roomList = ServiceManager.getDatabaseManager()
				.getRegisteredRooms();
		for (String roomName : roomList) {
			Room room = new Room(roomName);
			roomTable.put(roomName, room);
		}
	}

	private OutputMessageQueue invalidSyntaxMessage(ProtocolMessage message) {
		return new OutputMessageQueue(new ProtocolMessage(message.getUser(),
				getKey() + " syntax"));
	}

	@Override
	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		String[] messagePayload = message.getTokenizedPayload();

		switch (messagePayload.length) {
		case 0:
			return new OutputMessageQueue(new ProtocolMessage(
					message.getUser(), getKey() + " invalid"));
			
		case 1:
			if (messagePayload[0].equals("list")) {
				OutputMessageQueue roomListMessageQueue = new OutputMessageQueue();
				ProtocolMessage roomListMessage = new ProtocolMessage(message.getUser(), getKey() + " list");
				String[] roomNames = new String[roomTable.keySet().size()];
				roomTable.keySet().toArray(roomNames);
				for (String roomName : roomNames) {
					roomListMessage.append(roomName);
				}
				roomListMessageQueue.enqueue(roomListMessage);
				return roomListMessageQueue;
			}
			break;
			
		case 2:
			if (messagePayload[0].equals("info")) {
				OutputMessageQueue roomInfoMessageQueue = getRoomInfoMessage(messagePayload[1]);
				roomInfoMessageQueue.setUser(message.getUser());
				return roomInfoMessageQueue;
			}
			
			if (messagePayload[0].equals("join")) {
				Room roomToJoin = getRoom(messagePayload[1]);
				if (roomToJoin != null) {
					roomToJoin.addUser(message.getUser());
				}
				return getRoomJoinMessages(message);
			}
			
			if (messagePayload[0].equals("leave")) {
				Room roomToLeave = getRoom(messagePayload[1]);
				boolean leftRoom = false;
				if (roomToLeave != null) {
					leftRoom = roomToLeave.removeUser(message.getUser());
				}
				return getRoomLeaveMessage(message, leftRoom);
			}
			break;
		}

		return invalidSyntaxMessage(message);
	}

	private OutputMessageQueue getRoomJoinMessages(ProtocolMessage message) {
		OutputMessageQueue roomJoinMessageQueue = new OutputMessageQueue();
		Room room = getRoom(message.getTokenizedPayload()[1]);
		
		if (room == null) {
			roomJoinMessageQueue.enqueue(new ProtocolMessage(message.getUser(),
					"room join invalid room_not_exist"));
			return roomJoinMessageQueue;
		}
		
		for (User roomOccupant : room.getOccupants()) {
			ProtocolMessage joinMessage = new ProtocolMessage(roomOccupant);
			joinMessage.setProtocolKey(getKey());
			joinMessage.append("join");
			joinMessage.append(room.getRoomInformation().getName());
			joinMessage.append(message.getUser().getUserName());
			roomJoinMessageQueue.enqueue(joinMessage);
		}
		
		return roomJoinMessageQueue;
	}
	
	private OutputMessageQueue getRoomLeaveMessage(ProtocolMessage message, boolean leftRoom) {
		OutputMessageQueue roomLeaveMessageQueue = new OutputMessageQueue();
		Room room = getRoom(message.getTokenizedPayload()[1]);
		
		if (room == null || !leftRoom) {
			roomLeaveMessageQueue.enqueue(new ProtocolMessage(message.getUser(),
					"room leave invalid room_not_joined"));
			return roomLeaveMessageQueue;
		}
		
		for (User roomOccupant : room.getOccupants()) {
			ProtocolMessage leaveMessage = new ProtocolMessage(roomOccupant);
			leaveMessage.setProtocolKey(getKey());
			leaveMessage.append("leave");
			leaveMessage.append(room.getRoomInformation().getName());
			leaveMessage.append(message.getUser().getUserName());
			roomLeaveMessageQueue.enqueue(leaveMessage);
		}
		ProtocolMessage leaveMessage = new ProtocolMessage(message.getUser());
		leaveMessage.setProtocolKey(getKey());
		leaveMessage.append("leave");
		leaveMessage.append(room.getRoomInformation().getName());
		leaveMessage.append(message.getUser().getUserName());
		roomLeaveMessageQueue.enqueue(leaveMessage);		
		
		return roomLeaveMessageQueue;
	}
	
	/**
	 * Returns an OutputMessageQueue giving all relevant room information the
	 * the client. Its protocol is: <br>
	 * <br>
	 * <code>room info name <i>name</i></code><br>
	 * <code>room info description <i>description</i></code><br>
	 * <code>room info occupants <i>occupants</i></code><br>
	 * <code>room info owners <i>owners</i></code><br>
	 * <br>
	 * If the given roomName does not exist, it returns:<br>
	 * <br>
	 * <code>room info invalid room_not_exist</code> 
	 * 
	 * @param roomName
	 *            The room to return information about.
	 * @return An OutputMessageQueue with one ProtocolMessage for each line
	 *         specified above. The associated User is not guaranteed to have
	 *         any particular value.
	 */
	private OutputMessageQueue getRoomInfoMessage(String roomName) {
		OutputMessageQueue roomInfoMessageQueue = new OutputMessageQueue();
		Room room = getRoom(roomName);
		
		if (room == null) {
			roomInfoMessageQueue.enqueue(new ProtocolMessage("room info invalid room_not_exist"));
		}

		roomInfoMessageQueue.enqueue(new ProtocolMessage(getKey()
				+ " info name " + roomName));
		roomInfoMessageQueue.enqueue(new ProtocolMessage(getKey()
				+ " info description "
				+ room.getRoomInformation().getDescription()));

		ProtocolMessage roomOccupants = new ProtocolMessage(getKey()
				+ " info occupants");
		for (User occupant : room.getOccupants()) {
			roomOccupants.append(occupant.getUserName());
		}
		roomInfoMessageQueue.enqueue(roomOccupants);

		ProtocolMessage roomOwners = new ProtocolMessage(getKey()
				+ " info owners");
		for (String owner : room.getRoomInformation().getOwners()) {
			roomOwners.append(owner);
		}
		roomInfoMessageQueue.enqueue(roomOwners);

		return roomInfoMessageQueue;
	}

	@Override
	public void shutdown() {}

}
