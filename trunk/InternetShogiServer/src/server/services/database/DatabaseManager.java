package server.services.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;
import server.services.user.User;
import server.services.user.UserInformation;

public class DatabaseManager implements GlobalService {

	public static final String SERVICE_NAME = "DatabaseManager";
	DatabaseEngine engine;
	
	@Override
	public String getIdentifier() {
		return SERVICE_NAME;
	}

	@Override
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
	 * @param user The user whose information to retrieve.
	 * @return A fully populated UserInformation structure.
	 */
	public UserInformation getUserInfo(User user) {
		ResultSet rs = engine.getUserRow(user.getUserName());
		UserInformation userInfo = new UserInformation(user.getUserName());
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
		
	@Override
	public void shutdown() {
		try {
			engine.disconnect();
		} catch (SQLException e) {}
	}

}
