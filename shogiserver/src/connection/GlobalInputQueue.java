/*
 * GlobalInputQueue.java
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

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Adrian Petrescu
 */
public class GlobalInputQueue {
    
    public static GlobalInputQueue getGlobalInputQueue() {
        if (GlobalInputQueue.inputQueue == null) {
            GlobalInputQueue.inputQueue = new GlobalInputQueue();
        }
        return GlobalInputQueue.inputQueue;
    }
    
    private static GlobalInputQueue inputQueue;
    
    private ConcurrentLinkedQueue<ClientMessage> lowPriority;
    private ConcurrentLinkedQueue<ClientMessage> mediumPriority;
    private ConcurrentLinkedQueue<ClientMessage> highPriority;
    
    public ClientMessage getNextMessage() {
        ClientMessage result;
        if ((result = highPriority.poll()) != null)
            return result;
        if ((result = mediumPriority.poll()) != null)
            return result;
        if ((result = lowPriority.poll()) != null)
            return result;
        return null;
    }
    
    public void enqueueMessage(ClientMessage cm) {
        switch(cm.getPriority()) {
            case 0: lowPriority.offer(cm); break;
            case 1: mediumPriority.offer(cm); break;
            case 2: highPriority.offer(cm); break;
            default: lowPriority.offer(cm); break;
        }
    }
    
    public int size() {
        return lowPriority.size() + mediumPriority.size() + highPriority.size();
    }
    
    /** Creates a new instance of GlobalInputQueue */
    private GlobalInputQueue() {
        lowPriority = new ConcurrentLinkedQueue<ClientMessage>();
        mediumPriority = new ConcurrentLinkedQueue<ClientMessage>();
        highPriority = new ConcurrentLinkedQueue<ClientMessage>();
    }
    
}
