/**
 * Element of the ring of the integers, Z.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-10-05
 */
package de.phwbrnr.lina.main.fields;

public class IntegerElement implements RingElement {
	protected int value;
	
	public IntegerElement(int value) {
		this.value = value;
	}
	
	public IntegerElement(Integer value) {
		this.value = value.intValue();
	}

	@Override
	public RingElement add(RingElement... addends) throws OperationUndefinedException {
		int result = 0;
		for(int i = 0; i < addends.length; i++) {
			if (!(addends[i] instanceof IntegerElement))
				throw new OperationUndefinedException("Element " + addends[i].toString() + " is not an integer");
			
			IntegerElement el = (IntegerElement)addends[i];
			result += el.getValue();
		}
		
		return new IntegerElement(result);
	}

	@Override
	public RingElement subtract(RingElement subtrahend) throws OperationUndefinedException {
		if (subtrahend instanceof IntegerElement) {
			int result = value - ((IntegerElement)subtrahend).getValue();
			return new IntegerElement(result);
		} else {
			throw new OperationUndefinedException("Element " + subtrahend.toString() + " is not an integer");
		}
	}

	@Override
	public RingElement multiply(RingElement... factors) throws OperationUndefinedException {
		int result = 0;
		for(int i = 0; i < factors.length; i++) {
			if (!(factors[i] instanceof IntegerElement))
				throw new OperationUndefinedException("Element " + factors[i].toString() + " is not an integer");
			
			IntegerElement el = (IntegerElement)factors[i];
			result *= el.getValue();
		}
		
		return new IntegerElement(result);
	}

	@Override
	public boolean divisibleBy(RingElement divisor) {
		if (!(divisor instanceof IntegerElement))
			return false;
		
		int other_value = ((IntegerElement)divisor).getValue();
		return (value % other_value == 0);
	}

	@Override
	public RingElement divide(RingElement divisor) throws OperationUndefinedException {
		if (!divisibleBy(divisor))
			throw new OperationUndefinedException("Element " + toString() + " not divisible by " + divisor.toString());
		
		int other_value = ((IntegerElement)divisor).getValue();
		return new IntegerElement(value / other_value);
	}

	@Override
	public boolean invertible() {
		return (value == 1 || value == -1);
	}

	@Override
	public RingElement inverse() throws ElementNotInvertibleException {
		if(invertible())
			return this;
		else
			throw new ElementNotInvertibleException("Only 1, -1 invertible over the integers", this);
	}

	@Override
	public boolean canAdd(RingElement other) {
		return (other instanceof IntegerElement);
	}

	@Override
	public boolean canMultiply(RingElement other) {
		return (other instanceof IntegerElement);
	}

	@Override
	public Ring getRing() {
		return IntegerRing.getInstance();
	}

	public int getValue() {
		return value;
	}

	public int hashCode() {
		return Integer.hashCode(value);
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof IntegerElement))
			return false;
		return ((IntegerElement)other).getValue() == value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public boolean isZero() {
		return value == 0;
	}

	@Override
	public boolean isOne() {
		return value == 1;
	}

	@Override
	public RingElement negative() {
		return new IntegerElement(-1 * value);
	}
}
