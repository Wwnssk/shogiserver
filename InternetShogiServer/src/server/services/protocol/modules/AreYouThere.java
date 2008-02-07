package server.services.protocol.modules;

import java.util.Properties;

import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;

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
	public static final String version = "0.01";

	/**
	 * An <code>ayt</code> message should not contain any parameters. A simply
	 * "ayt" will be responded to with a simple "yes".
	 * 
	 * <br>
	 * <b>Expected syntax:</b>
	 * <br>
	 * <code><i>ayt</i></code>
	 * 
	 * <br>
	 * <b>Output syntax:</b>
	 * <br>
	 * <code><i>yes</i></code>
	 */
	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		return new OutputMessageQueue(new ProtocolMessage(message.getUser(), "yes"));
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
	
	public void initialize(Properties properties) {
		
	}
	
	public void shutdown() {
		
	}
	
}
