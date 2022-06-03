/**
 * This exception indicates that an operation can not be carried out because it is not or not properly implemented.
 * This serves a similar purpose as OperationUndefinedException.
 */

package de.phwbrnr.lina.main.fields;

public class NotImplementedException extends OperationUndefinedException {
	public NotImplementedException(String string) {
		super(string);
	}
	
	public NotImplementedException(String string, OperationUndefinedException ex) {
		super(string, ex);
	}

	private static final long serialVersionUID = 1L;

}
