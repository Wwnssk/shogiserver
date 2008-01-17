package server.protocol;

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
	 * Creates a new InputMessageQueue with no messages at all.
	 */
	public InputMessageQueue() {
		super();
		this.messageQueue = new LinkedList<ProtocolMessage>();
	}
	
}
