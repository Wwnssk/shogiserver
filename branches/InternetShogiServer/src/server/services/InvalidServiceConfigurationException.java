package server.services;

import java.util.Properties;

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
	
	public InvalidServiceConfigurationException(String service, Properties properties, String key) {
		this.service = service;
		this.properties = properties;
		this.key = key;
	}
	
	public InvalidServiceConfigurationException(String service, Properties properties, String key, String message) {
		this.service = service;
		this.properties = properties;
		this.key = key;
		this.message = message;
	}
	
	public String getService() {
		return service;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getMessage() { 
		return message;
	}
	
}
