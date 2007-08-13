/*
 * ProtocolModule.java
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

package protocol.modules;

import connection.ClientInputMessage;
import connection.ClientOutputMessage;

/**
 *
 * @author Adrian Petrescu
 */
public interface ProtocolModule {
    /**
     * Returns the string representing the first word of all received messages
     * that should be relayed to this protocol module.
     * @return The protocol key.
     */
    public String getKey();
    
    public String getVersion();
    
    public String getName();
    
    public String[] getDependencies();
    
}
