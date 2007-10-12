/*
 * MySQLDatabaseConnector.java
 * Copyright (C) 2007  Adrian Petrescu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package service.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.text.html.CSS;

import service.ServiceManager;
import service.users.User;

/**
 * 
 * @author Adrian Petrescu
 */
public class MySQLDatabaseConnector implements DatabaseConnector {
    
    private Connection conn;
    
    private static final String ENGINE_NAME = "MySQL";
    
    private static final String USER_TABLE = "Users";
    private static final String USER_COLUMN_USERNAME = "user_name";
    private static final String USER_COLUMN_USERID = "user_id";
    private static final String USER_COLUMN_PASSWORD = "user_password";
    
    public boolean connectToDatabase() {
        return false;
    }
    
    /**
     * Creates a new instance of MySQLDatabaseConnector
     */
    public MySQLDatabaseConnector(String dbHost, int port, String dbUser, String dbPassword, String dbName) throws Exception {
        //TODO: Specify a better exception to be thrown in the event of being unable to instantiate the JDBC driver.
        //TODO: Allow connections on non-default ports, and add that to the JUnit test as well.
        
        String dbUrl = "jdbc:mysql://" + dbHost + "/" + dbName;
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    
    /**
     * Verifies that the given username matches the server's rules for proper names.
     * At the moment, this means they must be alphanumeric, and between 4 and 16
     * characters long. Later, however, the server will also support Unicode names.
     * 
     * It is important that the server does NOT allow spaces in the user names, to
     * prevent SQL injection attacks.
     * @param username The username to be validated.
     * @return Returns <CODE>true</CODE> only if the name fits the server's naming
     * rules.
     */
    private boolean validateUsername(String username) {
        int MAX_USERNAME_LENGTH = 
                Integer.parseInt(ServiceManager.getConfigurationManager().getConfigurationOption("MAX_USERNAME_LENGTH"));
        int MIN_USERNAME_LENGTH =
                Integer.parseInt(ServiceManager.getConfigurationManager().getConfigurationOption("MIN_USERNAME_LENGTH"));
        if (username.length() > MAX_USERNAME_LENGTH || username.length() < MIN_USERNAME_LENGTH) {
            return false;
        }
        
        for (int i = 0;  i < username.length(); i++ ) {
            char c = username.charAt(i);
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                return false;
            }
        }
        
        return true;
    }
    
    public User retrieveUserInfo (String userName) throws DatabaseException {
        if (!validateUsername(userName)) {
            return null;
        }
        
        User user;
        String sqlQuery = "SELECT * FROM " + USER_TABLE + " WHERE user_name = '" + userName + "';";
        Statement userQuery;
        try {
            userQuery = conn.createStatement();
        } catch (SQLException e) {
            throw new DatabaseException(ENGINE_NAME, "Database could not be reached when trying to send query: " 
                    + sqlQuery);
        }
        
        try {
            ResultSet rs = userQuery.executeQuery(sqlQuery);
            if (!rs.next()) {
                return null;
            }
            int userID = rs.getInt(USER_COLUMN_USERID);
            
            user = new User(userName, userID);
        } catch (SQLException e) {
            throw new DatabaseException(ENGINE_NAME, "Database could not be reached when trying to send query: "
                    + sqlQuery);
        }
        return user;
    }
    
    public User retrieveUserInfo (int userID) throws DatabaseException {
        User user;
        String sqlQuery = "SELECT * FROM " + USER_TABLE + " WHERE user_id = '" + userID + "';";
        Statement userQuery;
        try {
            userQuery = conn.createStatement();
        } catch (SQLException e) {
            throw new DatabaseException (ENGINE_NAME, "Database could not be reached when trying to send query: "
                    + sqlQuery);
        }
        
        try {
            ResultSet rs = userQuery.executeQuery(sqlQuery);
            if (!rs.next()) {
                return null;
            }
            String userName = rs.getString(USER_COLUMN_USERNAME);
            
            user = new User(userName, userID);
        } catch (SQLException e) {
            throw new DatabaseException(ENGINE_NAME, "Database could not be reached when trying to send query: "
                    + sqlQuery);
        }
        return user;
    }
    
    public boolean checkConnection() {
        try {
            return conn.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            
        }
    }
    
}
