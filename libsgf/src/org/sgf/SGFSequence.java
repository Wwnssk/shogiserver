package org.sgf;

import java.util.ArrayList;

public class SGFSequence {

	private ArrayList<SGFNode> nodes = new ArrayList<SGFNode>();
	
	void addNode(SGFNode node) {
		nodes.add(node);
	}
	
	static SGFSequence fromString(StringBuffer sgf) throws IncorrectFormatException {
		SGFSequence sequence = new SGFSequence();
		sequence.addNode(SGFNode.fromString(sgf));
		
		while (Character.isWhitespace(sgf.charAt(0))) {
			sgf.deleteCharAt(0);
		}
		
		while (sgf.charAt(0) == ';') {
			sequence.addNode(SGFNode.fromString(sgf));
			while (Character.isWhitespace(sgf.charAt(0))) {
				sgf.deleteCharAt(0);
			}
		}
		
		return sequence;
	}
	
}
