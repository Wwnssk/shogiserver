package server.services.protocol;

import java.util.LinkedList;

/**
 * Represents an ordered collection of ProtocolMessages that are intended as output
 * for the server. Messages in an OutputMessageQueue are guaranteed to be parsed
 * consecutively in exactly the order they were added to the queue, without any
 * other instructions in between. It is the typical input unit that is passed
 * around within the server. An OutputMessageQueue should be used even if only
 * a single ProtocolMessage needs to be passed around.
 * 
 * @author Adrian Petrescu
 *
 */
public class OutputMessageQueue extends MessageQueue {
	

	/**
	 * Creates a new OutputMessageQueue with no messages at all.
	 * The priority defaults to 1.
	 */
	public OutputMessageQueue() {
		super();
		this.messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(1);
	}
	
	/**
	 * Creates a new OutputMessageQueue containing a single ProtocolMessage.
	 * @param message The first ProtocolMessage in this queue. The priority
	 * defaults to 1.
	 */
	public OutputMessageQueue(ProtocolMessage message) {
		super();
		this.messageQueue = new LinkedList<ProtocolMessage>();
		messageQueue.add(message);
		setPriority(1);
	}
	
	/**
	 * Creates a new OutputMessageQueue with no messages at all.
	 * 
	 * @param priority The priority value for this OutputMessageQueue. 
	 */
	public OutputMessageQueue(int priority) {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(priority);
	}
	
	/**
	 * Creates a new OutputMessageQueue containing a single ProtocolMessage.
	 * 
	 * @param message The first ProtocolMessage in this queue.
	 * @param priority The priority value for this OutputMessageQueue.
	 */
	public OutputMessageQueue(ProtocolMessage message, int priority) {
		super();
		messageQueue = new LinkedList<ProtocolMessage>();
		setPriority(priority);
		messageQueue.add(message);
	}
	
}
