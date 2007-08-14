/*
 * AreYouThereModuleTest.java
 * JUnit based test
 *
 * Created on August 14, 2007, 12:26 PM
 */

package protocol.modules;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import junit.framework.*;
import connection.ClientInputMessage;
import connection.ClientOutputMessage;
import connection.ClientInputMessageQueue;
import connection.ClientOutputMessageQueue;
import service.users.User;

/**
 *
 * @author apetrescu
 */
public class AreYouThereModuleTest extends TestCase {
    
    public AreYouThereModuleTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of parseClientMessage method, of class protocol.modules.AreYouThereModule.
     */
    public void testParseClientMessage() throws Exception {
        System.out.println("parseClientMessage");
        ClientInputMessageQueue in;
        ClientInputMessage inMessage;
        AreYouThereModule instance;
        User sender;
        ClientOutputMessageQueue out;
        ClientOutputMessage outMessage;
        boolean exceptionThrown;
        
        // Testing perfect input.
        in = new ClientInputMessageQueue();
        instance = new AreYouThereModule();
        sender = new User();
        in.queueMessage(new ClientInputMessage(sender, "ayt"));
        out = instance.parseClientMessage(in);
        assertEquals(out.size(), 1);
        outMessage = out.dequeueMessage();
        assertEquals(outMessage.getUser(), sender);
        assertEquals(outMessage.getMessage(), "yes");

        // Testing good input, but with superflous whitespace.
        in = new ClientInputMessageQueue();
        instance = new AreYouThereModule();
        sender = new User();
        in.queueMessage(new ClientInputMessage(sender, "ayt  "));
        out = instance.parseClientMessage(in);
        assertEquals(out.size(), 1);
        outMessage = out.dequeueMessage();
        assertEquals(outMessage.getUser(), sender);
        assertEquals(outMessage.getMessage(), "yes");
        
        // Testing bad input, wrong size
        in = new ClientInputMessageQueue();
        instance = new AreYouThereModule();
        sender = new User();
        in.queueMessage(new ClientInputMessage(sender, "ayt  "));
        in.queueMessage(new ClientInputMessage(sender, "ayt"));
        exceptionThrown = false;
        try {
            out = instance.parseClientMessage(in);
        } catch (UnrecognizedProtocolException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        // Testing bad input, too many arguments
        in = new ClientInputMessageQueue();
        instance = new AreYouThereModule();
        sender = new User();
        in.queueMessage(new ClientInputMessage(sender, "ayt are you there"));
        exceptionThrown = false;
        try {
            out = instance.parseClientMessage(in);
        } catch (UnrecognizedProtocolException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
}
