package server.protocol.modules;

import server.protocol.OutputMessageQueue;
import server.protocol.ProtocolMessage;

/**
 * ProtocolModules are the building block of the ProtocolMap. Each module may
 * register some string as its "Protocol Key", and all client messages whose
 * first token matches the key will be handed to the corresponding
 * ProtocolModule for processing.
 * 
 * @author Adrian Petrescu
 *
 */
public interface ProtocolModule {

	/**
	 * Every ProtocolModule may register a string which denotes which
	 * types of messages it can handle.
	 * 
	 * @return The key which this ProtocolModule handles.
	 */
	public String getKey();
	
	/**
	 * The version number of this ProtocolModule. It should be in the form
	 * x.yz, where x,y,z are digits from 0-9. Larger numeric values indicate
	 * more recent versions.
	 * 
	 * @return This ProtocolModule's version string.
	 */
	public String getVersion();
	
	/**
	 * The unique string identifying this ProtocolModule to the system.
	 * Other modules will use this name when declaring dependencies.
	 * The name should be alphabetic only, of any length, with no spaces.
	 * 
	 * @return A string uniquely identifying this ProtocolModule.
	 */
	public String getName();
	
	/**
	 * Each ProtocolModule may have a list of other module dependencies which
	 * they rely on. They should be given here as an array of Strings. Each
	 * entry should be of the form ModuleName + " " + ModuleVersion. A
	 * ProtocolModule will be loaded only if all its dependencies of the
	 * required version or higher are met.
	 * 
	 * @return An array of dependency strings.
	 */
	public String[] getDependencies();
	
	/**
	 * This method is called when the most recently-dequeued message for the
	 * server was determined to be aimed for this ProtocolModule.
	 * 
	 * @param message The message received from the client.
	 * @return An OutputMessageQueue of messages to be sent to the client.
	 */
	public OutputMessageQueue parseMessage(ProtocolMessage message);
	
}
