package server.protocol;

import java.util.Queue;


/**
 * Represents an ordered collection of ProtocolMessages that are intended as input
 * or output for the server. Messages in a MessageQueue are guaranteed to be parsed
 * consecutively in exactly the order they were added to the queue, without any
 * other instructions in between. It is the typical input unit that is passed
 * around within the server. A MessageQueue should be used even if only
 * a single ProtocolMessage needs to be passed around.
 * 
 * @author Adrian Petrescu
 *
 */
public abstract class MessageQueue {
	
	protected Queue<ProtocolMessage> messageQueue;
	
	/**
	 * Returns <code>true</code> if the MessageQueue has no more elements.
	 * 
	 * @return <code>true</code> if the MessageQueue has no more elements.
	 */
	public boolean isEmpty() {
		return messageQueue.isEmpty();
	}
	
	/**
	 * Returns the number of elements in this MessageQueue.
	 * 
	 * @return the number of elements in this MessageQueue.
	 */
	public int getSize() {
		return messageQueue.size();
	}
	
	/**
	 * Retrieves and removes the next ProtocolMessage in this queue, or null
	 * if this MessageQueue is empty.
	 * 
	 * @return The next ProtocolMessage in this queue, or null if it is empty.
	 */
	public ProtocolMessage dequeue() {
		return messageQueue.poll();
	}
}
