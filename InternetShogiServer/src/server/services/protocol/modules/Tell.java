package server.services.protocol.modules;

import java.util.Properties;

import server.services.ServiceManager;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;
import server.services.user.NoSuchUserException;

/**
 * This ProtocolModule is the simplest form of chat. A user can issue a 
 * <code>tell</code> to another user to send a one-line message that only
 * the other user can see.
 * 
 * @author Adrian Petrescu
 */
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
	
	
	/**
	 * The <b>tell</b> command takes only two parameters. The first token is assumed
	 * to be the recipient and the rest of the tokens are assumed to be the message.
	 * The message received by the recipient is identical to the one sent by the sender,
	 * except that the sender's name replaces the recipient's as the second argument.
	 * 
	 * <br>
	 * <b>Expected syntax:</b>
	 * <br>
	 * <code><i>tell recipient message</i></code>
	 * 
	 * <br>
	 * <b>Output syntax:</b>
	 * <br>
	 * <i>tell {sender message | [invalid {syntax | {{no_such_user | not_logged_in} recipient}]}</i>
	 */
	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		String[] messageData = message.getTokenizedPayload();
		ProtocolMessage response = new ProtocolMessage(protocolKey);
		
		/* If not enough parameters were given, respond with 
		 * tell invalid syntax recipient message
		 */
		if (messageData.length < 2) {
			response.append("invalid");
			response.append("syntax");
			response.append(message.getPayload());
			response.setUser(message.getUser());
			OutputMessageQueue responseQueue = new OutputMessageQueue(response);
			return responseQueue;
		}
		
		/* If an invalid user was given, respond in two different ways based on whether the
		 * user exists or not.
		 */
		try {
			if (ServiceManager.getConnectionManager().checkUserLoggedIn(ServiceManager.getUserManager().getUser(messageData[0]))) {
				response.setUser(ServiceManager.getUserManager().getUser(messageData[0]));
			} else {
				/* The user exists, but isn't logged in right now. We respond with
				 * tell invalid not_logged_in recipient
				 */
				response.append("invalid");
				response.append("not_logged_in");
				response.append(messageData[0]);
				response.setUser(message.getUser());
				OutputMessageQueue responseQueue = new OutputMessageQueue(response);
				return responseQueue;
			}
		} catch (NoSuchUserException e) {
			/* The user has never registered. The name is simply invalid. Repond with
			 * tell invalid no_such_user recipient
			 */
			response.append("invalid");
			response.append("no_such_user");
			response.append(messageData[0]);
			response.setUser(message.getUser());
			OutputMessageQueue responseQueue = new OutputMessageQueue(response);
			return responseQueue;
		}
		
		/* If we've gotten here, it means the message is syntactically valid and the
		 * user is logged on. Pack up the message and send it on its way.
		 */
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
