/*
 * UserManager.java
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

package service.users;

/**
 *
 * @author Adrian Petrescu
 */
public class UserManager {
    
    public static UserManager getUserManager() {
        if (UserManager.singleton == null) {
            UserManager.singleton = new UserManager();
        }
        return UserManager.singleton;
    }
    
    private static UserManager singleton;
    
    public User getUserByUsername(String username) {
        return null;
    }
    
    public User getUserByUid(int userid) {
        return null;
    }
    
    public void registerNewUser(String username, int userid) {
        return;
    }
    
    /** Creates a new instance of UserManager */
    private UserManager() {
    }
    
}
