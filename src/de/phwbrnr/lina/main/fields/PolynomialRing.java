/**
 * Represent the polynomial ring over a ring.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolynomialRing extends Ring {
	private Ring ring;
	
	public PolynomialRing(Ring ring) {
		this.ring = ring;
	}

	@Override
	public String getName() {
		return ring.getName() + "[X]";
	}

	@Override
	public RingElement getZero() {
		return new Polynomial(ring);
	}

	@Override
	public RingElement getOne() {
		return new Polynomial(ring, ring.getOne());
	}

	@Override
	public RingElement parseElement(String string) throws ElementParseException {
		Pattern p = Pattern.compile("polynomial\\[(.*)\\]");
		Matcher m = p.matcher(string);
		if(!m.matches())
			throw new ElementParseException("Polynomial strings have to be of the form 'polynomial[coeff0,coeff1,...]'");
		
		String[] elements = m.group(1).split(",");
		RingElement[] coeffs = new RingElement[elements.length];
		for (int i = 0; i < elements.length; i++)
			coeffs[i] = ring.parseElement(elements[i]);
		
		return new Polynomial(ring, coeffs);
	}

	@Override
	public boolean contains(RingElement el) {
		if (el instanceof Polynomial) {
			Polynomial elp = (Polynomial)el;
			return elp.getRing().equals(this);
		} else {
			return ring.contains(el);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PolynomialRing))
			return false;
		
		return ((PolynomialRing)other).getCoefficientRing().equals(ring);
		
		/* TODO equals has to be symmetric */
	}
	
	/**
	 * @return An instance of the ring the coefficients are from.
	 */
	public Ring getCoefficientRing() {
		return ring;
	}

	@Override
	public boolean isCommutative() {
		return ring.isCommutative();
	}

	@Override
	public boolean isIntegralDomain() {
		return ring.isIntegralDomain();
	}
}
