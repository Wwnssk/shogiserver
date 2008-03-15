package server.services.event;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import server.services.GlobalService;
import server.services.InvalidServiceConfigurationException;

/**
 * This GlobalService is one of the core services of the server. It maintains
 * a table of possible events and callbacks to be notified when those events
 * occur.
 * 
 * @author Adrian Petrescu
 *
 */
public class EventManager implements GlobalService {

	public static final String SERVICE_NAME = "EventManager";
	
	private ConcurrentHashMap<Event, List<EventCallback>> eventCallbackTable;
	private ConcurrentHashMap<String, Event> eventTable;
	
	public String getIdentifier() {
		return SERVICE_NAME;
	}

	public void initialize(Properties properties)
			throws InvalidServiceConfigurationException {
		eventCallbackTable = new ConcurrentHashMap<Event, List<EventCallback>>();
		eventTable = new ConcurrentHashMap<String, Event>();
	}

	/**
	 * Register an event with the server. Its key will be the name of the event.
	 * Once an event is registered, other components can register callbacks to
	 * this event.
	 * 
	 * Only one event can register with a given key, later attempts will fail.
	 * 
	 * @param event The event to register.
	 * @return <code>true</code> if no previous event was registered with the
	 * same key and the registration was successful, <code>false</code> otherwise.
	 */
	public synchronized boolean registerEvent(Event event) {
		if (eventTable.containsKey(event.getName())
				|| eventCallbackTable.containsKey(event)) {
			return false;
		}
		
		eventTable.put(event.getName(), event);
		List<EventCallback> registeredCallbacks = Collections
				.synchronizedList(new LinkedList<EventCallback>());
		eventCallbackTable.put(event, registeredCallbacks);
		return true;
	}
	
	/**
	 * Register a callback for a particular Event with the server. Every time
	 * the given event occurs, the registered EventCallback will also be 
	 * triggered.
	 * 
	 * An EventCallback can only be registered once for a given Event.
	 * 
	 * @param callback The EventCallback to be notified when the Event occurs.
	 * @param event The Event to watch.
	 * @return <code>false</code> if the same callback has previously been
	 * registered to the same event, <code>true</code> otherwise.
	 */
	public synchronized boolean registerCallback(EventCallback callback,
			Event event) {
		if (eventCallbackTable.get(event).contains(callback)) {
			return false;
		}
		
		eventCallbackTable.get(event).add(callback);
		return true;
	}
	
	/**
	 * Used to notify the EventManager that one of its registered events
	 * has occurred. The EventManager will notify each EventCallback in
	 * the same order they were registered in.
	 * 
	 * @param event The event which has occurred.
	 * @param eventData Some extra configuration data to pass to the
	 * EventCallback.
	 */
	synchronized void eventOccured(Event event, Properties eventData) {
		if (!eventCallbackTable.containsKey(event)) {
			return;
		}
		
		List<EventCallback> callbacks = eventCallbackTable.get(event);
		Iterator<EventCallback> i = callbacks.iterator();
		while (i.hasNext()) {
			i.next().eventOccured(eventData);
		}
	}
	
	/**
	 * Fetch the Event with the given name among all Events registered
	 * with the EventManager.
	 * 
	 * @param eventName The name to look for.
	 * @return The registered Event with the given name.
	 */
	public Event getEvent(String eventName) {
		return eventTable.get(eventName);
	}
	
	public void shutdown() {
	}

}
