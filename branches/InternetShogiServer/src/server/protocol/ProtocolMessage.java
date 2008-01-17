package server.protocol;

/**
 * Represents a single protocol instruction, either from the client or the
 * server. It represents the smallest functional unit of the protocol.
 * 
 * @author Adrian Petrescu
 * 
 */
public class ProtocolMessage {

	// $ANALYSIS-IGNORE
	private String message = "";

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
	 * Every protocol message identifies its own protocol module by the protocol
	 * key, which is usually the first token of the message, allowing for easily
	 * delegation to the appropriate handler.
	 * 
	 * @return The first token of the message.
	 */
	public String getProtocolKey() {
		// $ANALYSIS-IGNORE
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
		return message.substring(message.indexOf(' '));
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
	 * Adds a token to the end of the ProtocolMessage's payload. All leading and
	 * trailing whitespace is removed from the token.
	 * 
	 * @param token
	 *            A token to be added to the end of the payload of the message.
	 * @return The current message. Identical to calling
	 *         <code>getMessage()</code>.
	 */
	public String append(String token) {
		message += " " + token.trim();
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
		message = protocolKey.trim() + " " + getPayload();
		return message;
	}

}
