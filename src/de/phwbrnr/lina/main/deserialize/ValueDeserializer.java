/**
 * This class is a singleton and the main entry point for deserializing values.
 * 
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.deserialize;

import java.util.HashMap;

public final class ValueDeserializer {
	private static ValueDeserializer instance;
	public static ValueDeserializer getInstance() {
		if (instance == null)
			instance = new ValueDeserializer();
		return instance;
	}
	
	private HashMap<String, Deserializer> registeredTypes;
	
	private ValueDeserializer () {
		registeredTypes = new HashMap<String, Deserializer>();
		
		// TODO fill standard types
	}
}
