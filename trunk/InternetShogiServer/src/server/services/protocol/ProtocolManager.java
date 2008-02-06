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
import server.services.protocol.modules.ProtocolDependenciesNotMetException;
import server.services.protocol.modules.ProtocolModule;
import server.services.protocol.modules.Tell;

public class ProtocolManager implements GlobalService {

	public static final String SERVICE_NAME = "ProtocolManager";
	private static final String configFilePrefix = "_configFile";

	private ProtocolMap protocolMap;

	@Override
	public String getIdentifier() {
		return SERVICE_NAME;
	}

	private void loadProtocolModule(ProtocolModule module, Properties properties)
			throws FileNotFoundException, IOException,
			ProtocolDependenciesNotMetException,
			InvalidProtocolConfigurationException {
		Properties moduleProperties = new Properties();
		if (properties.containsKey(module.getName() + configFilePrefix)) {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					(String) properties
							.get(module.getName() + configFilePrefix))));
			moduleProperties.load(in);
		}

		protocolMap.loadProtocolModule(module, moduleProperties);
	}

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
			throw new InvalidServiceConfigurationException(ayt.getName(),
					properties, ayt.getKey());
		}

		/* Load Tell module */
		ProtocolModule tell = new Tell();
		try {
			loadProtocolModule(tell, properties);
		} catch (Throwable e) {
			throw new InvalidServiceConfigurationException(tell.getName(),
					properties, tell.getKey());
		}
	}

	public void parseMessages(InputMessageQueue messages) {
		GlobalOutputMessageQueue.getGlobalOutputMessageQueue().enqueue(
				protocolMap.parseMessages(messages));
	}

	@Override
	public void shutdown() {
		protocolMap.shutdown();
	}

}
