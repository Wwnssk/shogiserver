package server.services.event;

import java.util.Properties;

import server.services.ServiceManager;

/**
 * An Event represents a discrete occurrence on the server, such as
 * a user disconnecting or a game beginning. Events can be registered
 * with the EventManager to let other components of the system register
 * callbacks against these Events to be notified and take special actions
 * against them.
 * 
 * @author Adrian Petrescu
 *
 */
abstract public class Event {
	
	/** 
	 * Get the unique name identifying this event. If multiple
	 * Events have the same name, then all except the first will
	 * be unable to register with the EventManager.
	 * 
	 * @return A unique name identifying this event.
	 */
	abstract public String getName();
	
	/**
	 * Called to indicate that the event which this Event object symbolises
	 * has occurred. This method will notify the Event Manager, which will
	 * notify all registered callback functions.
	 * 
	 * @param eventData Any extra information needed by callbacks to do
	 * their job.
	 */
	protected void occured(Properties eventData) {
		ServiceManager.getEventManager().eventOccured(this, eventData);
	}
	
}
