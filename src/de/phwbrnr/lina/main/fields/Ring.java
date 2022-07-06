/**
 * Represent a Ring.
 * 
 * @author Philipp Weinbrenner
 * @see EuclideanRing - a ring structure over which we have a generalized division with remainder.
 */
package de.phwbrnr.lina.main.fields;

import java.util.ArrayList;

import de.phwbrnr.lina.main.strategies.Computation;

public interface Ring {
	/**
	 * String representation of the ring, such as "R" for the real numbers.
	 */
	public abstract String getName();
	
	/**
	 * @return An instance of the ring's neutral element of addition.
	 */
	public abstract RingElement getZero();
	
	/**
	 * @return An instance of the ring's neutral element of multiplication.
	 */
	public abstract RingElement getOne();
	
	/**
	 * Find out whether the ring (that means, its multiplication) is commutative
	 */
	public abstract boolean isCommutative();
	
	/**
	 * Find out whether the ring is an integral domain. A ring is an integral domain if it is commutative
	 * and there are no non-trivial divisors of zero.
	 */
	public abstract boolean isIntegralDomain ();
	
	/**
	 * Parse the string representation of an element of the ring.
	 * @param string: String represenation of an element of the ring; should be consistent with the according
	 * RingElement.toString() implementation.
	 * @throws ElementParseException if the string cannot be interpreted as an element of the ring.
	 */
	public abstract RingElement parseElement(String string) throws ElementParseException;
	
	/**
	 * Add a number of ring elements.
	 * @param addends: The elements to add. Must be at least a single element.
	 * @return The sum
	 * 
	 * This method may be preferred to RingElement.add, because RingElement.add() is agnostic of
	 * ring inclusion, and may fail if, for example, we add a ring element to a polynomial over the same ring,
	 * whereas adding a polynomial to a ring element may success; this method automatically tries to determine
	 * a strategy for adding.
	 * This, however, does not work, when we add an element of F_2, the field with two elements, to an element of F_3,
	 * because neither is a subring of the other.
	 * 
	 * @throws OperationUndefinedException if no strategy to add the elements can be found.
	 */
	public static RingElement add(RingElement... addends) throws OperationUndefinedException {
		RingElement result = addends[0].getRing().getZero();
		for (int i = 0; i < addends.length; i++)
			result = Ring.addtwo(result, addends[i]);
		
		return result;
	}
	
	/**
	 * A helper function for add.
	 */
	private static RingElement addtwo(RingElement a1, RingElement a2) throws OperationUndefinedException {
		if(a1.canAdd(a2))
			return a1.add(a2);
		else if(a2.canAdd(a1))
			return a2.add(a1);
		else
			throw new OperationUndefinedException("Cannot add ring element" + a1.toString() + " of ring " + a1.getRing() + " to element " + a2.toString() + " of ring " + a2.getRing());
	}
	
	/**
	 * Subtract a ring element from another.
	 * Internally, this uses minuend.subtract().
	 * @param minuend
	 * @param subtrahend
	 * @return REsult of the computation as new RingElement
	 * @throws OperationUndefinedException if the elements are not in the same ring
	 */
	public static RingElement subtract (RingElement minuend, RingElement subtrahend) throws OperationUndefinedException {
		return minuend.subtract(subtrahend);
	}
	
	/**
	 * Multiply a number of ring elements.
	 * @param factors: The elements to multiply. Must be at least a single element.
	 * @return The sum
	 * 
	 * This method may be preferred to RingElement.multiply, because RingElement.multiply() is agnostic of
	 * ring inclusion, and may fail if, for example, we add a ring element to a polynomial over the same ring,
	 * whereas adding a polynomial to a ring element may success; this method automatically tries to determine
	 * a strategy for adding.
	 * This, however, does not work, when we multiply an element of F_2, the field with two elements, to an element of F_3,
	 * because neither is a subring of the other.
	 * 
	 * @throws OperationUndefinedException if no strategy can be found.
	 */
	public static RingElement multiply(RingElement... factors) throws OperationUndefinedException {
		RingElement result = factors[0].getRing().getOne();
		for (int i = 0; i < factors.length; i++)
			result = Ring.multiplytwo(result, factors[i]);
		
		return result;
	}
	
	/**
	 * A helper function for multiply.
	 */
	private static RingElement multiplytwo(RingElement a1, RingElement a2) throws OperationUndefinedException {
		if(a1.canMultiply(a2))
			return a1.multiply(a2);
		else if(a2.canMultiply(a1))
			return a2.multiply(a1);
		else
			throw new OperationUndefinedException("Cannot multiply ring element" + a1.toString() + " of ring " + a1.getRing() + " and element " + a2.toString() + " of ring " + a2.getRing());
	}
	
	/**
	 * Checks whether the implementation of Ring considers a given RingElement a member of itself.
	 */
	public abstract boolean contains(RingElement el);
	
	/**
	 * Determine if the given Ring Element is irreducible over this ring.
	 * A ring element x is irreducible if for all decompositions x = a * b over the ring, one of the factors is
	 * invertible; in particular, 0 and the invertible elements are irreducible.
	 * 
	 * @throws OperationUndefinedException if the element cannot be interpreted as element of this ring.
	 */
	public abstract boolean irreducible(RingElement el) throws OperationUndefinedException;
	
	/**
	 * Return a Computation object that represents the computation of factoring a given ring element
	 * into irreducible factors.
	 * Over a general ring, factorization is not unique and in fact only makes sense over an integral domain.
	 * An invertible element will not be factored into other factors.
	 * @returns a FactorComputation object whose strategies return the factorization as an ArrayList of RingElements. 
	 * @throws OperationUndefinedException if the ring is not an integral domain, or if there is no strategy available
	 */
	public abstract Computation<ArrayList<RingElement>> factor() throws OperationUndefinedException;
}
