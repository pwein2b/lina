/**
 * An exception to indicate that it was not possible to parse a string that was supposed to represent an element of a ring, field, or similar.
 */
package de.phwbrnr.lina.main.fields;

public class ElementParseException extends Exception {
	private static final long serialVersionUID = -6727458682813786133L;

	public ElementParseException(String comment) {
		super(comment);
	}
}
