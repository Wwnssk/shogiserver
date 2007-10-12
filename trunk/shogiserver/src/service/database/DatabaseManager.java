/*
 * DatabaseManager.java
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

import service.ServiceManager;
import service.users.User;

/**
 *
 * @author Adrian Petrescu
 */
public class DatabaseManager {
    
    private String databaseEngine;
    private DatabaseConnector databaseConnector;
    
    /** Creates a new instance of DatabaseManager */
    public DatabaseManager() throws Exception {
        // TODO: Improve the exception handling here, throw a more descriptive exception
        databaseEngine = ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_ENGINE");
        if (databaseEngine.equals("MySQL")) {
            String dbHost = ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_HOST");
            int dbPort = Integer.parseInt(ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_PORT"));
            String dbUser = ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_USER");
            String dbPass = ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_PASS");
            String dbName = ServiceManager.getConfigurationManager().getConfigurationOption("DATABASE_NAME");
            databaseConnector = new MySQLDatabaseConnector(dbHost, dbPort, dbUser, dbPass, dbName);
            databaseConnector.connectToDatabase();
            if (!databaseConnector.checkConnection()) {
                throw new Exception();
            }
        }
    }
    
    public User retrieveUserInfo(String userName) throws DatabaseException {
        try{
            return databaseConnector.retrieveUserInfo(userName);
        } catch (DatabaseException e) {
            if (databaseConnector.checkConnection()) {
                throw e;
            } else {
                databaseConnector.connectToDatabase();
                return databaseConnector.retrieveUserInfo(userName);
            }
        }
    }
    
}
