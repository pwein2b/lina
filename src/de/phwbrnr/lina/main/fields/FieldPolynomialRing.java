/**
 * Represent a Polynomial Ring over a Field.
 * 
 * This class inherits most functions from PolynomialRing, but warrants to be a class on its own, because
 * Polynomial Rings over Fields are Euclidean Rings.
 * 
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.fields;

public class FieldPolynomialRing extends PolynomialRing implements EuclideanRing {

	public FieldPolynomialRing(Ring ring) {
		super(ring);
		if(!(ring instanceof Field))
			throw new IllegalArgumentException("FieldPolynomialRing only works for Polynomials from fields. Try PolynomialRing instead for " + ring.toString());
	}

	@Override
	public int degree(RingElement element) throws OperationUndefinedException {
		if(!(element instanceof Polynomial)) {
			if(element.isZero())
				return -1;
			else
				return 0;
		}
		
		Polynomial p = (Polynomial)element;
		return p.getDegree();
	}

	@Override
	public RingElement[] remainder_division(RingElement divid, RingElement divis)
			throws OperationUndefinedException {
		if(divis.isZero())
			throw new OperationUndefinedException("Division by zero not possible");
		
		Polynomial remainder = (Polynomial)divid, divisor = (Polynomial)divis;
		Polynomial quotient = null;
		Field coefficientField = (Field)remainder.getRing();
		
		while(degree(remainder) > degree(divisor)) {
			int newdeg = degree(remainder) - degree(divisor);
			RingElement factor = remainder.getLeadingCoefficient().divide(divisor.getLeadingCoefficient());
			Polynomial new_quotient = new Polynomial(coefficientField, newdeg, factor);
			
			remainder = remainder.subtract(new_quotient.multiply(divisor));
			quotient = quotient.add(new_quotient);
		}
		
		return new RingElement[] {quotient, remainder};
	}

}
