/**
 * An abstract class that provides deserialization functionality. Mainly for ValueDeserializer.
 * Should be constructed once for each type and then registered with ValueDeserializer.
 * 
 * If ValueDeserializer finds a string of the form 'Type[Param_1,...,Param_n]', it finds the appropriate Deserializer
 * for Type with the parameters. Implementing classes have to take into account that the parameters can themselves contain
 * serialized data.
 * 
 *  @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.deserialize;

public abstract class Deserializer {
	public abstract CodeValue parse (String... parameters) throws DeserializationException;
}
