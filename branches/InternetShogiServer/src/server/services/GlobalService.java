package server.services;

import java.util.Properties;

public interface GlobalService {
	void initialize(Properties properties) throws InvalidServiceConfigurationException;
	void shutdown();
	public String getIdentifier();
}
