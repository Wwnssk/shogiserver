package server.services.database;

import java.sql.ResultSet;
import java.sql.SQLException;

interface DatabaseEngine {
	void connect(String host, int port, String user, String password, String database) throws SQLException;
	void disconnect() throws SQLException;
	public ResultSet getUserRow(String userName);
	boolean checkConnected();
}