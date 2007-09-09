/*
 * DatabaseException.java
 *
 * Created on September 8, 2007, 3:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package service.database;

/**
 *
 * @author Adrian Petrescu
 */
public class DatabaseException extends java.lang.Exception {
    
    private String engine;
    
    /**
     * Creates a new instance of <code>DatabaseException</code> without detail message.
     */
    public DatabaseException(String engine) {
        this.engine = engine;
    }
    
    /**
     * Constructs an instance of <code>DatabaseException</code> with the specified detail message.
     * @param engine the database engine being used when the exception was raised.
     * @param msg the detail message.
     */
    public DatabaseException(String engine, String msg) {
        super(msg);
        this.engine = engine;
    }

    public String getEngine() {
        return engine;
    }
}
