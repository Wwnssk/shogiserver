package server.services.connection;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * The global, thread-safe queue where the various parts of
 * 
 * @author Adrian Petrescu
 *
 */
import server.protocol.OutputMessageQueue;

public abstract class GlobalOutputMessageQueue {
	/**
	 * 
	 * @author Adrian Petrescu
	 */
	
	protected Queue<OutputMessageQueue> lowPriorityOutputMessageQueue;
	protected Queue<OutputMessageQueue> medPriorityOutputMessageQueue;
	protected Queue<OutputMessageQueue> highPriorityOutputMessageQueue;
	
	/**
	 * Creates a new instance of GlobalOutputMessageQueue.
	 */
	protected GlobalOutputMessageQueue() {
		lowPriorityOutputMessageQueue = new ConcurrentLinkedQueue<OutputMessageQueue>();
		medPriorityOutputMessageQueue = new ConcurrentLinkedQueue<OutputMessageQueue>();
		highPriorityOutputMessageQueue = new ConcurrentLinkedQueue<OutputMessageQueue>();
	}
	
	/**
	 * Add a message to the global queue.
	 * 
	 * @param message The message to be queued up.
	 */
	protected void enqueue(OutputMessageQueue message) {
		switch (message.getPriority()) {
			case 0: lowPriorityOutputMessageQueue.add(message);
					break;
			case 1: medPriorityOutputMessageQueue.add(message);
					break;
			case 2: medPriorityOutputMessageQueue.add(message);
					break;
		}
	}
	
	/**
	 * @return The oldest message of the highest priority available, or NULL if the
	 * queue is empty.
	 */
	protected OutputMessageQueue dequeue() {
		try {
			highPriorityOutputMessageQueue.remove();
		} catch (NoSuchElementException noHigh) {
			try {
				medPriorityOutputMessageQueue.remove();
			} catch (NoSuchElementException noMed) {
				try {
					lowPriorityOutputMessageQueue.remove();
				} catch(NoSuchElementException noLow) {}
			}
		}
		return null;
	}
	
}

