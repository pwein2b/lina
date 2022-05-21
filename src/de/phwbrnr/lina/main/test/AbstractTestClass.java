/**
 * Represent an abstract test class
 */
package de.phwbrnr.lina.main.test;

public abstract class AbstractTestClass {
	/**
	 * Perform the tests defined in the test class
	 * @return true iff all checks passed
	 */
	public abstract boolean performTests();
	
	/**
	 * @return a string description of the test class
	 */
	public abstract String getDescription();
	
	/**
	 * Assert a statement and throw TestFailedException if it does not hold
	 */
	public static void assertThat(boolean statement) throws TestFailedException {
		assertThat(statement, "");
	}
	
	/**
	 * Assert a statement and throw TestFailedException if it does not hold
	 */
	public static void assertThat(boolean statement, String message) throws TestFailedException {
		if(statement)
			System.out.println("Test OK: " + message);
		else
			throw new TestFailedException("Test failed: " + message);
	}
	
	public static class TestFailedException extends Exception {
		private static final long serialVersionUID = 1L;

		public TestFailedException(String msg) {
			super(msg);
		}
		
		public TestFailedException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
	
	public void printHeader(String header) {
		System.out.println();
		System.out.println(header);
		System.out.println("=========");
	}
}
