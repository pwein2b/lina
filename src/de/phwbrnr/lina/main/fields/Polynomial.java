/**
 * Represent a polynomial over a ring.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-10-05
 */
package de.phwbrnr.lina.main.fields;


public class Polynomial implements RingElement {
	private RingElement[] coefficients;
	private Ring ring;
	
	/**
	 * Create a polynomial over a ring.
	 * @param ring
	 * @param coefficients
	 */
	public Polynomial(Ring ring, RingElement... coefficients) {
		this.ring = ring;
		this.coefficients = coefficients;
		
		for (int i = 0; i < coefficients.length; i++) {
			if (!ring.contains(coefficients[i]))
				throw new IllegalArgumentException("Coefficient #" + i + " (" + coefficients[i].toString() + ") is not member of ring " + ring.toString()); 
		}
	}
	
	/**
	 * Create the zero polynomial over a ring.
	 * @param ring
	 */
	public Polynomial(Ring ring) {
		this(ring, ring.getZero());
	}
	
	/**
	 * Create a polynomial of the form c * X^i.
	 */
	public Polynomial (Ring ring, int degree, RingElement coefficient) {
		if(!ring.contains(coefficient))
			throw new IllegalArgumentException("Coefficient " + coefficient.toString() + "is not member of ring " + ring.toString());
		
		this.ring = ring;
		coefficients = new RingElement[degree];
		for (int i = 0; i < degree - 1; i++)
			coefficients[i] = ring.getZero();
		coefficients[degree - 1] = coefficient;
	}
	
	@Override
	public Polynomial add(RingElement... addends) throws OperationUndefinedException {
		Polynomial result = new Polynomial(ring);
		for (int i = 0; i < addends.length; i++) {
			if (!canAdd(addends[i]))
					throw new OperationUndefinedException("Cannot add polynomial over " + ring.getName() + " and element of " + addends[i].getRing());
			
			Polynomial other = null;
			if (addends[i] instanceof Polynomial) {
				other = (Polynomial)addends[i];
			} else {
				/* simple ring element */
				other = new Polynomial(ring, addends[i]);
			}
				
			int len = Math.max(result.getDegree(), other.getDegree());
				
			RingElement[] coefficients = new RingElement[len];
			for (int j = 0; j < len; j++)
				coefficients[j] = result.getCoefficient(j).add(other.getCoefficient(j));
				
			result = new Polynomial(ring, coefficients);
		}
		
		return result;
	}

	@Override
	public Polynomial subtract(RingElement subtrahend) throws OperationUndefinedException {
		Polynomial other = null;
		if (subtrahend instanceof Polynomial) {
			other = (Polynomial)subtrahend;
		} else {
			/* simple ring element */
			other = new Polynomial(ring, subtrahend);
		}
			
		int len = Math.max(getDegree(), other.getDegree());
			
		RingElement[] coefficients = new RingElement[len];
		for (int j = 0; j < len; j++)
			coefficients[j] = getCoefficient(j).subtract(other.getCoefficient(j));
			
		return new Polynomial(ring, coefficients);
	}

	@Override
	public Polynomial multiply(RingElement... factors) throws OperationUndefinedException {
		Polynomial result = new Polynomial(ring, ring.getOne());
		for (int i = 0; i < factors.length; i++) {
			if (!canAdd(factors[i]))
					throw new OperationUndefinedException("Cannot multiply polynomial over " + ring.getName() + " and element of " + factors[i].getRing());
			
			Polynomial other = null;
			if (factors[i] instanceof Polynomial) {
				other = (Polynomial)factors[i];
			} else {
				/* simple ring element */
				other = new Polynomial(ring, factors[i]);
			}
			
			if (other.getDegree() == -1) {
				/* multiplication with zero */
				return other;
			}
				
			int len = result.getDegree() + other.getDegree();
				
			RingElement[] coefficients = new RingElement[len];
			for (int j = 0; j < len; j++) {
				/* compute coefficients[j] */
				RingElement intermediate = ring.getZero();
				for (int k = 0; k < j; k++) {
					intermediate = intermediate.add(result.getCoefficient(k).multiply(other.getCoefficient(j - k)));
				}
				
				coefficients[j] = intermediate;
			}
				
			result = new Polynomial(ring, coefficients);
		}
		
		return result;
	}

