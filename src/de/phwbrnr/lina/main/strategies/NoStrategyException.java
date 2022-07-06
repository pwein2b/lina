/**
 * An exception that should be thrown by Computation subclasses when there is no applicable strategy to
 * perform the computation.
 * 
 * @see Computation
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.strategies;

public class NoStrategyException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoStrategyException(String msg) {
		super(msg);
	}
	
	public NoStrategyException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	/**
	 * Construct a NoStrategyException with default message
	 * @param <T> Type parameter for the failedComputation
	 * @param message a custom message
	 * @param failedComputation the Computation that could not be carried out
	 * @param problem the Computation instance that could not be computed
	 */
	public <T> NoStrategyException(String message, Computation<T> failedComputation, Object... problem) {
		super("No applicable strategy [" + message + "] for computation '" + failedComputation.getDescription() + "' on problem instance " + problem.toString());
	}
}
