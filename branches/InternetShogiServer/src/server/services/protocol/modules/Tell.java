package server.services.protocol.modules;

import java.util.Properties;

import server.services.ServiceManager;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;
import server.services.user.NoSuchUserException;

public class Tell implements ProtocolModule {
	private static final String name = "Tell";
	private static final String protocolKey = "tell";
	private static final String version = "0.01";
	
	public String getVersion() {
		return version;
	}
	
	public String getName() {
		return name;
	}
	
	public String getKey() {
		return protocolKey;
	}
	
	public String[] getDependencies() {
		return new String[0];
	}
	
	public void initialize(Properties properties) {
		
	}
	
	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		String[] messageData = message.getTokenizedPayload();
		ProtocolMessage response = new ProtocolMessage(protocolKey);
		
		if (messageData.length < 2) {
			//TODO: Handle improper syntax.
			return null;
		}
		
		try {
			if (ServiceManager.getConnectionManager().checkUserLoggedIn(ServiceManager.getUserManager().getUser(messageData[0]))) {
				response.setUser(ServiceManager.getUserManager().getUser(messageData[0]));
			} else {
				//TODO: Handle user logged off.
				return null;
			}
		} catch (NoSuchUserException e) {
			//TODO: Handle no such user.
			return null;
		}
		
		response.append(message.getUser().getUserName());
		for (int i = 1; i < messageData.length; i++) {
			response.append(messageData[i]);
		}
		OutputMessageQueue responseQueue = new OutputMessageQueue(response);
		return responseQueue;
	}
	
	public void shutdown() {
		
	}

}
