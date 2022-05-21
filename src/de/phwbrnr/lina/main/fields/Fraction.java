/**
 * Represent a formal fraction over an euclidean ring.
 * Formal fractions can be defined over arbitrary integral domains, but for hashCode() we need a normal form
 * (shortening of fractions), for which we wish to use only euclidean rings. 
 * 
 * @author Philipp Weinbrenner
 * @version 2022-10-05
 */
package de.phwbrnr.lina.main.fields;

public class Fraction implements RingElement {
	private Ring coefficientRing;
	private RingElement numerator;
	private RingElement denominator;
	
	/**
	 * The fraction will be shortened using the Euclidean algorithm.
	 * TODO enable numerator and denominator to be fractions
	 * @param numerator
	 * @param denominator
	 * @throws IllegalArgumentException if numerator and denominator are not elements of the same ring,
	 * or if the ring is not Euclidean.
	 * @throws OperationUndefinedException if denominator vanishes.
	 */
	public Fraction(RingElement numerator, RingElement denominator) throws OperationUndefinedException {
		if (denominator.isZero())
			throw new OperationUndefinedException("The denominator must not vanish");
		
		/* find the coefficient ring */
		if (numerator.getRing().contains(denominator))
			coefficientRing = numerator.getRing();
		else if (denominator.getRing().contains(numerator))
			coefficientRing = denominator.getRing();
		else
			throw new IllegalArgumentException("Cannot find a ring that contains both numerator " + numerator.toString() + " and denominator " + denominator.toString());
		
		if (!(coefficientRing instanceof EuclideanRing))
			throw new IllegalArgumentException("Only accepting numerator and denominator from Euclidean Rings, but " + coefficientRing.toString() + " is not Euclidean");
		
		try {
			numerator = numerator.interpret(coefficientRing);
			denominator = denominator.interpret(coefficientRing);
		} catch (OperationUndefinedException ex) {
			throw new OperationUndefinedException("Cannot interpret numerator " + numerator.toString()
				+ " and denominator " + denominator.toString() + " as elements of "
				+ coefficientRing.getName(), ex);
		}
		
		try {
			EuclideanRing ring = (EuclideanRing)coefficientRing;
			RingElement gcd = ring.gcd(numerator, denominator);
			this.numerator = numerator.divide(gcd);
			this.denominator = denominator.divide(gcd);
		} catch (OperationUndefinedException e) {
			throw new Error("Programming error: Shortening the fraction fails", e);
		}
	}
	
	/**
	 * Create a Fraction that is just the numerator.
	 * @param numerator
	 */
	public Fraction(RingElement numerator) {
		this.coefficientRing = numerator.getRing();
		this.numerator = numerator;
		this.denominator = coefficientRing.getOne();
	}

	@Override
	public RingElement add(RingElement... addends) throws OperationUndefinedException {
		Fraction result = this;
		
		for (int i = 0; i < addends.length; i++) {
			if (!canAdd(addends[i]))
				throw new OperationUndefinedException("Cannot add " + addends[i].toString() + " to fraction over " + coefficientRing.getName());
			
			if (addends[i] instanceof Fraction) {
				Fraction other = (Fraction)addends[i];
			
				RingElement newnumerator = Ring.add(Ring.multiply(result.numerator, other.denominator),
						Ring.multiply(result.denominator, other.numerator));
				RingElement newdenominator = Ring.multiply(result.denominator, other.denominator);
				result = new Fraction(newnumerator, newdenominator);
			} else {
				result = new Fraction(Ring.add(result.numerator, Ring.multiply(addends[i], result.denominator)), result.denominator);
			}
		}
		
		return result;
	}

	@Override
	public RingElement subtract(RingElement other) throws OperationUndefinedException {
		if (!canAdd(other))
			throw new OperationUndefinedException("Cannot add " + other.toString() + " to fraction over " + coefficientRing.getName());
		
		Fraction subtrahend = (Fraction)other;
		
		RingElement newnumerator = Ring.subtract(Ring.multiply(numerator, subtrahend.denominator),
				Ring.multiply(denominator, subtrahend.numerator));
		RingElement newdenominator = Ring.multiply(denominator, subtrahend.denominator);
		return new Fraction(newnumerator, newdenominator);
	}

