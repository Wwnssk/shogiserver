package org.sgf.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings("unchecked")
public class Node {

	Properties properties;
	
	public Node() {
		properties = new Properties();
	}
	
	public boolean setProperty(String propIdent, String propValue) {
		if (!checkProperty(propIdent)) {
			properties.setProperty(propIdent, propValue);
			return true;
		}
		
		
		Object oldValue = properties.get(propIdent);
		
		if (oldValue instanceof String) {
			LinkedList<String> newValue = new LinkedList<String>();
			newValue.add((String) oldValue);
			newValue.add(propValue);
			properties.put(propIdent, newValue);
			return true;
		} else if (oldValue instanceof LinkedList) {
			LinkedList<String> newValue = new LinkedList<String>((LinkedList<String>)oldValue);
			newValue.add(propValue);
			properties.put(propIdent, newValue);
			return true;
		}
		
		return false;
	}
	
	public int getNumberOfProperties(String propIdent) {
		if (properties.get(propIdent) == null) {
			return 0;
		} else if (properties.get(propIdent) instanceof String) {
			return 1;
		} else if (properties.get(propIdent) instanceof LinkedList) {
			return ((LinkedList<String>) properties.get(propIdent)).size();
		}
		
		return 0;
	}
	
	public String getProperty(String propIdent) {
		if (!checkProperty(propIdent)) {
			return null;
		} else if (getNumberOfProperties(propIdent) == 1) {
			return properties.getProperty(propIdent);
		} else {
			return ((LinkedList<String>) properties.get(propIdent)).getFirst();
		}
	}
	
	public String[] getProperties(String propIdent) {
		if (!checkProperty(propIdent)) {
			return new String[0];
		} else if (getNumberOfProperties(propIdent) == 1) {
			return new String[] { properties.getProperty(propIdent) };
		} else {
			return ((LinkedList<String>)properties.get(propIdent)).toArray(new String[0]);
		}
	}
	
	public boolean checkProperty(String propIdent) {
		return properties.containsKey(propIdent);
	}
	
	public boolean removeProperty(String propIdent) {
		return !(properties.remove(propIdent) == null);
	}
	
	public boolean removeProperty(String propIdent, String propValue) {
		if (!checkProperty(propIdent)) {
			return false;
		}
		
		Object oldValue = properties.get(propIdent);
		
		if (oldValue instanceof String) {
			if (((String) oldValue).equals(propValue)) {
				return removeProperty(propIdent);
			} else {
				return false;
			}
		} else if (oldValue instanceof LinkedList) {
			Iterator<String> i = ((LinkedList<String>) oldValue).iterator();
			while (i.hasNext()) {
				if (i.next().equals(propValue)) {
					i.remove();
					if (((LinkedList<String>) oldValue).size() == 1) {
						properties.put(propIdent, ((LinkedList<String>) oldValue).getFirst());
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String toString() {
		String result = ";";
		
		Set propIdents = properties.keySet();
		Iterator i = propIdents.iterator();
		
		while(i.hasNext()) {
			String propIdent = (String) i.next();
			result += propIdent;
			for (String propValue : getProperties(propIdent)) {
				result += "[" + propValue + "]";
			}
		}
		
		return result;
	}
}
