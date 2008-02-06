package server.protocol.modules;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import server.services.protocol.ProtocolMessage;
import server.services.protocol.modules.AreYouThere;
import server.services.protocol.modules.InvalidProtocolConfigurationException;
import server.services.protocol.modules.ProtocolModule;

public class AreYouThereTest {

	@Test
	public void testParseMessage() {
		ProtocolModule areYouThereModule = new AreYouThere();
		try {
			areYouThereModule.initialize(new Properties());
		} catch (InvalidProtocolConfigurationException e) {}
		ProtocolMessage normalInput = new ProtocolMessage(areYouThereModule
				.getKey());
		assertEquals("yes", areYouThereModule.parseMessage(normalInput)
				.dequeue().getMessage());

		ProtocolMessage abnormalInput = new ProtocolMessage(areYouThereModule
				.getKey());
		abnormalInput.append("foo bar");
		assertEquals("yes", areYouThereModule.parseMessage(abnormalInput)
				.dequeue().getMessage());
	}

}
