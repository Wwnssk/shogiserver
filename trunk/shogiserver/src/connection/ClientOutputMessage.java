/*
 * ClientOutputMessage.java
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
public class ClientOutputMessage extends ClientMessage {
    
    /** Creates a new instance of ClientOutputMessage */
    public ClientOutputMessage(User targetUser, String message) {
        super(targetUser, message);
    }
    
    public ClientOutputMessage(User targetUser, String message, int priority) {
        super(targetUser, message, priority);
    }
    
}
