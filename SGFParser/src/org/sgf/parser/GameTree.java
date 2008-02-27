package org.sgf.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameTree {

	private Sequence sequence;
	private List<GameTree> gameTrees;
	
	public GameTree() {
		sequence = new Sequence();
		gameTrees = new LinkedList<GameTree>();
	}
	
	public String toString() {
		String result = "(" + sequence.toString();
		Iterator<GameTree> i = gameTrees.iterator();
		while (i.hasNext()) {
			result += i.next().toString();
		}
		result += ")";
			
		return result;
	}
}
