package org.sgf;

public class SGF {

	SGFGameTree gameTree;
	
	void parseSGF (String sgf) throws IncorrectFormatException {
		StringBuffer sgfBuf = new StringBuffer(sgf);
		this.gameTree = SGFGameTree.fromString(sgfBuf);
	}
	
}
