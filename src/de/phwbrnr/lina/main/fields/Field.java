/**
 * Represent a field.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

public abstract class Field implements Ring {

	@Override
	public final boolean isCommutative() {
		return true;
	}

	@Override
	public final boolean isIntegralDomain () {
		return true;
	}

}
