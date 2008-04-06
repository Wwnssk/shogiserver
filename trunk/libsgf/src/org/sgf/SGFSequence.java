package org.sgf;

import java.util.ArrayList;
import java.util.Iterator;

public class SGFSequence {

	private ArrayList<SGFNode> nodes = new ArrayList<SGFNode>();
	
	void addNode(SGFNode node) {
		nodes.add(node);
	}
	
	static SGFSequence fromString(StringBuffer sgf) throws IncorrectFormatException {
		SGFSequence sequence = new SGFSequence();
		sequence.addNode(SGFNode.fromString(sgf));
		
		/* Remove leading whitespace */
		while (Character.isWhitespace(sgf.charAt(0))) {
			sgf.deleteCharAt(0);
		}
		
		while (sgf.charAt(0) == ';') {
			sequence.addNode(SGFNode.fromString(sgf));
			
			/* Remove leading whitespace */
			while (Character.isWhitespace(sgf.charAt(0))) {
				sgf.deleteCharAt(0);
			}
		}
		
		return sequence;
	}
	
	public String toString() {
		String result = "";
		Iterator<SGFNode> i = nodes.iterator();
		while (i.hasNext()) {
			result += i.next().toString();
		}
		return result;
	}
	
}
