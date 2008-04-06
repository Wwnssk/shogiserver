package org.sgf;

import java.util.ArrayList;

public class SGFProperty {

	String propIdent;
	String propValue;
	ArrayList<String> propValues;
	
	void addValue(String value) {
		if (propValue == null && propValues == null) {
			propValue = value;
			return;
		}
		else {
			propValue = null;
			propValues.add(value);
		}
	}
	
	String[] getValues() {
		if (propValues == null) {
			String[] r = { propValue };
			return r;
		} else {
			return propValues.toArray(new String[0]);
		}
	}
	
	private String escapePropertyValue(String propValue) {
		return propValue.replace("]", "\\]");
	}

	public SGFProperty(String propIdent) {
		this.propIdent = propIdent;
	}

	static SGFProperty fromString(StringBuffer sgf)
			throws IncorrectFormatException {
		
		/* Remove leading whitespace */
		if (!Character.isUpperCase(sgf.charAt(0))) {
			throw new IncorrectFormatException();
		}

		String propIdent = String.valueOf(sgf.charAt(0));
		sgf.deleteCharAt(0);
		while (Character.isUpperCase(sgf.charAt(0))) {
			propIdent += String.valueOf(sgf.charAt(0));
			sgf.deleteCharAt(0);
		}

		if (!(sgf.charAt(0) == '[')) {
			throw new IncorrectFormatException();
		}

		SGFProperty property = new SGFProperty(propIdent);


		while (sgf.charAt(0) == '[') {
			sgf.deleteCharAt(0);
			String propValue = "";
			boolean escape = false;
			while (!escape && !(sgf.charAt(0) == ']')) {
				escape = false;
				char nextChar = sgf.charAt(0);
				sgf.deleteCharAt(0);

				if (nextChar == '\\') {
					escape = true;
				} else {
					propValue += String.valueOf(nextChar);
				}
			}
			sgf.deleteCharAt(0);
			property.addValue(propValue);
			
			/* Remove leading whitespace */
			while (Character.isWhitespace(sgf.charAt(0))) {
				sgf.deleteCharAt(0);
			}
		}

		return property;
	}
	
	public String toString() {
		String result = propIdent;
		for (String value : getValues()) {
			result += "[" + escapePropertyValue(value) + "]";
		}
		return result;
	}
}
