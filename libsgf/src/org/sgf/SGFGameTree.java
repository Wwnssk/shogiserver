package org.sgf;

import java.util.ArrayList;

public class SGFGameTree {

	private SGFSequence sequence;
	private SGFGameTree[] subtrees;
	
	SGFSequence getSequence() {
		return sequence;
	}
	
	static SGFGameTree fromString(StringBuffer sgf) throws IncorrectFormatException {
		SGFGameTree gameTree = new SGFGameTree();
		ArrayList<SGFGameTree> subtrees = new ArrayList<SGFGameTree>();
		
		while (Character.isWhitespace(sgf.charAt(0))) {
			sgf.deleteCharAt(0);
		}
		
		if (!(sgf.charAt(0) == '(')) {
			throw new IncorrectFormatException();
		}
		sgf.deleteCharAt(0);
		
		while (Character.isWhitespace(sgf.charAt(0))) {
			sgf.deleteCharAt(0);
		}
		
		gameTree.sequence = SGFSequence.fromString(sgf);
		
		while (Character.isWhitespace(sgf.charAt(0))) {
			sgf.deleteCharAt(0);
		}
		
		while (!(sgf.charAt(0) == ')')) {
			subtrees.add(SGFGameTree.fromString(sgf));
		}
		gameTree.subtrees = subtrees.toArray(new SGFGameTree[0]);
		
		sgf.deleteCharAt(0);
		
		return gameTree;
	}
}
