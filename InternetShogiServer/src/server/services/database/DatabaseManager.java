package server.services.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;
import server.services.protocol.modules.room.RoomInformation;
import server.services.user.UserInformation;

/**
 * This GlobalService is one of the core services of the system. It
 * maintains the connection with the database, and is responsible for
 * retrieving and persisting info back and forth between the database
 * engine and the server.
 * 
 * @author Adrian Petrescu
 *
 */
public class DatabaseManager implements GlobalService {

	public static final String SERVICE_NAME = "DatabaseManager";
	DatabaseEngine engine;
	
	public String getIdentifier() {
		return SERVICE_NAME;
	}

	public void initialize(Properties properties)
			throws InvalidServiceConfigurationException {
		String dbEngine = properties.getProperty("engine");
		if (dbEngine == null || !dbEngine.equals("mysql")) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "engine", "Invalid or missing engine. Currently supported engines are: mysql");
		}
		
		String dbHost = properties.getProperty("host");
		if (dbHost == null) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "host", "Missing db_host parameter");
		}

		String dbUser = properties.getProperty("user");
		if (dbUser == null) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "user", "Missing db_user parameter");
		}
		
		String dbPass = properties.getProperty("pass");
		if (dbPass == null) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "pass", "Missing db_pass parameter");
		}
		
		int dbPort;
		try {
			String sPort = properties.getProperty("port");
			if (sPort == null) {
				throw new InvalidServiceConfigurationException(getIdentifier(), properties, "port", "Missing db_port parameter");
			}
			dbPort = Integer.parseInt(properties.getProperty("port"));
		} catch (NumberFormatException e) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "port", "Invalid port, not a numeric value");
		}
		
		String dbDatabase = properties.getProperty("database");
		if (dbDatabase == null) {
			throw new InvalidServiceConfigurationException(getIdentifier(), properties, "database", "Missing db_database parameter");
		}
		
		
		if (dbEngine.equals("mysql")) {
			engine = new MySQLEngine();
			try {
				engine.connect(dbHost, dbPort, dbUser, dbPass, dbDatabase);
			} catch (SQLException e) {
				throw new InvalidServiceConfigurationException(getIdentifier(), properties, "database", "Could not log onto database");
			}
		}
		
	}
	
	/**
	 * Get a UserInformation structure populated with data about the given
	 * user.
	 * 
	 * @param userName The user whose information to retrieve.
	 * @return A fully populated UserInformation structure, or <code>null</code>
	 * if the specified user could not be found.
	 */
	public UserInformation getUserInfo(String userName) {
		ResultSet rs = engine.getUserRow(userName);
		if (rs == null) {
			return null;
		}
		UserInformation userInfo = new UserInformation(userName);
		try {
			rs.next();
			userInfo.setEmail(rs.getString("email"));
			userInfo.setDescription(rs.getString("description"));
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return userInfo;
	}
	
	/**
	 * Get a RoomInformation structure populated with data about the given
	 * room.
	 * 
	 * @param roomName The room whose information to retrieve.
	 * @return A fully populated RoomInformation structure, or <code>null</code>
	 * if the specified room could not be found.
	 */
	public RoomInformation getRoomInfo(String roomName) {
		ResultSet rs = engine.getRoomRow(roomName);
		if (rs == null) {
			return null;
		}
		RoomInformation roomInfo = new RoomInformation(roomName);
		try {
			rs.next();
			roomInfo.setDescription(rs.getString("description"));
			roomInfo.setOwners(rs.getString("owners").split(","));
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return roomInfo;
	}
	
	/**
	 * Get a list of all registered users.
	 * 
	 * @return An array containing the UserName of every user in the database.
	 */
	public String[] getRegisteredUsers() {
		ResultSet rs = engine.getAllUserNames();
		ArrayList<String> names = new ArrayList<String>();
		try {
			while (rs.next()) {
				names.add(rs.getString("userName"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] nameArray = new String[names.size()];
		names.toArray(nameArray);
		return nameArray;
	}
	
	/**
	 * Get a list of all registered rooms.
	 * 
	 * @return An array containing the name of every room in the database.
	 */
	public String[] getRegisteredRooms() {
		ResultSet rs = engine.getAllRoomNames();
		ArrayList<String> names = new ArrayList<String>();
		try {
			while (rs.next()) {
				names.add(rs.getString("name"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] nameArray = new String[names.size()];
		names.toArray(nameArray);
		return nameArray;
	}
	
	/**
	 * Checks a user's login credentials to make sure the given user name and
	 * password match what is in the database.
	 * 
	 * @param userName The userName of the user attempting to log in.
	 * @param password The attempted password.
	 * @return <code>true</code> if the provided password was correct for the
	 * given user, and <code>false</code> otherwise.
	 */
	public boolean validateLogin(String userName, String password) {
		ResultSet rs = engine.getUserRow(userName);
		try {
			rs.next();
			String correctPassword = rs.getString("password");
			return password.equals(correctPassword);
		} catch (SQLException e) {
			return false;
		}
	}
		
	public void shutdown() {
		try {
			engine.disconnect();
		} catch (SQLException e) {}
	}

}
