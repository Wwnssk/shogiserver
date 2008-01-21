package server.protocol.modules;

import java.util.Properties;

import server.protocol.OutputMessageQueue;
import server.protocol.ProtocolMessage;
import server.services.InvalidServiceConfigurationException;

/**
 * ProtocolModules are the building block of the ProtocolMap. Each module may
 * register some string as its "Protocol Key", and all client messages whose
 * first token matches the key will be handed to the corresponding
 * ProtocolModule for processing.
 * 
 * @author Adrian Petrescu
 *
 */
interface ProtocolModule {

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
	
	/**
	 * This method is called when the ProtocolModule is first loaded into the
	 * map. It is guaranteed to be called exactly once for the lifetime of
	 * the ProtocolModule.
	 * 
	 * @param properties A collection of configuration options (possibly empty)
	 * that the ProtocolModule needs in order to function.
	 * 
	 * @throws InvalidProtocolConfigurationException Thrown if the provided
	 * <code>Properties</code> has an invalid configuration set, or is missing
	 * a required setting.
	 */
	void initialize(Properties properties) throws InvalidProtocolConfigurationException;
	
	/**
	 * This method is called when this ProtocolModule is shutdown.. It is guaranteed
	 * that it will only be called once per the lifetime of this service, and that
	 * once it is called, no other messages will need to be parsed.
	 */
	void shutdown();
}
