package server.services.event;

import java.util.Properties;

/**
 * An EventCallback represents a desire to be notified of a certain
 * Event happening on the server. As soon as the Event that an
 * EventCallback is registered to occurs, the callback will be
 * notified and may take any action as a response.
 * 
 * @author Adrian Petrescu
 *
 */
public interface EventCallback {

	/**
	 * Once this EventCallback is registered against an Event, this 
	 * method will be called each time the corresponding Event occurs.
	 * 
	 * @param eventData Some extra information. Check the API of
	 * the event this callback is registered to to see what can
	 * be expected to be here.
	 */
	public void eventOccured(Properties eventData);
	
}
