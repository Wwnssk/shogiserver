package server.services;

import java.util.Properties;

/**
 * This Interface must be implemented by all system services that can be called
 * by the ServiceManager.
 * 
 * GlobalServices are singletons; you are guaranteed that each GlobalService instance
 * will be created exactly once.
 * 
 * @author Adrian Petrescu
 *
 */
public interface GlobalService {
	
	/**
	 * This method is called the first time this GlobalService is loaded. It is
	 * guaranteed that it will only be called once per lifetime of this service.
	 * 
	 * @param properties A collection of configuration options (possibly empty)
	 * that the GlobalService needs in order to function.
	 * 
	 * @throws InvalidServiceConfigurationException Thrown if the provided
	 * <code>Properties</code> has an invalid configuration set, or is missing
	 * a required setting.
	 */
	void initialize(Properties properties) throws InvalidServiceConfigurationException;
	
	/**
	 * This method is called when this GlobalService is shutdown.. It is guaranteed
	 * that it will only be called once per the lifetime of this service, and that
	 * once it is called, no other methods will.
	 */
	void shutdown();
	
	/**
	 * Each GlobalService must have a unique name identifying it to the system.
	 * @return A unique, constant String identifying the GlobalService.
	 */
	public String getIdentifier();
}
