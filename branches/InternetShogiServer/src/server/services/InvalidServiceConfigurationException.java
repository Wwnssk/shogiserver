package server.services;

import java.util.Properties;

/**
 * This Exception is thrown when the server attempts to start up a GlobalService
 * with missing or invalid configuration data.
 * 
 * @author Adrian Petrescu
 *
 */
public class InvalidServiceConfigurationException extends Throwable {

	private String service;
	private Properties properties;
	private String key;
	private String message;
	
	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifices this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	/**
	 * Constructs a new InvalidServiceConfiguration.
	 * @param service The unique identifier of the service which threw this exception.
	 * @param properties The <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * @param key The key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 */
	public InvalidServiceConfigurationException(String service, Properties properties, String key) {
		this.service = service;
		this.properties = properties;
		this.key = key;
	}
	
	/**
	 * Constructs a new InvalidServiceConfiguration.
	 * @param service The unique identifier of the service which threw this exception.
	 * @param properties The <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * @param key The key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 * @param message A human-readable message explaining what was wrong with the given
	 * configuration.
	 */
	public InvalidServiceConfigurationException(String service, Properties properties, String key, String message) {
		this.service = service;
		this.properties = properties;
		this.key = key;
		this.message = message;
	}
	
	/**
	 * Get the service whose <code>initialize()</code> method threw this error.
	 * @return The service whose <code>initialize()</code> method threw this error.
	 */
	public String getService() {
		return service;
	}
	
	/**
	 * Get the <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * @return the <code>Properties</code> structure which contained an illegal
	 * or absent configuration
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Get the key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 * @return the key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get a human-readable message explaining what was wrong with the given
	 * configuration.
	 * @return a human-readable message explaining what was wrong with the given
	 * configuration.
	 */
	public String getMessage() { 
		return message;
	}
	
}
