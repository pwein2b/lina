/**
 * Singleton that represents the ring of the integers; its elements are represented by the IntegerElement class.
 * 
 * @see IntegerElement
 * @author Philipp Weinbrenner
 * @version 2022-10-05
 */
package de.phwbrnr.lina.main.fields;

public class IntegerRing extends Ring {
	static IntegerRing instance = null;
	private IntegerRing () {
		
	}
	
	public static IntegerRing getInstance () {
		if (instance == null)
			instance = new IntegerRing();
		return instance;
	}

	@Override
	public RingElement getZero() {
		return new IntegerElement(0);
	}

	@Override
	public RingElement getOne() {
		return new IntegerElement(1);
	}

	@Override
	public RingElement parseElement(String string) throws ElementParseException {
		try {
			Integer val = Integer.parseInt(string);
			return new IntegerElement(val);
		} catch (NumberFormatException ex) {
			throw new ElementParseException("Unable to parse string '" + string + "' as IntegerElement");
		}
	}
	
	public String getName() {
		return "Z";
	}

	@Override
	public boolean contains(RingElement el) {
		return el.getRing().equals(this);
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isIntegralDomain() {
		return true;
	}
}
