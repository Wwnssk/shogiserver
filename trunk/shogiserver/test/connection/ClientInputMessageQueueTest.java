/*
 * ClientInputMessageQueueTest.java
 * JUnit based test
 *
 * Created on August 14, 2007, 9:42 AM
 */

package connection;

import junit.framework.*;
import java.util.NoSuchElementException;
import service.users.User;

/**
 *
 * @author Adrian Petrescu
 */
public class ClientInputMessageQueueTest extends TestCase {
    
    public ClientInputMessageQueueTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of queueMessage method, of class connection.ClientInputMessageQueue.
     */
    public void testQueueMessage() {
        System.out.println("queueMessage");
        final int NUM_OF_ELEMENTS = 5000;
        ClientMessageQueue instance = new ClientInputMessageQueue();
        for (int i = 0; i < NUM_OF_ELEMENTS; i+=2) {
            ClientMessage a = new ClientInputMessage(null, String.valueOf(i));
            ClientInputMessage b = new ClientInputMessage(null, String.valueOf(i));
            instance.queueMessage(a);
            instance.queueMessage(b);
        }
        assertEquals(instance.size(), NUM_OF_ELEMENTS);
        ClientMessage[] messageArray = new ClientMessage[NUM_OF_ELEMENTS];
        for (int i = 0; i < NUM_OF_ELEMENTS; i++) {
            messageArray[i] = instance.dequeueMessage();
        }
        assertEquals(instance.size(), 0);
        instance.queueMessages(messageArray);
        assertEquals(instance.size(), NUM_OF_ELEMENTS);
    }
    
    /**
     * Test of dequeueMessage method, of class connection.ClientInputMessageQueue.
     */
    public void testDequeueMessage() {
        System.out.println("dequeueMessage");
        
        ClientInputMessageQueue instance = new ClientInputMessageQueue();
        
        ClientMessage a = new ClientInputMessage(null, "test-a");
        ClientMessage b = new ClientInputMessage(new User(), "test-b");
        ClientMessage c = new ClientInputMessage(new User(), "test-c");
        ClientMessage[] messageArray = new ClientMessage[2];
        messageArray[0] = b;
        messageArray[1] = c;
        
        instance.queueMessage(a);
        instance.queueMessages(messageArray);
        assertEquals(instance.dequeueMessage(), a);
        assertEquals(instance.dequeueMessage(), b);
        instance.queueMessage(a);
        assertEquals(instance.dequeueMessage(), c);
        assertFalse(instance.isEmpty());
        assertEquals(instance.dequeueMessage(), a);
        assertTrue(instance.isEmpty());
        boolean exceptionThrown = false;
        try {
            instance.dequeueMessage();
        } catch (NoSuchElementException e) {
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }
    
}
