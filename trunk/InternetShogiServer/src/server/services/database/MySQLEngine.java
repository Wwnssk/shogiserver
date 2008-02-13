package server.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This DatabaseEngine interfaces with a MySQL database, on either a local
 * or remote server. It was tested with MySQL 5.0, but should work on any
 * release.
 * 
 * @author Adrian Petrescu
 *
 */
public class MySQLEngine implements DatabaseEngine {

	Connection conn;
	
	@Override
	public void connect(String host, int port, String user, String password,
			String database) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
		if (conn == null || conn.isClosed()) {
			throw new SQLException();
		}
	}

	@Override
	public void disconnect() throws SQLException {
		conn.close();
	}
	
	public ResultSet getUserRow(String userName) {
		if (!validateAlphanumericToken(userName)) {
			return null;
		}
		
		String userQuery = "SELECT * FROM Users WHERE userName = '" + userName + "';";
		return executeQuery(userQuery);
	}
	
	public ResultSet getRoomRow(String roomName) {
		//TODO: Need some other form of security here.
		/*
		if (!validateAlphanumericToken(roomName)) {
			return null;
		}*/
		
		String roomQuery = "SELECT * FROM Rooms WHERE name = '" + roomName + "';";
		return executeQuery(roomQuery);
	}
	
	public ResultSet getAllUserNames() {
		String userQuery = "SELECT userName FROM Users;";
		return executeQuery(userQuery);
	}
	
	public ResultSet getAllRoomNames() {
		String roomQuery = "SELECT name FROM Rooms;";
		return executeQuery(roomQuery);
	}
	
	private synchronized ResultSet executeQuery(String query) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Verifies that a single token is alphanumeric. In particular, it will contain
	 * no " or ' or ) or ; characters, which would be a target for SQL injections.
	 * 
	 * @param token The token to be validated.
	 * @return <code>true</code> if every character in token is alphanumeric, and
	 * <code>false</code> otherwise.
	 */
	private boolean validateAlphanumericToken(String token) {
		for (int i = 0; i < token.length(); i++) {
			if (!Character.isLetterOrDigit(token.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean checkConnected() {
		try {
			return conn.isValid(10);
		} catch (SQLException e) {
			return false;
		}
	}

}
