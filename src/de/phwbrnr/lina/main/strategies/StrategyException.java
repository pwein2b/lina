/**
 * A simple exception class to indicate that some Strategy failed to execute.
 */

package de.phwbrnr.lina.main.strategies;

public class StrategyException extends Exception {

	private static final long serialVersionUID = 1L;

	public StrategyException (String text) {
		super(text);
	}
	
	public StrategyException (String text, Throwable cause) {
		super(text, cause);
	}
}