	@Override
	public RingElement multiply(RingElement... factors) throws OperationUndefinedException {
		Fraction result = this;
		
		for (int i = 0; i < factors.length; i++) {
			if (!canAdd(factors[i]))
				throw new OperationUndefinedException("Cannot add " + factors[i].toString() + " to fraction over " + coefficientRing.getName());
			
			if (factors[i] instanceof Fraction) {
				Fraction other = (Fraction)factors[i];
			
				RingElement newnumerator = Ring.multiply(result.numerator, other.numerator);
				RingElement newdenominator = Ring.multiply(result.denominator, other.denominator);
				result = new Fraction(newnumerator, newdenominator);
			} else {
				result = new Fraction(Ring.multiply(factors[i], result.numerator), result.denominator);
			}
		}
		
		return result;
	}

	@Override
	public boolean divisibleBy(RingElement divisor) {
		return (canMultiply(divisor) && !divisor.isZero());
	}

	@Override
	public RingElement divide(RingElement divisor) throws OperationUndefinedException {
		try {
			return multiply(divisor.inverse());
		} catch (ElementNotInvertibleException ex) {
			throw new OperationUndefinedException("Divisor " + divisor.toString() + " not invertible");
		}
	}

	@Override
	public boolean invertible() {
		return !(isZero());
	}

	@Override
	public RingElement inverse() throws ElementNotInvertibleException {
		if (invertible()) {
			try {
				return new Fraction(denominator, numerator);
			} catch (OperationUndefinedException ex) {
				throw new Error("A validly constructed fraction (" + toString() + " over " + coefficientRing.getName() + ") should be invertible", ex);
			}
		} else
			throw new ElementNotInvertibleException("A fraction representing zero is not invertible");
	}

	@Override
	public boolean canAdd(RingElement other) {
		if (other instanceof Fraction) {
			Fraction f = (Fraction)other;
			return f.coefficientRing.equals(coefficientRing);
		} else {
			return coefficientRing.contains(other);
		}
	}

	@Override
	public boolean canMultiply(RingElement other) {
		return canAdd(other);
	}

	@Override
	public Ring getRing() {
		if(denominator.isOne())
			return coefficientRing;
		else
			return new QuotientField(coefficientRing);
	}

	@Override
	public boolean isZero() {
		return numerator.isZero();
	}

	@Override
	public boolean isOne() {
		return numerator.equals(denominator);
	}

	public RingElement getNumerator() {
		return numerator;
	}

	public RingElement getDenominator() {
		return denominator;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof RingElement))
			return false;
		
		RingElement ro = (RingElement)other;
		if(!(other instanceof Fraction)) {
			if (!coefficientRing.contains(ro))
				return false;
			
			try {
				return Ring.multiply(ro, denominator).equals(numerator);
			} catch (OperationUndefinedException ex) {
				return false;
			}
		}
		
		Fraction fo = (Fraction)other;
		
		if (!canMultiply(fo))
			return false;
		
		try {
			return denominator.multiply(fo.numerator).equals(numerator.multiply(fo.denominator));
		} catch (OperationUndefinedException ex) {
			throw new Error("Fraction " + toString() + " and " + fo.toString() + " were supposed to be multiplieable, but multiplication of coefficients is undefined", ex);
		}
	}
	
	/**
	 * The hashCode contract should be fulfilled, since the constructor shortens the fraction and brings it into normal form.
	 */
	@Override
	public int hashCode () {
		return denominator.hashCode() + numerator.hashCode();
	}
	
	@Override
	public String toString() {
		return "fraction[" + numerator.toString() + "," + denominator.toString() + "]";
	}

	@Override
	public RingElement negative() {
		try {
			return new Fraction(numerator.negative(), denominator);
		} catch(OperationUndefinedException ex) {
			throw new Error("Each ring element has an additive inverse, unexpected exception", ex);
		}
	}
	
	@Override
	public RingElement interpret (Ring ring) throws OperationUndefinedException {
		if (denominator.isOne())
			return numerator.interpret(ring);
		else
			return RingElement.interpret(denominator, ring);
	}
}
