/*
 * MySQLDatabaseConnectionTest.java
 * JUnit based test
 *
 * Created on August 21, 2007, 8:36 PM
 */

package service.database;

import junit.framework.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import service.users.User;

/**
 *
 * @author Adrian Petrescu
 */
public class MySQLDatabaseConnectionTest extends TestCase {
    
    private static final String dbHost = "localhost";
    private static final String dbUser = "shogi_test";
    private static final String dbPass = "shogi";
    private static final int dbPort = 3306;
    private static final String dbName = "shogi_test_db";
    private static final String pathToTestSchema = "sql/schemas/shogi_test_db.sql";
    
    public MySQLDatabaseConnectionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        // Start connection and load schema
        
        String dbUrl = "jdbc:mysql://" + dbHost + "/";
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        Statement stmt = conn.createStatement();
        
        BufferedReader in = new BufferedReader(new FileReader(new File(pathToTestSchema)));
        String sqlQuery = "";
        String nextLine = "";
        while ((nextLine = in.readLine()) != null) {
            sqlQuery += nextLine;
            if (!sqlQuery.isEmpty()) {
                if (sqlQuery.endsWith(";")) {
                    stmt.execute(sqlQuery);
                    System.out.println(sqlQuery);
                    sqlQuery = "";
                } else if (sqlQuery.startsWith("--")) {
                    sqlQuery = "";
                }
            }
        }
        
        in.close();
        conn.close();
    }

    protected void tearDown() {
    }
    
    /**
     * Test of checkConnection method, of class service.database.MySQLDatabaseConnector.
     */
    public void checkCheckConnection() throws Exception {
        System.out.println("checkConnection");
        
        MySQLDatabaseConnector instance = new MySQLDatabaseConnector(dbHost, dbPort, dbUser, dbPass, dbName);
        assertTrue(instance.checkConnection());
        instance.closeConnection();
        assertFalse(instance.checkConnection());
    }

    /**
     * Test of retrieveUserInfo method, of class service.database.MySQLDatabaseConnector.
     */
    public void testRetrieveUserInfo() throws Exception {
        System.out.println("getUserByName");
        
        User user;
        String username;
        MySQLDatabaseConnector instance;
        
        
        // First we'll test a user that IS present in the test database.
        username = "admin";
        instance = new MySQLDatabaseConnector(dbHost, dbPort, dbUser, dbPass, dbName);
        user = instance.retrieveUserInfo(username);
        assertEquals(user.getUserid(), 1);
        assertEquals(user.getUsername(), "admin");
        instance.closeConnection();
        
        // Now we'll test a user we know is NOT present in the test database.
        username = "rumpelstiltskin";
        instance = new MySQLDatabaseConnector(dbHost, dbPort, dbUser, dbPass, dbName);
        user = instance.retrieveUserInfo(username);
        assertNull(user);
        
        // Now we'll validate a few users whose names aren't even legal.
        username = "no spaces";
        instance = new MySQLDatabaseConnector(dbHost, dbPort, dbUser, dbPass, dbName);
        user = instance.retrieveUserInfo(username);
        assertNull(user);
        
        
        
    }
    
}
