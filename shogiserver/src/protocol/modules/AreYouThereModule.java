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
import connection.ClientInputMessageQueue;
import connection.ClientOutputMessageQueue;

/**
 *
 * @author Adrian Petrescu
 */
public class AreYouThereModule implements ProtocolModule {
    
    private static final String name = "Are You There";
    private static final String author = "Adrian Petrescu";
    private static final String version = "0.1";
    private static final String key = "ayt";
    private static final String[] dependencies = new String[0];
    
    /** Creates a new instance of AreYouThereModule */
    public AreYouThereModule() {
    }
    
    public ClientOutputMessageQueue parseClientMessage(ClientInputMessageQueue in) throws UnrecognizedProtocolException {
        ClientOutputMessageQueue out = new ClientOutputMessageQueue();
        if (in.size() != 1) {
            throw new UnrecognizedProtocolException(in, "There should only be one message in the queue.");
        }
        ClientInputMessage message = in.dequeueMessage();
        if (!message.getMessage().trim().equals("ayt")) {
            throw new UnrecognizedProtocolException(in, "There should be no arguments to ayt");
        }
        out.queueMessage(new ClientOutputMessage(message.getUser(), "yes"));
        return out;
    }
    
    public String[] getDependencies() {
        return AreYouThereModule.dependencies;
    }
    
    public String getName() {
        return AreYouThereModule.name;
    }
    
    public String getVersion() {
        return AreYouThereModule.version;
    }
    
    public String getKey() {
        return AreYouThereModule.key;
    }
    
}
