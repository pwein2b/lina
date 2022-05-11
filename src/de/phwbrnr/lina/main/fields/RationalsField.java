/**
 * Represent the field of rational numbers as a singleton.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-11
 * 
 * TODO implement shortening of fractions
 */

package de.phwbrnr.lina.main.fields;

public class RationalsField extends Field {
	private static RationalsField instance;
	private IntegerRing integers;
	public static RationalsField getInstance() {
		if (instance == null)
			instance = new RationalsField();
		return instance;
	}
	
	private RationalsField() {
		integers = IntegerRing.getInstance();
	}

	@Override
	public String getName() {
		return "Q";
	}

	@Override
	public RingElement getZero() {
		try {
			return new Fraction(integers.getZero(), integers.getOne());
		} catch (OperationUndefinedException e) {
			throw new Error("Unexpected exception when constructing 0/1 in Q", e);
		}
	}

	@Override
	public RingElement getOne() {
		try {
			return new Fraction(integers.getOne(), integers.getOne());
		} catch (OperationUndefinedException e) {
			throw new Error("Unexpected exception when constructing 1/1 in Q", e);
		}
	}

	@Override
	public RingElement parseElement(String string) throws ElementParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(RingElement el) {
		// TODO outsource this to QuotientField
		if(el instanceof IntegerRing)
			return true;
		if (!(el instanceof Fraction))
			return el.getRing().equals(this);
		
		Fraction other = (Fraction)el;
		return integers.contains(other.getNumerator()) && integers.contains(other.getDenominator());
	}

}
