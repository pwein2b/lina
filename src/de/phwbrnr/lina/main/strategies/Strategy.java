/**
 * An interface to represent a Strategy for performing a complex operation.
 * Implementations of this interface need to implement only one type of Computation.
 * 
 * The input values to the computation (the problem instance) are given as array of Object's; if the given
 * array cannot be interpreted as valid instance of the problem, an IllegalArgumentException should be thrown.
 * 
 * A strategy will be compared to other available strategies in terms of efficiency and thus should rate its
 * expected Cost in a useful way; the expected case should be in the range of 0 to 100, where 0 means that the
 * strategy in essence carries out some elementary operation, whereas 100 means that the strategy cannot at
 * all be applied to the given problem instance.    
 * 
 * The type paremeter describes the type of the return value.
 * 
 * @see Computation
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.strategies;

public interface Strategy<ResultType> {
	public String getDescription();
	public String getName();
	
	/**
	 * Check if the given strategy can be applied to the given  
	 * @param problem representation of the computation input values 
	 * @return whether the strategy can be applied to the problem
	 */
	public boolean appliesTo(Object... problem);
	
	/**
	 * Get the expected computational cost of applying the strategy to a given problem.
	 * This method should be implemented in a rather straightforward fashion, a good or general guess may
	 * actually suffice.
	 * @return the expected cost, where 0 means minimal cost and 100 means the strategy is
	 * not applicable to the given problem
	 * @throws IllegalArgumentException is the problem given does not represent a valid instance
	 * of the computational problem
	 */
	public int expectedCost(Object...problem);
	
	/**
	 * Execute the strategy on a computational problem
	 * @param problem
	 * @throws IllegalArgumentException if the strategy cannot be applied to the arguments given
	 */
	public ResultType execute(Object... problem);
	
	/**
	 * Some methods require a concrete Strategy type, so we define this wrapping class.
	 *
	 * @param <RType> Return value type of the strategy
	 */
	public class WrapStrategy<RType> implements Strategy<RType> {
		private Strategy<RType> strategy;
		
		public WrapStrategy(Strategy<RType> wrap) {
			strategy = wrap;
		}
		
		@Override
		public String getDescription() {
			return strategy.getDescription() + " (WrapStrategy)";
		}

		@Override
		public boolean appliesTo(Object... problem) {
			return strategy.appliesTo(problem);
		}

		@Override
		public int expectedCost(Object... problem) {
			return strategy.expectedCost(problem);
		}

		@Override
		public RType execute(Object... problem) {
			return strategy.execute(problem);
		}
		
		public Strategy<RType> getStrategy() {
			return strategy;
		}

		@Override
		public String getName() {
			return strategy.getName();
		}
	}
}
