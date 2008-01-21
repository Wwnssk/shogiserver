package server.protocol.modules;

import server.protocol.OutputMessageQueue;
import server.protocol.ProtocolMessage;

/**
 * This ProtocolModule implements a simple presence-checker for the client.
 * It will match any string beginning with "ayt" and simply reply with "yes",
 * indicating that the server connection works.
 * 
 * @author Adrian Petrescu
 *
 */
public class AreYouThere implements ProtocolModule {

	public static final String name = "AreYouThere";
	public static final String protocolKey = "ayt";
	public static final String version = "0.1";

	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		return new OutputMessageQueue(new ProtocolMessage("yes"));
	}
	
	public String[] getDependencies() {
		return new String[0];
	}
	
	public String getKey() {
		return protocolKey;
	}
	
	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}
	
}
