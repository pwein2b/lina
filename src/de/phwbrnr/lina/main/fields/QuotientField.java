/**
 * Represent the Quotient Field over an integral domain.
 * 
 * The quotient field over in integral domain I is the set of formal fractions p/q, where p and q are elements of I,
 * and q does not vanish; multiplication, addition and equality are defined analogously to the rational numbers Q.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuotientField extends Field {
	private Ring coefficientRing;
	
	public QuotientField(Ring integralDomain) {
		if (!integralDomain.isIntegralDomain())
			throw new IllegalArgumentException("The Quotient Field is only defined over an integral domain");
		
		this.coefficientRing = integralDomain;
	}
	
	@Override
	public String getName() {
		if (coefficientRing instanceof PolynomialRing)
			return ((PolynomialRing) coefficientRing).getCoefficientRing().getName() + "(X)";
		else
			return "quotientField(" + coefficientRing.getName() + ")";
	}

	@Override
	public RingElement getZero() {
		return new Fraction(coefficientRing.getZero());
	}

	@Override
	public RingElement getOne() {
		return new Fraction(coefficientRing.getOne());
	}

	@Override
	public RingElement parseElement(String string) throws ElementParseException {
		Pattern p = Pattern.compile("fraction\\[(.*)\\]");
		Matcher m = p.matcher(string);
		if(!m.matches())
			throw new ElementParseException("Fraction strings have to be of the form 'fraction[numerator,denominator]'");
		
		String[] elements = m.group(1).split(",");
		RingElement[] coeffs = new RingElement[elements.length];
		for (int i = 0; i < elements.length; i++)
			coeffs[i] = coefficientRing.parseElement(elements[i]);
		
		return new Polynomial(coefficientRing, coeffs);
	}

	@Override
	public boolean contains(RingElement el) {
		if (el instanceof Fraction) {
			Fraction f = (Fraction)el;
			return coefficientRing.contains(f.getNumerator()) && coefficientRing.contains(f.getDenominator());
		}
		return coefficientRing.contains(el); 
	}

}
