package server.services.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import server.main.GlobalOutputMessageQueue;
import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;
import server.services.protocol.modules.AreYouThere;
import server.services.protocol.modules.InvalidProtocolConfigurationException;
import server.services.protocol.modules.MessageOfTheDay;
import server.services.protocol.modules.ProtocolDependenciesNotMetException;
import server.services.protocol.modules.ProtocolModule;
import server.services.protocol.modules.Tell;

/**
 * This GlobalService is one of the core services of the system. It
 * maintains the protocol map, which is responsible for choosing
 * which ProtocolModule will handle a given input message, and parsing
 * it.
 * 
 * @author Adrian Petrescu
 *
 */
public class ProtocolManager implements GlobalService {

	public static final String SERVICE_NAME = "ProtocolManager";
	private static final String configFileSuffix = "_configFile";

	private ProtocolMap protocolMap;

	@Override
	public String getIdentifier() {
		return SERVICE_NAME;
	}

	/**
	 * Loads a new ProtocolModule into the protocol map. This may include
	 * parsing a module's individual configuration file.
	 * 
	 * @param module The ProtocolModule to be loaded.
	 * @param properties A property structure containing a <i>module</i>_confFile,
	 * where <i>module</i> is the given module's protocol key, if it is required.
	 * @throws FileNotFoundException Thrown if the configuration file was given,
	 * but was not found on the filesystem.
	 * @throws IOException Thrown if the configuration file was given, but could
	 * not be read from the filesystem.
	 * @throws ProtocolDependenciesNotMetException Thrown if the given module
	 * depends on a ProtocolModule that has not yet been loaded.
	 * @throws InvalidProtocolConfigurationException Thrown if the given configuration
	 * file exists and can be read, but does not contain a valid configuration for
	 * this ProtocolModule.
	 */
	private void loadProtocolModule(ProtocolModule module, Properties properties)
			throws FileNotFoundException, IOException,
			ProtocolDependenciesNotMetException,
			InvalidProtocolConfigurationException {
		Properties moduleProperties = new Properties();
		if (properties.containsKey(module.getKey() + configFileSuffix)) {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					(String) properties
							.get(module.getKey() + configFileSuffix))));
			moduleProperties.load(in);
			in.close();
		}

		protocolMap.loadProtocolModule(module, moduleProperties);
	}

	/**
	 * Called when the server is ready to start loading ProtocolModules and
	 * parsing messages.
	 * 
	 * @param properties A Properties structure containing configuration data
	 * for the ProtocolManager.
	 * <br>
	 * <b>Required configuration options:</b>
	 * <br>
	 * <i>module</i>_confFile - A path to a module-specific configuration
	 * file, for any modules that may require it.
	 */
	@Override
	public void initialize(Properties properties)
			throws InvalidServiceConfigurationException {
		// TODO: Load 3rd-party modules.

		protocolMap = new ProtocolMap();

		/* Load AreYouThere module */
		ProtocolModule ayt = new AreYouThere();
		try {
			loadProtocolModule(ayt, properties);
		} catch (Throwable e) {
			throw new InvalidServiceConfigurationException(SERVICE_NAME,
					properties, ayt.getKey());
		}

		/* Load Tell module */
		ProtocolModule tell = new Tell();
		try {
			loadProtocolModule(tell, properties);
		} catch (Throwable e) {
			throw new InvalidServiceConfigurationException(SERVICE_NAME,
					properties, tell.getKey());
		}
		
		/* Load MessageOfTheDay module */
		ProtocolModule motd = new MessageOfTheDay();
		try {
			loadProtocolModule(motd, properties);
		} catch (Throwable e) {
			throw new InvalidServiceConfigurationException(SERVICE_NAME,
					properties, motd.getKey());
		}
	}

	/**
	 * Passes each message to its appropriate ProtocolModule, waits for
	 * the resulting OutputMessageQueues, and adds them all in order to the
	 * global output queue.
	 * 
	 * @param messages The messages to be parsed and replied to.
	 */
	public void parseMessages(InputMessageQueue messages) {
		GlobalOutputMessageQueue.getGlobalOutputMessageQueue().enqueue(
				protocolMap.parseMessages(messages));
	}

	/**
	 * Called when the server is shutting down. The ProtocolManager begins
	 * emptying the protocol map, doing any necessary unloading work for
	 * the ProtocolModules (like writing state to the database).
	 * 
	 */
	@Override
	public void shutdown() {
		protocolMap.shutdown();
	}

}
