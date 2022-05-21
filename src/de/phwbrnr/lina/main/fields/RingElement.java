/**
 * Represent an element of a ring.
 * This class also defines the ring operations.
 * 
 * Notice that due to the inclusion hierarchy of rings, it is possible to add/multiply etc. elements that are not
 * elements of the same ring according to RingElement.getRing(), for example, when multiplying a ring element with
 * a polynomial over the same ring. The canAdd() method should be used to check for such problems. Alternatively,
 * take a look at Ring.add(), Ring.multiply().
 * 
 * Implementing classes should definitely override equals() and possibly also hashCode().
 * The implementation of toString() should be consistent with Ring.parse().
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

public interface RingElement {
	/**
	 * Add ring elements.
	 * @param addends: Other ring elements to be added to this element
	 * @return The sum as a new RingElement
	 * 
	 * Note: if the addends are not from the same ring, but can be added together anyway, it may be safer to
	 * use the static Ring.add() method as it checks which implementation to use.
	 * 
	 * @throws OperationUndefinedException if one of the addends is not from the same ring.
	 */
	public RingElement add(RingElement... addends) throws OperationUndefinedException;
	
	/**
	 * Subtract a ring element from this element
	 * @throws OperationUndefinedException if the subtrahend is from a different ring. See Ring.subtract().
	 */
	public RingElement subtract(RingElement subtrahend) throws OperationUndefinedException;
	
	/**
	 * Multiply ring elements
	 * @throws OperationUndefinedException if one of the factors is from da different ring. See Ring.multiply().
	 */
	public RingElement multiply(RingElement... factors) throws OperationUndefinedException;
	
	/**
	 * Check if this ring element is divisible by another element.
	 * @return false also if the correct answer is unknown.
	 */
	public boolean divisibleBy(RingElement divisor);
	
	/**
	 * Divide the ring element by another element
	 * @throws OperationUndefinedException if the result is unknown.
	 */
	public RingElement divide(RingElement divisor) throws OperationUndefinedException;
	
	/**
	 * Check if the element is invertible
	 * @return false also if unkown
	 */
	public boolean invertible();
	
	/**
	 * Compute the inverse of the ring element.
	 * @throws ElementNotInvertibleException if the element is not invertible
	 */
	public RingElement inverse() throws ElementNotInvertibleException;
	
	/**
	 * Compute the negative (the inverse under addition) of the ring element.
	 * Must not throw OperationUndefinedException, because all ring elements have negative elements.
	 */
	public RingElement negative();
	
	/**
	 * Check if another ring element can be added to this element.
	 * @param other
	 * @return False if the other element is not a member of the same ring.
	 * This need not be symmetric, see Ring.add().
	 */
	public boolean canAdd(RingElement other);
	
	/**
	 * Check if another ring element can be multiplied with this element.
	 */
	public boolean canMultiply(RingElement other);
	
	/**
	 * Return an instance of the ring this element is member of.
	 * This method is agnostic of ring inclusion hierarchy, so the answer may or may not be helpful.
	 */
	public Ring getRing();
	
	/**
	 * @return true, if the element is the neutral element of addition in the ring.
	 */
	public boolean isZero();
	
	/**
	 * @return true, if the element is the neutral element of multiplication in the ring.
	 */
	public boolean isOne();
	
	public String toString();
	
	public boolean equals(Object other);
	
	/**
	 * Interpret this Ring element as an element of another ring.
	 * For example, a fraction 2/1 is considered as a rational, but not as an integer, but this method
	 * allows implementing classes to bring awareness of such inclusions into the system.
	 * 
	 * The default implementation only admits re-interpretations of the 0 and 1 elements of a ring.
	 * 
	 * If a implementing class wants to "chain up" to the default implementation, it may use the static
	 * method interpret(RingElement, Ring).
	 * 
	 * @param ring The ring the element should be interpreted in
	 * @throws OperationUndefinedException if the interpretation is not possible or not implemented.
	 */
	public default RingElement interpret(Ring ring) throws OperationUndefinedException {
		return RingElement.interpret(this, ring);
	}
	
	/**
	 * Interpret a ring element as element of another ring.
	 * Like the other non-static implementation
	 */
	public static RingElement interpret(RingElement element, Ring ring) throws OperationUndefinedException {
		if(element.getRing().equals(ring))
			return element;
		
		if(element.isZero())
			return ring.getZero();
		else if (element.isOne())
			return ring.getOne();
		else
			throw new OperationUndefinedException("Cannot interpret element '" + element.toString() + "' of ring " + element.getRing() + " as element of ring " + ring.toString() + " by default");
	}
}
