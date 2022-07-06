/**
 * Lina values and objects can be serialized and deserialized, and computations on them can be started from
 * the command line. To expose those methods to the deserialize interface, such types should implement
 * CodeValue and via that interface supply CodeFunctions.
 * 
 * Since a certain function may have different strategies, the CodeFunction is evaluated through a Computation instance.
 */

package de.phwbrnr.lina.main.deserialize;

import de.phwbrnr.lina.main.strategies.Computation;

public abstract class CodeFunction {
	protected String name;
	protected String description;
	
	public abstract Computation<CodeValue> getComputation();
	
	public String getName () {
		return name;
	}
	
	public String getDescription () {
		return description;
	}
}
