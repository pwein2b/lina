/**
 * Represent a Euclidean Ring.
 * A Euclidean Ring R is an integral domain with a function d from R\{0} to the natural numbers (degree function or Euclidean function), such that for an element a and a nonzero element b, there exist elements p, q, such that a = pb + q and d(q) is smaller than d(b) or q = 0.
 * The Euclidean Degree function is implemented through degree(), and the above generalized division with remainder by remainder_division()
 *
 * The division with remainder induces the possibility of determining the greatest common divisor and least common multiple of ring elements through the euclidean algorithm in default methods gcd, lcm; a subclass should override these iff. it provides a more efficient implementation.
 *
 * @author Philipp Weinbrenner
 * @version 2022-05-12
 */

public abstract class EuclideanRing extends Ring {
	@Override
	public final boolean isCommutative() {
		return true;
	}

	@Override
	public abstract boolean isIntegralDomain () {
		return true;
	}

	/**
	 * Compute the euclidean degree of a nonzero ring element.
	 * @throws OperationUndefinedException if the ring element is zero
	 * @throws IllegalArgumentException if the argument is not an element of the ring
	 */
	public abstract int degree(RingElement element) throws OperationUndefinedException;

	/**
	 * Compute ring elements p, q such that dividend = p * divisor + q, and degree(q) is smaller than the degree of divisor.
	 * @param dividend The ring element to decompose.
	 * @param divisor The divisor, which must not be null.
	 * @return The pair (p,q), where p is the quotient and q the remainder of the division.
	 * @throws OperationUndefinedException If one of the arguments is not an element of the ring, or if divisor vanishes.
	 */
	public abstract RingElement[2] remainder_division (RingElement dividend, RingElement divisor) throws OperationUndefinedException;

	/**
	 * Compute the greatest common divisor of the arguments by the euclidean algorithm.
	 * A gcd is not necessarily uniquely determined. A gcd is a ring element of maximum degree which divides both of the arguments, and all common divisors of the arguments divide the gcd.
	 * A subclass should override this method if it can provide a solution that is more efficient than the standard implementation.
	 * @throws OperationUndefinedException If one of the operations employed in the algorithm fails.
	 */
	public RingElement gcd (RingElement a, RingElement b) throws OperationUndefinedException {
		if (a.isZero())
			return b;
		if (b.isZero())
			return a;

		RingElement dividend = null, divisor = null;
		if (degree(a) > degree(b)) {
			dividend = a;
			divisor = b;
		} else {
			dividend = b;
			divisor = a;
		}

		RingElement[2] division_result = remainder_division(dividend, divisor);
		return gcd(division_result[1], divisor);
	}

	/**
	 * Compute the least common multiply of the arguments.
	 * The computation uses the gcd() method.
	 * Subclasses should override if they can provide a more efficient implementation.
	 *
	 * A least common divisor of the arguments (in general, not unique) is a ring element that is a multiply of both elements such that all other common multiples of the arguments are also multiples of the lcm.
	 *
	 * @throws OperationUndefinedException if one of the methods used fails.
	 */
	public RingElement lcm (RingElement a, RingElement b) throws OperationUndefinedException {
		RingElement gcd = this.gcd(a, b);
		RingElement quotient = remainder_division(a, gcd)[1];
		return Ring.multiply(quotient, b);
	}
}