	@Override
	public boolean divisibleBy(RingElement divisor) {
		try {
			divide(divisor);
			return true;
		} catch (OperationUndefinedException ex) {
			return false;
		}
	}

	@Override
	public Polynomial divide(RingElement divisor) throws OperationUndefinedException {
		if (ring.contains(divisor)) {
			// scalar multiplication
			try {
				return multiply(divisor.inverse());
			} catch (ElementNotInvertibleException ex) {
				throw new OperationUndefinedException("Scalar element to divide by is not invertible");
			}
		} else if (!(divisor instanceof Polynomial)) {
			throw new OperationUndefinedException("Can only divide polynomials by other polynomials or by scalars");
		}
		
		Polynomial div = (Polynomial)divisor;
		if (div.getDegree() == -1)
			throw new OperationUndefinedException("Divison by zero polynomial not possible");
		
		Polynomial remainder = this;
		Polynomial quotient = new Polynomial(ring);
		
		while(remainder.getDegree() <= div.getDegree()) {
			int fromDeg = div.getDegree();
			int toDeg = remainder.getDegree();
			RingElement leading_coefficient = remainder.getCoefficient(toDeg).divide(div.getCoefficient(fromDeg));
			
			Polynomial factor = new Polynomial(ring, toDeg - fromDeg, leading_coefficient);
			quotient = quotient.add(factor);
			
			remainder = remainder.subtract(div.multiply(factor));
		}
		
		if (remainder.getDegree() != -1)
			throw new OperationUndefinedException("Polynomial " + toString() + " over " + ring.getName() + " not evenly divisible by " + divisor.toString() + " (remainder " + remainder.toString() + ")");
		
		return quotient;
	}

	@Override
	public boolean invertible() {
		return (getDegree() == 0 && coefficients[0].invertible()); 
	}

	@Override
	public RingElement inverse() throws ElementNotInvertibleException {
		if (getDegree() != 0)
			throw new ElementNotInvertibleException("Only non-zero polynomials of degree 0 have a chance to be invertible");
		return coefficients[0].inverse();
	}

	@Override
	public boolean canAdd(RingElement other) {
		if (other instanceof Polynomial) {
			return ((Polynomial)other).getRing().equals(this.ring);
		} else {
			return ring.contains(other);
		}
	}

	@Override
	public boolean canMultiply(RingElement other) {
		return canAdd(other);
	}

	@Override
	public Ring getRing() {
		return ring;
	}

	public RingElement[] getCoefficients() {
		return coefficients;
	}
	
	/**
	 * Compute the degree of the polynomial.
	 * @return If it is not the zero polynomial: The degree of the polynomial, that is, the highest term coefficient
	 * that does not vanish; -1, otherwise.
	 */
	public int getDegree () {
		for (int i = coefficients.length - 1; i > 0; i--) {
			if (!coefficients[i].isZero())
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Return the i-th coefficient, where the 0th coefficient is the constant term.
	 * If i is greater than the degree of the polynomial, 0 is returned.
	 */
	public RingElement getCoefficient(int index) {
		if (index > getDegree())
			return ring.getZero();
		return coefficients[index];
	}
	
	/**
	 * @return the leading term coefficient of the polynomial.
	 */
	public RingElement getLeadingCoefficient() {
		return coefficients[getDegree() - 1];
	}

	@Override
	public boolean isZero() {
		return getDegree() == -1;
	}

	@Override
	public boolean isOne() {
		return (getDegree() == 0 && coefficients[0].isOne());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("polynomial[");
		for (int i = 0; i < coefficients.length - 1; i++) {
			sb.append(coefficients[i] + ",");
		}
		sb.append(coefficients[coefficients.length - 1] + "]");
		return sb.toString();
	}

	@Override
	public RingElement negative() {
		RingElement[] coefficients = new RingElement[this.coefficients.length];
		for (int i = 0; i < coefficients.length; i++)
			coefficients[i] = this.coefficients[i].negative();
		return new Polynomial(ring, coefficients);
	}
	
	@Override
	public RingElement interpret(Ring r) throws OperationUndefinedException {
		if (coefficients.length == 1)
			return coefficients[0].interpret(r);
		else
			return RingElement.interpret(this, r);
	}
}
