package server.services.connection;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.protocol.InputMessageQueue;

public abstract class GlobalInputMessageQueue {
	/**
	 * 
	 * 
	 * @author Adrian Petrescu
	 */

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
	protected void enqueue(InputMessageQueue message) {
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
	protected InputMessageQueue dequeue() {
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
	
}
