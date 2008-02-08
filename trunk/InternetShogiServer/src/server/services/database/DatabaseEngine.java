package server.services.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A DatabaseEngine is a backend to a database. It is responsible for the
 * low-level access details that the DatabaseManager needs. This interface
 * should be implemented for each engine (MySQL, PostgreSQL, MSSQL, etc)
 * you plan to use.
 * 
 * @author APetrescu
 *
 */
interface DatabaseEngine {
	
	/**
	 * Establish a connection to the database server. This method will
	 * always be called before any queries are made.
	 * 
	 * @param host The host containing the database server. Use 'localhost'
	 * if the server and database are on the same machine.
	 * @param port The port to connect to the host over. Unless it has been
	 * explicitly set otherwise, the default 3306 should work here.
	 * @param user The user to log in the database with. Should have all
	 * required permissions on the given database, and have access permission
	 * from the server host's location.
	 * @param password The user's password.
	 * @param database The database with the tables needed by the server.
	 * @throws SQLException Thrown if a driver could not be loaded, a 
	 * connection could not be established, the database could not be found,
	 * or the login credentials were not valid.
	 */
	void connect(String host, int port, String user, String password, String database) throws SQLException;
	
	/**
	 * Disconnect from the database server. After this method is called, no
	 * more queries will be made.
	 * 
	 * @throws SQLException A problem occurred disconnecting from the
	 * database server; probably means the connection was lost.
	 */
	void disconnect() throws SQLException;

	/**
	 * Checks if the DatabaseEngine still has a valid connection with the
	 * database.
	 * 
	 * @return <code>true</code> if the connection works, <code>false</code>
	 * otherwise.
	 */
	boolean checkConnected();
	
	/**
	 * Get the row in the Users table corresponding to the given user name.
	 * 
	 * @param userName The user to retrieve information about.
	 * @return A ResultSet with one row containing the given user's information,
	 * or <code>null</code> if it could not be found.
	 */
	public ResultSet getUserRow(String userName);
	
	/**
	 * Get the entire userName column of the Users table.
	 * 
	 * @return A ResultSet with a row containing the userName of each registered
	 * user.
	 */
	public ResultSet getAllUserNames();
	
}