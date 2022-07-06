/**
 * Represent the polynomial ring over a ring.
 * For polynomial rings over a field, it is better to use FieldPolynomialRing, because not all Polynomial Rings are
 * euclidean rings, but those over fields are.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.polynomials;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.phwbrnr.lina.main.fields.ElementParseException;
import de.phwbrnr.lina.main.fields.NotImplementedException;
import de.phwbrnr.lina.main.fields.OperationUndefinedException;
import de.phwbrnr.lina.main.fields.Ring;
import de.phwbrnr.lina.main.fields.RingElement;
import de.phwbrnr.lina.main.strategies.Computation;
import de.phwbrnr.lina.main.strategies.Uncomputation;

public class PolynomialRing implements Ring {
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
		
		Ring cr = ((PolynomialRing)other).getCoefficientRing();
		return cr.equals(ring);
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

	@Override
	public boolean irreducible(RingElement el) throws OperationUndefinedException {
		throw new NotImplementedException("The default PolynomialRing does not know about irreducible elements");
	}

	@Override
	public Computation<ArrayList<RingElement>> factor() {
		return new Uncomputation<ArrayList<RingElement>>("The default PolynomialRing does not know about factorization");
	}
}
