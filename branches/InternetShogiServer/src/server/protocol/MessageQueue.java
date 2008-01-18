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
	protected int priority;
	
	/**
	 * The largest priority a MessageQueue can have on the server. If a
	 * MessageQueue's priority is set to a value higher than MAX_PRIORITY,
	 * it will default to MAX_PRIORITY.
	 */
	public static final int MAX_PRIORITY = 3;
	
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

	/**
	 * Sets the priority of this Message Queue. The priority of a MessageQueue 
	 * is an integer between 0 and <code>MessageQueue.MAX_PRIORITY</code>. The 
	 * higher the priority value, the sooner the MessageQueue will be processed
	 * by the main server. It is guaranteed that no message of priority 
	 * <code>n</code> will be processed until all messages of priority <code>n+1
	 * </code> have already been processed.
	 * 
	 * If a MessageQueue's priority is set to a value higher than MAX_PRIORITY,
	 * it will default to MAX_PRIORITY. If a MessageQueue's priority is set to
	 * a value less than 0, it will default to 0.
	 * 
	 * @param priority The new priority of this MessageQueue.
	 */
	protected void setPriority(int priority) {
		this.priority = Math.min(Math.max(priority, 0), MAX_PRIORITY);
	}

	/**
	 * Gets the priority of this Message Queue. The priority of a MessageQueue 
	 * is an integer between 0 and <code>MessageQueue.MAX_PRIORITY</code>. The 
	 * higher the priority value, the sooner the MessageQueue will be processed
	 * by the main server. It is guaranteed that no message of priority 
	 * <code>n</code> will be processed until all messages of priority <code>n+1
	 * </code> have already been processed.
	 * 
	 * @return The priority of this MessageQueue.
	 */
	protected int getPriority() {
		return priority;
	}
}
