package server.services.protocol;

import java.util.LinkedList;

/**
 * Represents an ordered collection of ProtocolMessages that are intended as input
 * for the server. Messages in an InputMessageQueue are guaranteed to be parsed
 * consecutively in exactly the order they were added to the queue, without any
 * other instructions in between. It is the typical input unit that is passed
 * around within the server. An InputMessageQueue should be used even if only
 * a single ProtocolMessage needs to be passed around.
 * 
 * @author Adrian Petrescu
 *
 */
public class InputMessageQueue extends MessageQueue {
	

	/**
	 * Creates a new InputMessageQueue with no messages at all. The priority
	 * defaults to 1.
	 */
	public InputMessageQueue() {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(1);
	}
	
	/**
	 * Creates a new InputMessageQueue containing a single ProtocolMessage.
	 * The priority defaults to 1.
	 * @param message The first ProtocolMessage in this queue.
	 */
	public InputMessageQueue(ProtocolMessage message) {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(1);
		messageQueue.add(message);
	}
	
	/**
	 * Creates a new InputMessageQueue with no messages at all.
	 * 
	 * @param priority The priority value for this InputMessageQueue. 
	 */
	public InputMessageQueue(int priority) {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(priority);
	}
	
	/**
	 * Creates a new InputMessageQueue containing a single ProtocolMessage.
	 * 
	 * @param message The first ProtocolMessage in this queue.
	 * @param priority The priority value for this InputMessageQueue.
	 */
	public InputMessageQueue(ProtocolMessage message, int priority) {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(priority);
		messageQueue.add(message);
	}
	
}
