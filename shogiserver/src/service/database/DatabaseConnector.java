/*
 * ConnectionManager.java
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

import service.users.User;

/**
 *
 * @author Adrian Petrescu
 */
public interface DatabaseConnector {
    
    public boolean checkConnection();
    public void closeConnection();
    public boolean connectToDatabase() throws DatabaseException;
    public User retrieveUserInfo(String userName) throws DatabaseException;
    public User retrieveUserInfo(int userID) throws DatabaseException;
    
}
