package server.services.protocol.modules;

import java.util.Properties;

/**
 * This Exception is thrown when the server attempts to start up a ProtocolModule
 * with missing or invalid configuration data.
 * 
 * @author Adrian Petrescu
 *
 */
public class InvalidProtocolConfigurationException extends Throwable {

	private String protocolModule;
	private Properties properties;
	private String key;
	private String message;
	
	/* Satisfies the condition that extends Throwable (which
	 * implements Serializable) specifies this value for
	 * serialization purposes. It doesn't really matter what
	 * we use here.
	 */
	private static final long serialVersionUID = 54091L;
	
	/**
	 * Constructs a new InvalidProtocolConfigurationException.
	 * 
	 * @param protocolModule The name of the ProtocolModule which threw this exception.
	 * @param properties The <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * @param key The key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 */
	public InvalidProtocolConfigurationException(String protocolModule, Properties properties, String key) {
		this.protocolModule = protocolModule;
		this.properties = properties;
		this.key = key;
	}
	
	/**
	 * Constructs a new InvalidProtocolConfigurationException.
	 * 
	 * @param protocolModule The name of the ProtocolModule which threw this exception.
	 * @param properties The <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * @param key The key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 * @param message A human-readable message explaining what was wrong with the given
	 * configuration.
	 */
	public InvalidProtocolConfigurationException(String protocolModule, Properties properties, String key, String message) {
		this.protocolModule = protocolModule;
		this.properties = properties;
		this.key = key;
		this.message = message;
	}
	
	/**
	 * Get the name of the ProtocolModule whose <code>initialize()</code> method threw this error.
	 * 
	 * @return The Protocol whose <code>initialize()</code> method threw this error.
	 */
	public String getProtocolModule() {
		return protocolModule;
	}
	
	/**
	 * Get the <code>Properties</code> structure which contained an illegal
	 * or absent configuration.
	 * 
	 * @return The <code>Properties</code> structure which contained an illegal
	 * or absent configuration
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Get the key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 * 
	 * @return The key of the setting which was either missing or absent from the
	 * <code>Properties</code> structure.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get a human-readable message explaining what was wrong with the given
	 * configuration.
	 * 
	 * @return A human-readable message explaining what was wrong with the given
	 * configuration.
	 */
	public String getMessage() { 
		return message;
	}
	
}
