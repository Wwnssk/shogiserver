package server.services.protocol.modules;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import server.main.GlobalInputMessageQueue;
import server.services.ServiceManager;
import server.services.event.EventCallback;
import server.services.protocol.InputMessageQueue;
import server.services.protocol.OutputMessageQueue;
import server.services.protocol.ProtocolMessage;
import server.services.user.NoSuchUserException;

/**
 * The MessageOfTheDay (MOTD for short) is a message sent to all users on
 * login, outlining any news, instructions, or miscellaneous information
 * that the administrator wants to make publicly available.
 * 
 * @author APetrescu
 *
 */
public class MessageOfTheDay implements ProtocolModule {

	class NewUserCallback implements EventCallback {
		
		public void eventOccured(Properties properties) {
			if (properties.containsKey("userName")) {
				try {
					GlobalInputMessageQueue.getGlobalInputMessageQueue().enqueue(
							new InputMessageQueue(new ProtocolMessage(ServiceManager.getUserManager().getUser(properties.getProperty("userName")), "motd")));
				} catch (NoSuchUserException e) {
					e.printStackTrace();
				}

			}
		}
		
	}
	
	private ArrayList<String> message;
	
	public static final String name = "MessageOfTheDay";
	public static final String protocolKey = "motd";
	public static final String version = "0.01";
	
	public String[] getDependencies() {
		return new String[0];
	}

	public String getKey() {
		return protocolKey;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public void initialize(Properties properties)
			throws InvalidProtocolConfigurationException {
		String motdConfFile = properties.getProperty("file");
		message = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(motdConfFile));
			String inLine = in.readLine();
			while (inLine != null) {
				message.add(inLine);
				inLine = in.readLine();
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new InvalidProtocolConfigurationException(getName(), properties, "file", "Configuration file not found.");
		} catch (IOException e) {
			throw new InvalidProtocolConfigurationException(getName(), properties, "file", "Configuration file" + motdConfFile + " is not readable.");
		}
		
		NewUserCallback newUser = new NewUserCallback();
		ServiceManager.getEventManager().registerCallback(newUser, ServiceManager.getEventManager().getEvent("USER_CONNECT"));
	}

	/**
	 * A <code>motd</code> message should not contain any parameters. A simple
	 * "motd" will be responded to with a series of "motd message *message*" responses
	 * until the full message has been transmitted, followed by a "motd done".
	 * 
	 * <br>
	 * <b>Expected syntax:</b>
	 * <br>
	 * <code><i>motd</i></code>
	 * 
	 * <br>
	 * <b>Output syntax:</b>
	 * <br>
	 * <code><i>motd message *message*</i> ... <i>motd done</i></code>
	 */
	public OutputMessageQueue parseMessage(ProtocolMessage message) {
		OutputMessageQueue outputQueue = new OutputMessageQueue();
		Iterator<String> i = this.message.iterator();
		
		while(i.hasNext()) {
			ProtocolMessage m = new ProtocolMessage(message.getUser(), "motd message");
			m.append(i.next());
			outputQueue.enqueue(m);
		}
		outputQueue.enqueue(new ProtocolMessage(message.getUser(), "motd done"));
		
		return outputQueue;
	}

	public void shutdown() {
	}

}
