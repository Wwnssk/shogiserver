package org.sgf.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

class Sequence {

	private List<Node> nodes;
	
	public Sequence() {
		nodes = new LinkedList<Node>();
	}
	
	public String toString() {
		Iterator<Node> i = nodes.iterator();
		String result = "";
		while (i.hasNext()) {
			result += i.next().toString();
		}
		
		return result;
	}
	
}
