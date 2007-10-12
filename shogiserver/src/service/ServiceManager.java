/*
 * ServiceManager.java
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

package service;

import service.configuration.ConfigurationManager;
import service.database.DatabaseManager;
import service.users.UserManager;

/**
 *
 * @author Adrian Petrescu
 */
public class ServiceManager {
    
    private static ConfigurationManager configurationManager;
    private static DatabaseManager databaseManager;
    private static UserManager userManager;

    public static ConfigurationManager getConfigurationManager() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            try {
                databaseManager = new DatabaseManager();
            } catch (Exception e) {
                return null;
            }
        }
        return databaseManager;
    }

    public static UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }
    
}
