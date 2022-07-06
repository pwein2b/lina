/**
 * An exception class that indicates something went wrong when deserializing something.
 * 
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.deserialize;

public class DeserializationException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeserializationException(String message) {
		super(message);
	}
	
	public DeserializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
