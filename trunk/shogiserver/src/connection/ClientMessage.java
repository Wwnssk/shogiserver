/*
 * ClientMessage.java
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

package connection;

import service.users.User;

/**
 *
 * @author Adrian Petrescu
 */
public class ClientMessage {
    
    private User user;
    private String message;
    private int priority;
    
    /** Creates a new instance of ClientMessage */
    public ClientMessage(User user, String message) {
        this.user = user;
        this.message = message;
        this.priority = 0;
    }
    
    public ClientMessage(User user, String message, int priority) {
        this.user = user;
        this.message = message;
        this.priority = priority;
    }

    public User getUser() {
        return this.user;
    }

    public String getMessage() {
        return this.message;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
}
