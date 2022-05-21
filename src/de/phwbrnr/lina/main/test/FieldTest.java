/**
 * Perform tests on the subpackage main.fields. Can be aggregated further through AbstractTestClass or invoked directly
 * 
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.test;

public class FieldTest extends AbstractTestClass {
	private AbstractTestClass[] tests;
	
	public FieldTest() {
		tests = new AbstractTestClass[] {
				new TestIntegers(),
				new TestRationals()
		};
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FieldTest test = new FieldTest();
		
		boolean result = test.performTests();
		System.out.println();
		
		if(result)
			System.out.println("FieldTest: All Tests okay");
		else
			System.out.println("FieldTest: Some tests failed");
	}

	@Override
	public boolean performTests() {
		boolean result = true;
		for(int i = 0; i < tests.length; i++) {
			printHeader("FieldTest [#" + i + "]: " + tests[i].getDescription());
			result = result & tests[i].performTests();
		}
		return result;
	}

	@Override
	public String getDescription() {
		return "Perform tests on de.phwbrnr.lina.main.fields";
	}

}
