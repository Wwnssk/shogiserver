package server.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
		String userQuery = "SELECT * FROM Users WHERE userName = '" + userName + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(userQuery);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
