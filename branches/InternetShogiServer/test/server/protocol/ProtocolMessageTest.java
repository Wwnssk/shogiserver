package server.protocol;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProtocolMessageTest {

	@Test
	public void testGetProtocolKey() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		assertEquals(emptyMessage.getProtocolKey(), "");
		ProtocolMessage aytMessage = new ProtocolMessage("  ayt  ");
		assertEquals(aytMessage.getProtocolKey(), "ayt");
		aytMessage.append("foo");
		assertEquals(aytMessage.getProtocolKey(), "ayt");
	}

	@Test
	public void testGetPayload() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		assertEquals(emptyMessage.getPayload(), "");
		ProtocolMessage aytMessage = new ProtocolMessage("  ayt  ");
		assertEquals(aytMessage.getPayload(), "");
		aytMessage.append("  foo  ");
		assertEquals(aytMessage.getPayload(), "foo");
		aytMessage.append("bar");
		assertEquals(aytMessage.getPayload(), "foo bar");
		aytMessage.append("   b    a z        ");
		assertEquals(aytMessage.getPayload(), "foo bar b a z");
	}

	@Test
	public void testGetTokenizedPayload() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		assertEquals(emptyMessage.getTokenizedPayload().length, 0);
		ProtocolMessage aytMessage = new ProtocolMessage("ayt  ");
		assertEquals(aytMessage.getTokenizedPayload().length, 0);
		aytMessage.append("  b  a    z  ");
		assertEquals(aytMessage.getTokenizedPayload().length, 3);
		assertEquals(aytMessage.getTokenizedPayload()[0], "b");
		assertEquals(aytMessage.getTokenizedPayload()[1], "a");
		assertEquals(aytMessage.getTokenizedPayload()[2], "z");
	}

	@Test
	public void testGetMessage() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		assertEquals(emptyMessage.getMessage(), "");
		emptyMessage.append("    ");
		assertEquals(emptyMessage.getMessage(), "");
		ProtocolMessage aytMessage = new ProtocolMessage("ayt");
		assertEquals(aytMessage.getMessage(), "ayt");
		aytMessage.append(" foo bar ");
		assertEquals(aytMessage.getMessage(), "ayt foo bar");
		aytMessage.append("  b   a z");
		assertEquals(aytMessage.getMessage(), "ayt foo bar b a z");
	}

	@Test
	public void testAppend() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		emptyMessage.append("            ");
		assertEquals(emptyMessage.getMessage(), "");
		assertEquals(emptyMessage.getTokenizedPayload().length, 0);
		assertEquals(emptyMessage.append("    "), emptyMessage.getMessage());
		ProtocolMessage aytMessage = new ProtocolMessage("ayt");
		assertEquals(aytMessage.append("foo   bar   "), aytMessage.getMessage());
		assertEquals(aytMessage.append("  b a         z"), aytMessage.getMessage());
	}

	@Test
	public void testSetProtocolKey() {
		ProtocolMessage emptyMessage = new ProtocolMessage();
		assertEquals(emptyMessage.setProtocolKey(" foo  "), emptyMessage.getMessage());
		assertEquals(emptyMessage.getProtocolKey(), "foo");
		emptyMessage.setProtocolKey("bar");
		assertEquals(emptyMessage.getProtocolKey(), "bar");
		assertEquals(emptyMessage.getMessage(), "bar");
		assertEquals(emptyMessage.getPayload(), "");
		ProtocolMessage aytMessage = new ProtocolMessage("ayt");
		assertEquals(aytMessage.setProtocolKey("foo"), "foo");
		aytMessage.append("bar");
		assertEquals(aytMessage.setProtocolKey("ayt"), "ayt bar");
	}

}
