/**
 * Perform some tests on the RationalsField.
 * 
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.test;

import de.phwbrnr.lina.main.fields.ElementParseException;
import de.phwbrnr.lina.main.fields.Fraction;
import de.phwbrnr.lina.main.fields.IntegerElement;
import de.phwbrnr.lina.main.fields.IntegerRing;
import de.phwbrnr.lina.main.fields.OperationUndefinedException;
import de.phwbrnr.lina.main.fields.RationalsField;
import de.phwbrnr.lina.main.fields.Ring;
import de.phwbrnr.lina.main.fields.RingElement;

public class TestRationals extends AbstractTestClass {
	private RationalsField rationals;
	private IntegerRing integers;
	
	public TestRationals() {
		rationals = RationalsField.getInstance();
		integers = IntegerRing.getInstance();
	}
	
	private Fraction makeFraction(int num, int den) throws OperationUndefinedException {
		return new Fraction(new IntegerElement(num), new IntegerElement(den));
	}

	private boolean testArithmetic() {
		try {
			Fraction zero = (Fraction) rationals.getZero();
			Fraction i = (Fraction) rationals.getOne();
			Fraction ii = makeFraction(2, 1);
			RingElement iii = integers.parseElement("3");
			RingElement v = integers.parseElement("5");
			Fraction vii = makeFraction(7, 1);
			Fraction half = makeFraction(1, 2);
			Fraction twohalf = makeFraction(5, 2);
			Fraction fourth = makeFraction(1, 4);
		
			// Addition
			assertThat(i.equals(i), "Identity");
			assertThat(Ring.add(i, i).equals(ii), "1+1=2 by Ring.add");
			assertThat(i.add(i).equals(ii), "1+1=2 by IntegerElement.add");
			assertThat(vii.add(zero).equals(vii), "7+0=7");
			assertThat(!vii.add(i).equals(ii), "7+1 != 2");
			assertThat(zero.negative().equals(zero), "-0 = 0");
			assertThat(i.negative().add(i).equals(zero), "1 + (-1) = 0");
			assertThat(half.add(twohalf).equals(iii), "1/2 + 5/2 = 3");
			
			// Multiplication
			assertThat(i.multiply(i).equals(i), "1*1 = 1");
			assertThat(vii.multiply(zero).equals(zero), "7*0 = 0");
			assertThat(half.multiply(twohalf).equals(Ring.multiply(v, fourth)), "1/2 * 5/2 = 5/4");
			assertThat(integers.contains(half.multiply(ii)), "2 * 1/2 is an integer");
			assertThat(half.multiply(ii).equals(i), "2 * 1/2 = 1");
			
			// Subtraction
			assertThat(i.subtract(i).isZero(), "1-1=0");
			assertThat(twohalf.subtract(half).equals(ii), "5/2 - 1/2 = 2");
		} catch (TestFailedException ex) {
			System.out.println(ex);
			return false;
		} catch (Exception ex) {
			System.out.println("Unexpected exception in testArithmetic: " + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean testSerialize() {
		try {
			Fraction i = (Fraction)rationals.getOne();
			Fraction negx = new Fraction(new IntegerElement(-10), i);
			Fraction threequarter = new Fraction(new IntegerElement(3), new IntegerElement(4));
			
			assertThat(rationals.parseElement(i.toString()).equals(i), "parse(toString()) should be the identity map");
			assertThat(rationals.parseElement(threequarter.toString()).equals(threequarter), "parse(toString()) should be the identity map");
			assertThat(rationals.parseElement("fraction[-10,1]").equals(negx), "Soft creation of fractions and parseElement() should match");
			assertThat(rationals.parseElement("fraction[3,4]").equals(threequarter), "Soft creation of fractions and parseElement() should match");
			try {
				rationals.parseElement("aghfkj");
				System.out.println("Fail: Expected ElementParseException when parsing illegal fraction");
			} catch (ElementParseException ex) {
				System.out.println("Good: Parsing invalid fraction yields ElementParseException");
			}
		} catch (TestFailedException ex) {
			System.out.println(ex);
			return false;
		} catch (Exception ex) {
			System.out.println("Unexpected exception in testSerialize: " + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean testMisc() {
		try {
			Fraction zero = (Fraction) rationals.getZero();
			Fraction ii = makeFraction(2, 1);
			RingElement iii = integers.parseElement("3");
			Fraction half = makeFraction(1, 2);
			Fraction twohalf = makeFraction(5, 2);
			
			/* Contains */
			assertThat(rationals.contains(zero), "0 element of Q");
			assertThat(rationals.contains(iii), "3 element of Q");
			assertThat(rationals.contains(twohalf), "2/5 element of Q");
			
			/* Inverses */
			assertThat(half.invertible(), "1/2 invertible");
			assertThat(half.inverse().equals(ii), "2 is the inverse of 1/2");
			
			/* Division by zero */
			try {
				makeFraction(1, 0);
				System.out.println("Fail: Expected exception when creating fraction 1/0");
			} catch (OperationUndefinedException e) {
				System.out.println("Good: Exception when creating a fraction with denominator zero");
			}
		} catch (TestFailedException ex) {
			System.out.println(ex);
			return false;
		} catch (Exception ex) {
			System.out.println("Unexpected exception in testSerialize: " + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean performTests() {
		boolean result = true;
		
		printHeader("Arithmetic");
		result = result && testArithmetic();
		
		printHeader("Serialization");
		result = result && testSerialize();
		
		printHeader("Misc");
		result = result && testMisc();
		
		return result;
	}

	@Override
	public String getDescription() {
		return "Some tests with the field of the rational numbers Q";
	}

}
