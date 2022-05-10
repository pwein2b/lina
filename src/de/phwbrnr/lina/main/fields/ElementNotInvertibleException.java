/**
* An exception that indicates that somewhere the attempt was made to use the inverse of a non-invertible ring element.
* 
* @author Philipp Weinbrenner
* @version 2022-05-09
*/

package de.phwbrnr.lina.main.fields;

class ElementNotInvertibleException extends Exception {
	private static final long serialVersionUID = -2797239153040818238L;

	public ElementNotInvertibleException (String description) {
		super(description);
	}
	
	public <R extends Ring> ElementNotInvertibleException (String description, RingElement element) {
		super(description + " - Element '" + element.toString() + "' in Ring " + element.getRing().toString() + "not invertible");
	}
}
