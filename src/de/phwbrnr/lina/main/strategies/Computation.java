/**
 * This class represents any Computation that may be somewhat complex, can be performed with different ways,
 * or where simply different instances of the problem may we require completely different strategies in the first
 * place.
 * A class that wants to provide such a computation should create a method that does not directly return
 * the computation, but rather returns some instance of this class, which can determine which strategy is
 * appropriate.
 * In some instance, the object that is required to create a computation object may not know all strategies
 * for performing the computation; so, the Computation object returned should honor additional strategies that
 * may be supplied.
 * 
 * The computation itself is described by an array of Object's. What exactly is considered an acceptable set
 * of objects is determined by the implementing classes; methods with unacceptable arguments should throw
 * IllegalArgumentException.
 * The type parameter represents the type of the result of the computation.
 * 
 * @author Philipp Weinbrenner
 */
package de.phwbrnr.lina.main.strategies;

import java.util.ArrayList;

public abstract class Computation<ResultType> {
	/**
	 * @return A string description of the kind of computation the instance can perform
	 */
	public abstract String getDescription();
	
	/**
	 * Find out what kind of strategies are available for solving the computation
	 * @return a new array of Strategy instances
	 */
	public abstract Strategy<ResultType>[] getStrategies();
	
	public abstract void addStrategy(Strategy<ResultType> strategy);
	
	/**
	 * Find a list of strategies that are applicable to a certain instance of the problem
	 * @param problem A set of objects that describe the computation to be carried out
	 * @return a newly-allocated array of Strategy instances which are appropriate according to themselves.
	 * @throws IllegalArgumentException if the problem parameters don't describe a valid instance of the computation
	 * @throws NoStrategyException if no applicable strategy was found
	 */
	@SuppressWarnings("unchecked")
	public Strategy<ResultType>[] getApplicableStrategies(Object... problem) throws NoStrategyException {
		Strategy<ResultType>[] strategies = getStrategies();
		ArrayList<Strategy<ResultType>> applicable = new ArrayList<Strategy<ResultType>>(strategies.length);
		for(Strategy<ResultType> s : strategies) {
			if(s.appliesTo(problem))
				applicable.add(s);
		}
		
		return (Strategy<ResultType>[])(applicable.toArray());
	}
	
	/**
	 * Find the cheapest strategy for a given instance of the problem
	 * @param problem the computation instance
	 * @return one of the applicable (according to appliesTo()) strategies with minimal expectedCost()
	 * @throws IllegalArgumentException if the problem does not a represent a valid task
	 * @throws NoStrategyException if no applicable strategy has been registered
	 */
	public Strategy<ResultType> findCheapestStrategy(Object... problem) throws NoStrategyException {
		Strategy<ResultType>[] strategies = getApplicableStrategies(problem);
		if(strategies.length == 0)
			throw new NoStrategyException("No strategy found for computation '" + getDescription() + "' on problem instance " + problem.toString());
		
		Strategy<ResultType> optimalstrat = null;
		int optimalvalue = 100;
		for(Strategy<ResultType> s : strategies) {
			int cost = s.expectedCost(problem);
			if(cost < optimalvalue) {
				optimalvalue = cost;
				optimalstrat = s;
			}
		}
		
		return optimalstrat;
	}
	
	/**
	 * Perform the computation with an arbitrary acceptable strategy.
	 * The method tries to use one of the applicable strategies that consider themselves cheapest.
	 * @param problem An array of objects describing the problem instance
	 * @return the return value of the computation
	 * @throws NoStrategyException if no applicable strategy could be found
	 */
	public ResultType compute(Object... problem) throws NoStrategyException {
		return findCheapestStrategy(problem).execute(problem);
	}
}
