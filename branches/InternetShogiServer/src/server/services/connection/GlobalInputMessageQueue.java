package server.services.connection;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.services.protocol.InputMessageQueue;

/**
 * A threadsafe global queue of all the messages received by all clients
 * connected to the server. The server is constantly processing this queue.
 * 
 * The queue uses Round-Robin scheduling; that is, messages of higher priority
 * are always dequeued before messages of lower priority, and it follows a FIFO
 * scheme for messages with the same priority.
 * 
 * @author Adrian Petrescu
 */
public class GlobalInputMessageQueue {

	private static GlobalInputMessageQueue self;
	/**
	 * Singleton accessor to the GlobalInputMessageQueue. If the queue has not
	 * yet been accessed, it will be created.
	 * 
	 * @return A reference to the GlobalInputMessageQueue.
	 */
	public static GlobalInputMessageQueue getGlobalInputMessageQueue() {
		if (self == null) {
			self = new GlobalInputMessageQueue();
		}
		return self;
	}
	
	protected Queue<InputMessageQueue> lowPriorityInputMessageQueue;
	protected Queue<InputMessageQueue> medPriorityInputMessageQueue;
	protected Queue<InputMessageQueue> highPriorityInputMessageQueue;
	
	/**
	 * Creates a new instance of GlobalInputMessageQueue.
	 */
	protected GlobalInputMessageQueue() {
		lowPriorityInputMessageQueue = new ConcurrentLinkedQueue<InputMessageQueue>();
		medPriorityInputMessageQueue = new ConcurrentLinkedQueue<InputMessageQueue>();
		highPriorityInputMessageQueue = new ConcurrentLinkedQueue<InputMessageQueue>();
	}
	
	/**
	 * Add a message to the global queue. 
	 * 
	 * @param message The message to be queued up.
	 */
	protected synchronized void enqueue(InputMessageQueue message) {
		switch (message.getPriority()) {
			case 0: lowPriorityInputMessageQueue.add(message);
					break;
			case 1: medPriorityInputMessageQueue.add(message);
					break;
			case 2: medPriorityInputMessageQueue.add(message);
					break;
		}
	}
	
	/**
	 * @return The oldest message of the highest priority available, or NULL if the
	 * queue is empty.
	 */
	protected synchronized InputMessageQueue dequeue() {
		try {
			highPriorityInputMessageQueue.remove();
		} catch (NoSuchElementException noHigh) {
			try {
				medPriorityInputMessageQueue.remove();
			} catch (NoSuchElementException noMed) {
				try {
					lowPriorityInputMessageQueue.remove();
				} catch(NoSuchElementException noLow) {}
			}
		}
		return null;
	}
	
	/**
	 * Return the total number of input message queues still queued up
	 * in the global buffer, of all priorities.
	 * 
	 * @return The total number of queued InputMessageStacks.
	 */
	protected int getSize() {
		return lowPriorityInputMessageQueue.size() +
				medPriorityInputMessageQueue.size() +
				highPriorityInputMessageQueue.size();
	}
	
	/**
	 * Return the total number of input message queues still in the queue
	 * with a given priority.
	 * 
	 * @param priority The priority level of counted messages.
	 * @return The total number of queued InputMessageStacks of the given
	 * priority.
	 */
	protected int getSize(int priority) {
		switch (priority) {
			case 0: return lowPriorityInputMessageQueue.size();
			case 1: return medPriorityInputMessageQueue.size();
			case 2: return highPriorityInputMessageQueue.size();
			default: return 0;
		}
	}
	
}
