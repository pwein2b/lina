/**
 * Singleton that represents the ring of the integers; its elements are represented by the IntegerElement class.
 * 
 * @see IntegerElement
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.fields;

public class IntegerRing implements Ring, EuclideanRing {
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

	public int degree (RingElement el) throws OperationUndefinedException {
		if (!(el instanceof IntegerElement))
			throw new IllegalArgumentException("Element " + el.toString() + " is not an integer");
		if (el.isZero())
			throw new OperationUndefinedException("The zero element has undefined degree");

		return Math.abs(((IntegerElement)el).getValue());
	}

	public RingElement[] remainder_division (RingElement dividend, RingElement divisor) throws OperationUndefinedException {
		if (!(contains(dividend) && contains(divisor)))
			throw new OperationUndefinedException("Dividend " + dividend.toString() + " and divisor " + divisor.toString() + " have to be elements of the integers");

		int divid = ((IntegerElement)dividend).getValue();
		int divis = ((IntegerElement)divisor).getValue();

		int remainder = divid % divis;
		int quotient = (divid - remainder) / divis;

		return new RingElement[] { new IntegerElement(quotient), new IntegerElement(remainder) };
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
