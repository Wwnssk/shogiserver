package server.protocol;

import static org.junit.Assert.*;

import org.junit.Test;

import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;

public class OutputMessageQueueTest {
	
	@Test
	public void testIsEmpty() {
		OutputMessageQueue emptyQueue = new OutputMessageQueue();
		assertTrue(emptyQueue.isEmpty());
		emptyQueue.enqueue(new ProtocolMessage("foo"));
		assertFalse(emptyQueue.isEmpty());
		assertFalse(new OutputMessageQueue(new ProtocolMessage("ayt")).isEmpty());
	}

	@Test
	public void testGetSize() {
		OutputMessageQueue testQueue = new OutputMessageQueue();
		assertEquals(0, testQueue.getSize());
		for (int i = 1; i <= 10; i++) {
			testQueue.enqueue(new ProtocolMessage("foo"));
			assertEquals(i, testQueue.getSize());
		}
		
		for (int i = 9; i >= 0; i--) {
			testQueue.dequeue();
			assertEquals(i, testQueue.getSize());
		}
	}

	@Test
	public void testEnqueue() {
		OutputMessageQueue testQueue = new OutputMessageQueue();
		testQueue.enqueue(new ProtocolMessage("1"));
		assertEquals(1, testQueue.getSize());
		for (int i = 2; i <= 10; i++) {
			testQueue.enqueue(new ProtocolMessage(String.valueOf(i)));
			assertEquals(i, testQueue.getSize());
		}
	
		for (int i = 9; i >= 0; i--) {
			assertEquals(String.valueOf(10-i), testQueue.dequeue().getMessage());
			assertEquals(i, testQueue.getSize());
		}
		
		testQueue.enqueue(new ProtocolMessage("foo"));
		testQueue.enqueue(new ProtocolMessage("bar"));
		testQueue.dequeue();
		testQueue.enqueue(new ProtocolMessage("foo"));
		assertEquals("bar", testQueue.dequeue().getMessage());
		assertEquals("foo", testQueue.dequeue().getMessage());
	}
	
	@Test
	public void testDequeue() {
		OutputMessageQueue testQueue = new OutputMessageQueue();
		testQueue.enqueue(new ProtocolMessage("1"));
		assertEquals(1, testQueue.getSize());
		for (int i = 2; i <= 10; i++) {
			testQueue.enqueue(new ProtocolMessage(String.valueOf(i)));
			assertEquals(i, testQueue.getSize());
		}
	
		for (int i = 9; i >= 0; i--) {
			assertEquals(String.valueOf(10-i), testQueue.dequeue().getMessage());
			assertEquals(i, testQueue.getSize());
		}
		
		testQueue.enqueue(new ProtocolMessage("foo"));
		testQueue.enqueue(new ProtocolMessage("bar"));
		testQueue.dequeue();
		testQueue.enqueue(new ProtocolMessage("foo"));
		assertEquals("bar", testQueue.dequeue().getMessage());
		assertEquals("foo", testQueue.dequeue().getMessage());
	}

	@Test
	public void testPriority() {
		OutputMessageQueue testQueueNoPriority = new OutputMessageQueue();
		assertEquals(1, testQueueNoPriority.getPriority());
		
		OutputMessageQueue testQueue = new OutputMessageQueue(OutputMessageQueue.MAX_PRIORITY + 1);
		assertEquals(OutputMessageQueue.MAX_PRIORITY, testQueue.getPriority());
		testQueue.setPriority(-10);
		assertEquals(0, testQueue.getPriority());
		for (int i = 0; i <= OutputMessageQueue.MAX_PRIORITY; i++) {
			testQueue.setPriority(i);
			assertEquals(i, testQueue.getPriority());
		}
	}

}
