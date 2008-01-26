package server.services.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import server.services.protocol.modules.InvalidProtocolConfigurationException;
import server.services.protocol.modules.ProtocolDependenciesNotMetException;
import server.services.protocol.modules.ProtocolModule;

/**
 * The ProtocolMap is the construct in charge of delegating the parsing of input
 * messages to individual ProtocolModules. It is responsible for loading and unloading
 * each such module, and properly sending each ProtocolMessage of a MessageQueue to
 * the appropriate handler.
 * 
 * @author Adrian Petrescu
 *
 */
public class ProtocolMap {

	private ConcurrentHashMap<String, ProtocolModule> protocolMap;
	
	/**
	 * Constructs a new instance of the ProtocolMap.
	 */
	public ProtocolMap() {
		protocolMap = new ConcurrentHashMap<String, ProtocolModule>();
	}
	
	/**
	 * Load a new ProtocolModule into the map. Future input messages that are received with
	 * a matching protocol key will be delegated to this module for parsing.
	 * 
	 * @param module The ProtocolModule to be added to the ProtocolMap.
	 * @param properties A Properties structure containing configuration data for the module
	 * to be loaded. May be empty.
	 * @throws ProtocolDependenciesNotMetException Thrown if any of the given ProtocolModule's 
	 * dependencies are not met.
	 * @throws InvalidProtocolConfigurationException Thrown if the given Properties file contained
	 * a missing or invalid configuration parameter.
	 */
	public void loadProtocolModule(ProtocolModule module, Properties properties) throws ProtocolDependenciesNotMetException, InvalidProtocolConfigurationException {
		Collection<ProtocolModule> loadedModules = protocolMap.values();
		ArrayList<String> unmetDependencies = new ArrayList<String>();
		for(String dependency : module.getDependencies()) {
			String moduleName = dependency.split(" ")[0];
			String moduleVersion = dependency.split(" ")[1];
			boolean dependencyMet = false;
			for (ProtocolModule loadedModule : ((ProtocolModule[]) loadedModules.toArray())) {
				if (loadedModule.getName().equals(moduleName)) {
					String loadedModuleVersion = loadedModule.getVersion();
					if (loadedModuleVersion.charAt(0) > moduleVersion.charAt(0)) {
						dependencyMet = true;
					} else if (loadedModuleVersion.charAt(0) == moduleVersion.charAt(0)
							&& Integer.parseInt(loadedModuleVersion.substring(2)) > Integer.parseInt(moduleVersion.substring(2))) {
						dependencyMet = true;
					}
				}
			}
			
			if (!dependencyMet) {
				unmetDependencies.add(dependency);
			}
		}
		
		if (unmetDependencies.size() > 0) {
			throw new ProtocolDependenciesNotMetException(module.getName(), (String []) unmetDependencies.toArray());
		}
		
		module.initialize(properties);
		protocolMap.put(module.getKey(), module);
	}
	
	
	/**
	 * Parses an entire InputMessageQueue, and returns the server's reply.
	 * If any of the messages received did not have an associated ProtocolModule handler,
	 * then return an <code>invalid</code> message.
	 * 
	 * @param messages An InputMessageQueue of messages from the clients.
	 * @return An OutputMessageQueue of replies for clients.
	 */
	public OutputMessageQueue parseMessages(InputMessageQueue messages) {
		OutputMessageQueue finalOutput = new OutputMessageQueue();
		
		while (!messages.isEmpty()) {
			ProtocolMessage message = messages.dequeue();
			OutputMessageQueue reply;
			if (protocolMap.containsKey(message.getProtocolKey())) {
				reply = protocolMap.get(message.getProtocolKey()).parseMessage(message);
			} else {
				ProtocolMessage invalid = new ProtocolMessage("invalid");
				invalid.append(message.getProtocolKey());
				reply = new OutputMessageQueue(invalid);
			}
			finalOutput.append(reply);
		}
		return finalOutput;
	}
	
	/**
	 * Removes a ProtocolModule from the map. Any future messages received
	 * will not be sent to the given Module, unless a module for
	 * the same ProtocolKey is later loaded back.
	 * 
	 * @param moduleName The name of the ProtocolModule to remove.
	 */
	public void unloadProtocolModule(String moduleName) {
		if (!protocolMap.containsKey(moduleName)) {
			return;
		}
		
		protocolMap.get(moduleName).shutdown();
		protocolMap.remove(moduleName);
	}
	
	/**
	 * This method is called when this ProtocolMap is shutdown.. It is guaranteed
	 * that it will only be called once per the lifetime of this service, and that
	 * once it is called, no other messages will need to be parsed.
	 */
	public void shutdown() {
		Collection<ProtocolModule> loadedModules = protocolMap.values();
		for (ProtocolModule loadedModule : ((ProtocolModule[]) loadedModules.toArray())) {
			unloadProtocolModule(loadedModule.getName());
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		shutdown();
	}
	
}
