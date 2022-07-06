/**
 * Singleton that represents the ring of the integers; its elements are represented by the IntegerElement class.
 * 
 * @see IntegerElement
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.fields;

import java.util.ArrayList;
import java.util.LinkedList;

import de.phwbrnr.lina.main.strategies.Computation;
import de.phwbrnr.lina.main.strategies.Strategy;

public class IntegerRing implements Ring, EuclideanRing {
	static IntegerRing instance = null;
	private IntegerRing () {
		
	}
	
	public static IntegerRing getInstance () {
		if (instance == null)
			instance = new IntegerRing();
		return instance;
	}

	@Override
	public RingElement getZero() {
		return new IntegerElement(0);
	}

	@Override
	public RingElement getOne() {
		return new IntegerElement(1);
	}

	@Override
	public RingElement parseElement(String string) throws ElementParseException {
		try {
			Integer val = Integer.parseInt(string);
			return new IntegerElement(val);
		} catch (NumberFormatException ex) {
			throw new ElementParseException("Unable to parse string '" + string + "' as IntegerElement");
		}
	}
	
	public String getName() {
		return "Z";
	}

	@Override
	public boolean contains(RingElement el) {
		return el.getRing().equals(this);
	}

	public int degree (RingElement el) throws OperationUndefinedException {
		if (!(el instanceof IntegerElement))
			throw new IllegalArgumentException("Element " + el.toString() + " is not an integer");
		if (el.isZero())
			throw new OperationUndefinedException("The zero element has undefined degree");

		return Math.abs(((IntegerElement)el).getValue());
	}

	public RingElement[] remainder_division (RingElement dividend, RingElement divisor) throws OperationUndefinedException {
		if (!(contains(dividend) && contains(divisor)))
			throw new OperationUndefinedException("Dividend " + dividend.toString() + " and divisor " + divisor.toString() + " have to be elements of the integers");

		int divid = ((IntegerElement)dividend).getValue();
		int divis = ((IntegerElement)divisor).getValue();

		int remainder = divid % divis;
		int quotient = (divid - remainder) / divis;

		return new RingElement[] { new IntegerElement(quotient), new IntegerElement(remainder) };
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isIntegralDomain() {
		return true;
	}

	@Override
	public boolean irreducible(RingElement el) throws OperationUndefinedException {
		RingElement ell = el.interpret(getInstance());
		if(!(ell instanceof IntegerElement))
			throw new OperationUndefinedException("Element " + ell + " can be interpreted as integer, but is not an instance of IntegerElement");
		
		IntegerElement i = (IntegerElement)ell;
		int j = Math.abs(i.getValue());
		for (int k = 2; k < (int)Math.pow(j, 0.5); k++)
			if (j % k == 0)
				return false;
		
		return true;
	}

	@Override
	public Computation<ArrayList<RingElement>> factor() throws OperationUndefinedException {
		return IntegerFactorComputation.getInstance();
	}
	
	public static class IntegerFactorComputation extends Computation<ArrayList<RingElement>> {
		private LinkedList<Strategy<ArrayList<RingElement>>> strategies;
		private static IntegerFactorComputation instance;
		
		public static IntegerFactorComputation getInstance() {
			if(instance == null)
				instance = new IntegerFactorComputation();
			return instance;
		}
		
		private IntegerFactorComputation() {
			strategies.add(new Strategy<ArrayList<RingElement>> () {
				@Override
				public String getDescription() {
					return "Compute prime factors of an integer n by checking all values up to sqrt(n)";
				}

				@Override
				public boolean appliesTo(Object... problem) {
					return (problem.length == 1 && problem[0] instanceof IntegerElement);
				}

				@Override
				public int expectedCost(Object... problem) {
					if(appliesTo(problem))
						return 10;
					else
						return 100;
				}

				@Override
				public ArrayList<RingElement> execute(Object... problem) {
					if(!appliesTo(problem))
						throw new IllegalArgumentException("Unapplicable problem instance. Only pass a single IntegerElement as problem instance");
					
					IntegerElement i;
					try {
						i = (IntegerElement)((RingElement)problem[0]).interpret(IntegerRing.getInstance());
					} catch (OperationUndefinedException ex) {
						throw new IllegalArgumentException("Unable to convert element to factor (" + problem[0].toString() + ") as IntegerElement", ex);
					}
					int number = i.getValue();

					ArrayList<RingElement> factors = new ArrayList<RingElement>();
					
					for (int k = 0; k < (int)Math.pow(number, 0.5); k++) {
						if (k % number == 0) {
							int other = number / k;
							factors.add(new IntegerElement(k));
							number = other;
						}

						if (number == 1 || number == -1 || number == 0)
							break;
					}
					
					return factors;
				}
				
			});
		}

		@Override
		public String getDescription() {
			return "Compute prime factorization of an integer";
		}

		@SuppressWarnings("unchecked")
		@Override
		public Strategy<ArrayList<RingElement>>[] getStrategies() {
			return (Strategy<ArrayList<RingElement>>[]) strategies.toArray();
		}

		@Override
		public void addStrategy(Strategy<ArrayList<RingElement>> strategy) {
			strategies.add(strategy);
		}
		
	}
}
