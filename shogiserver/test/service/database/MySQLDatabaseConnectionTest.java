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
        while ((sqlQuery += in.readLine()) != null) {
            if (!sqlQuery.isEmpty()) {
                if (sqlQuery.endsWith(";")) {
                    stmt.execute(sqlQuery);
                    System.out.println(sqlQuery);
                    sqlQuery = "";
                } 
            }
        }
        
        in.close();
        conn.close();
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getUserByName method, of class service.database.MySQLDatabaseConnection.
     */
    public void testGetUserByName() {
        System.out.println("getUserByName");
        
        String userName = "";
        MySQLDatabaseConnection instance = null;
        
        String expResult = "";
        //String result = instance.getUserByName(userName);
        //assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
