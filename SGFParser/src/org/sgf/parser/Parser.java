package org.sgf.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

	public GameTree loadFromFile(String fileName) throws IOException {
		GameTree tree = new GameTree();
		String sgfFile = "";
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String inLine = in.readLine();
			while (inLine != null) {
				sgfFile += inLine;
				inLine = in.readLine();
			}
		} catch (IOException e) {
			throw e;
		}
		
		return tree;
	}
	
}
