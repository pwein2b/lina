/**
 * Test the IntegerRing class from the fields package
 * 
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.test;

import de.phwbrnr.lina.main.fields.ElementParseException;
import de.phwbrnr.lina.main.fields.IntegerElement;
import de.phwbrnr.lina.main.fields.IntegerRing;
import de.phwbrnr.lina.main.fields.Ring;

public class TestIntegers extends AbstractTestClass {
	IntegerRing integers;
	
	public TestIntegers() {
		integers = IntegerRing.getInstance();
	}
	
	private boolean testArithmetic() {
		try {
			IntegerElement zero = (IntegerElement) integers.getZero();
			IntegerElement i = (IntegerElement) integers.getOne();
			IntegerElement ii = (IntegerElement) integers.parseElement("2");
			IntegerElement vii = (IntegerElement) integers.parseElement("7");
		
			assertThat(i.equals(i), "Identity");
			assertThat(Ring.add(i, i).equals(ii), "1+1=2 by Ring.add");
			assertThat(i.add(i).equals(ii), "1+1=2 by IntegerElement.add");
			assertThat(vii.add(zero).equals(vii), "7+0=7");
			assertThat(!vii.add(i).equals(ii), "7+1 != 2");
			assertThat(zero.negative().equals(zero), "-0 = 0");
			assertThat(i.negative().add(i).equals(zero), "1 + (-1) = 0");
			
			// Multiplication
			assertThat(i.multiply(i).equals(i), "1*1 = 1");
			assertThat(vii.multiply(zero).equals(zero), "7*0 = 0");
			
			// Subtraction
			assertThat(i.subtract(i).isZero(), "1-1=0");
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
			IntegerElement i = (IntegerElement)integers.getOne();
			IntegerElement negx = new IntegerElement(-10);
			
			assertThat(integers.parseElement(i.toString()).equals(i), "parse(toString()) should be the identity map");
			assertThat(integers.parseElement("-10").equals(negx), "Soft creation of integers and parseElement() should match");
			try {
				integers.parseElement("aghfkj");
				System.out.println("Fail: Expected ElementParseException when parsing illegal integre");
			} catch (ElementParseException ex) {
				System.out.println("Correct ElementParseException");
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
	
	private boolean testDivision() {
		try {
			IntegerElement zero = (IntegerElement)integers.getZero();
			IntegerElement i = (IntegerElement)integers.getOne();
			IntegerElement negi = (IntegerElement)integers.getOne().negative();
			IntegerElement ii = (IntegerElement) integers.parseElement("2");
			IntegerElement v = (IntegerElement) integers.parseElement("5");
			IntegerElement viii = (IntegerElement) integers.parseElement("8");
			
			assertThat(!i.divisibleBy(zero), "Division by zero not possible");
			try {
				i.divide(zero);
				System.out.println("Fail: Division by zero should throw an Exception!");
			} catch (Exception ex) {
				System.out.println("Good: Division by zero throws " + ex.toString());
			}
			assertThat(!v.divisibleBy(viii), "8 does not divide 5");
			assertThat(v.divisibleBy(i), "1 divides all integers");
			assertThat(viii.divisibleBy(ii), "2 divides 8");
			assertThat(viii.divide(ii).divide(ii).equals(ii), "8/2/2 = 2");
			assertThat(i.divide(negi).equals(negi), "1/(-1) = -1");
			
			/* Inverses */
			assertThat(negi.invertible(), "-1 is invertible");
			assertThat(!viii.invertible(), "8 is not invertible");
			assertThat(i.inverse().equals(i), "1^-1 = 1");
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
			IntegerElement zero = (IntegerElement)integers.getZero();
			IntegerElement i = (IntegerElement)integers.getOne();
			IntegerElement negi = (IntegerElement)integers.getOne().negative();
			IntegerElement ii = (IntegerElement) integers.parseElement("2");
			IntegerElement v = (IntegerElement) integers.parseElement("5");
			
			/* Contains */
			assertThat(integers.contains(zero), "0 element of Z");
			assertThat(integers.contains(negi), "-1 element of Z");
			
			/* Euclidean Degree */
			assertThat(integers.degree(v) == 5, "Euclidean degree of 5 is 5");
			
			/* GCD and remainder division */
			assertThat(integers.gcd(ii, v).equals(i), "gcd(2,5) = 1");
			assertThat(integers.remainder_division(v, ii)[1].equals(i), "5 modulo 2 = 1");
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
		
		printHeader("Test Addition, Subtraction, Multiplication");
		result = result && testArithmetic();
		
		printHeader("Test serialization");
		result = result && testSerialize();
		
		printHeader("Test division");
		result = result && testDivision();
		
		printHeader("Test miscelleanous functions");
		result = result && testMisc();
		
		return result;
	}

	@Override
	public String getDescription() {
		return "Test operations on the Ring of Integers";
	}
}
