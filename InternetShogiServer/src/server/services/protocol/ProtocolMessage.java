package server.services.protocol;

import server.services.user.User;

/**
 * Represents a single protocol instruction, either from the client or the
 * server. It represents the smallest functional unit of the protocol.
 * 
 * @author Adrian Petrescu
 * 
 */
public class ProtocolMessage {

	private String message = "";
	private User user;

	/**
	 * Creates a new ProtocolMessage with an empty message. Its payload and
	 * protocol key are both the empty string.
	 */
	public ProtocolMessage() {
	}

	/**
	 * Constructs a new ProtocolMessage containing the given
	 * <code>message</code>. The message will be trimmed of leading and
	 * trailing whitespace.
	 * 
	 * @param message
	 *            The message to be stored.
	 */
	public ProtocolMessage(String message) {
		this.message = message.trim();
	}

	/**
	 * Constructs a new ProtocolMessage with the associated User. If this
	 * ProtocolMessage is placed in an InputMessageQueue, then <code>user</code>
	 * will be considered to be the sender of this message. If this
	 * ProtocolMessage is placed in an OutputMessageQueue, then
	 * <code>user</code> will be considered the target recipient of this
	 * message.
	 * 
	 * @param user
	 *            The User who is either the sender or the recipient of this
	 *            message.
	 */
	public ProtocolMessage(User user) {
		this.user = user;
	}

	/**
	 * Constructs a new ProtocolMessage containing the given message, and
	 * associated with with the associated User. If this ProtocolMessage is
	 * placed in an InputMessageQueue, then <code>user</code> will be
	 * considered to be the sender of this message. If this ProtocolMessage is
	 * placed in an OutputMessageQueue, then <code>user</code> will be
	 * considered the target recipient of this message. The message will be
	 * trimmed of leading and trailing whitespace.
	 * 
	 * @param user
	 *            The User who is either the sender or the recipient of this
	 *            message.
	 * @param message
	 *            The message to be stored.
	 */
	public ProtocolMessage(User user, String message) {
		this.message = message.trim();
		this.user = user;
	}

	/**
	 * Every protocol message identifies its own protocol module by the protocol
	 * key, which is usually the first token of the message, allowing for easily
	 * delegation to the appropriate handler.
	 * 
	 * @return The first token of the message.
	 */
	public String getProtocolKey() {
		return (message.length() == 0) ? "" : message.split(" ")[0];
	}

	/**
	 * The payload of a ProtocolMessage consists of all tokens besides the
	 * first. It represents the data to be passed to the protocol handler
	 * specified by the protocol key.
	 * 
	 * @return The payload of the ProtocolMessage, as a String.
	 */
	public String getPayload() {
		if (message.split(" ").length < 2)
			return "";
		return message.substring(message.indexOf(' ') + 1);
	}

	/**
	 * The payload of a ProtocolMessage consists of all tokens besides the
	 * first. It represents the data to be passed to the protocol handler
	 * specified by the protocol key.
	 * 
	 * This is a convenience method that tokenizes the payload into an array.
	 * The content is identical to the result of calling
	 * <code>getPayload()</code>.
	 * 
	 * @return The payload of the ProtocolMessage, as a tokenized String array.
	 */
	public String[] getTokenizedPayload() {
		if (message.split(" ").length < 2)
			return new String[0];
		String[] payload = new String[message.split(" ").length - 1];
		System.arraycopy(message.split(" "), 1, payload, 0, payload.length);
		return payload;
	}

	/**
	 * Returns the message contained in the ProtocolMessage, including both the
	 * protocol key and the payload.
	 * 
	 * @return The message contained in the ProtocolMessage.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Adds token(s) to the end of the ProtocolMessage's payload. All leading
	 * and trailing whitespace is removed from each token.
	 * 
	 * @param tokens
	 *            A string of tokens to be added to the end of the payload of
	 *            the message.
	 * @return The current message. Identical to calling
	 *         <code>getMessage()</code>.
	 */
	public String append(String tokens) {
		String[] tokensArray = tokens.trim().split("\\s+");

		// If we try to append the empty string, or a bunch of whitespace, then
		// tokens.trim().split("\\s+") returns an array of length 1 containing
		// the empty string. So do nothing. If we don't treat this as a special
		// case, the main loop will add an unnecessary space to the message.
		if (tokensArray[0].equals(""))
			return message;

		for (int i = 0; i < tokensArray.length; i++) {
			message += " " + tokensArray[i];
		}
		return message;
	}

	/**
	 * Changes the protocol key (the first token) of the ProtocolMessage. If the
	 * message was the empty string, the ProtocolMessage becomes a message to
	 * the specified protocol key, with an empty payload. All leading and
	 * trailing whitespace is removed from the protocol key.
	 * 
	 * @param protocolKey
	 *            The new protocol key for the ProtocolMessage.
	 * @return The current message. Identical to calling
	 *         <code>getMessage()</code>.
	 */
	public String setProtocolKey(String protocolKey) {
		message = protocolKey.trim() + (getPayload().length() == 0 ? "" : " ")
				+ getPayload();
		return message;
	}
	
	/**
	 * Returns this message's associated User. If this ProtocolMessage is
	 * placed in an InputMessageQueue, then <code>user</code> will be
	 * considered to be the sender of this message. If this ProtocolMessage is
	 * placed in an OutputMessageQueue, then <code>user</code> will be
	 * considered the target recipient of this message.
	 * 
	 * @return The user who is either the sender or recipient of this message.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets this message's associated User. If this ProtocolMessage is
	 * placed in an InputMessageQueue, then <code>user</code> will be
	 * considered to be the sender of this message. If this ProtocolMessage is
	 * placed in an OutputMessageQueue, then <code>user</code> will be
	 * considered the target recipient of this message.
	 * 
	 * @param user The user who is either the sender or recipient of this message.
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
