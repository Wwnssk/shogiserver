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

	public void disconnect() throws SQLException {
		conn.close();
	}
	
	public boolean checkConnected() {
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public ResultSet getUserRow(String userName) {
		if (!validateAlphanumericToken(userName)) {
			return null;
		}
		
		String userQuery = "SELECT * FROM Users WHERE userName = '" + userName + "';";
		return executeQuery(userQuery);
	}
	
	public ResultSet getRoomRow(String roomName) {
		if (!validateAlphanumericToken(roomName)) {
			return null;
		}
		
		/*
		 * For the simplicity of the server code, I decided to enforce single-token
		 * room names, with the following convention: the client will interpret the
		 * underscore as a space, and display it that way to the user.
		 */
		
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
	
	/**
	 * Executes a raw SQL query. It performs <b>no validation</b> whatsoever,
	 * it is the caller's job to ensure the queries are clean.
	 * 
	 * @param query The query to be executed.
	 * @return The ResultSet returned from the query, or <code>null</code>
	 * only if the query was invalid.
	 */
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
	 * Strangely enough, we consider _ to be alphanumeric.
	 * 
	 * @param token The token to be validated.
	 * @return <code>true</code> if every character in token is alphanumeric or _, and
	 * <code>false</code> otherwise.
	 */
	private boolean validateAlphanumericToken(String token) {
		for (int i = 0; i < token.length(); i++) {
			if (!(Character.isLetterOrDigit(token.charAt(i))) || token.charAt(i) == '_') {
				return false;
			}
		}
		return true;
	}

}
