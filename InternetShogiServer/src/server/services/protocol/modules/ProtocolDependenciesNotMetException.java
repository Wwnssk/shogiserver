package server.services.protocol.modules;

/**
 * This Exception is thrown when the server attempts to load a ProtocolModule
 * with missing or invalid dependencies.
 * 
 * @author Adrian Petrescu
 *
 */
public class ProtocolDependenciesNotMetException extends Throwable {

	private String protocolModule;
	private String[] unmetDependencies;
	
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
	 * @param unmetDependencies An array of all the dependencies (both name and version
	 * number) that were not met.
	 */
	public ProtocolDependenciesNotMetException(String protocolModule, String[] unmetDependencies) {
		this.protocolModule = protocolModule;
		this.unmetDependencies = unmetDependencies;
	}
	
	/**
	 * Get the name of the ProtocolModule whose dependencies were not met.
	 * 
	 * @return The Protocol with a missing dependency.
	 */
	public String getProtocolModule() {
		return protocolModule;
	}

	/**
	 * Get all of the dependencies (both name and version number) which were
	 * not met for the ProtocolModule.
	 * 
	 * @return An array of dependency strings representing all unmet
	 * dependencies.
	 */
	public String[] getUnmetDependencies() { 
		return unmetDependencies;
	}
	
}
