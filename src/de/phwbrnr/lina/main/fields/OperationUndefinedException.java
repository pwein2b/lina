/**
 * An exception that is thrown if a ring operation is undefined due to the operands not being members of the same ring or so.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

public class OperationUndefinedException extends Exception {

	private static final long serialVersionUID = -6991081755230586090L;

	public OperationUndefinedException (String message) {
		super(message);
	}

	public OperationUndefinedException(String string, OperationUndefinedException ex) {
		super(string, ex);
	}
}
