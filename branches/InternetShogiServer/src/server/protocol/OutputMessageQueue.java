package server.protocol;

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
	 */
	public OutputMessageQueue() {
		super();
		this.messageQueue = new LinkedList<ProtocolMessage>();
	}
	
}
