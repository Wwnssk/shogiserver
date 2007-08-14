/*
 * ClientMessageQueue.java
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

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Adrian Petrescu
 */
public abstract class ClientMessageQueue {
    
    protected Queue<ClientMessage> queue;
    
    /** Creates a new instance of ClientMessageQueue */
    public ClientMessageQueue() {
        this.queue = new LinkedList<ClientMessage>();
    }
    
    public ClientMessageQueue(ClientMessage message) {
        this.queue = new LinkedList<ClientMessage>();
        this.queue.add(message);
    }
    
    public ClientMessageQueue(ClientMessage[] messages) {
        this.queue = new LinkedList<ClientMessage>();
        for (ClientMessage message : messages) {
            this.queue.add(message);
        }
    }
    
    public boolean queueMessage(ClientMessage message) {
        return this.queue.add(message);
    }
    
    public boolean queueMessages(ClientMessage[] messages) {
        for (ClientMessage message : messages) {
            this.queue.add(message);
        }
        return true;
    }
    
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
    
    public int size() {
        return this.queue.size();
    }
    
    abstract public ClientMessage dequeueMessage();
    
}
