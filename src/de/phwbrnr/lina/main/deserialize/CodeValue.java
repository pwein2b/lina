/**
 * This interface should be implemented by concrete types in lina.main package that want to expose according functionality
 * to the deserialize interface.
 * 
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.deserialize;

import java.util.HashMap;

public interface CodeValue {
	public String getValueTypeName();
	
	/**
	 * Return a HashMap of CodeFunctions that can be called on this value type; the keys are the code names. 
	 */
	public HashMap<String, CodeFunction> getInstanceFunctions();
}
