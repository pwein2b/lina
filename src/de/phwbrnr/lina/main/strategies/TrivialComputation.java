/**
 * Some Computation instances represent computational tasks that are so simple only a single strategy for
 * solving them is required; for these, subclasses of TrivialComputation may be used.
 * Since the computation is considered trivial, adding new strategies outside of the constructor is not possible.
 * 
 * The description string is inferred from the supplied single strategy.
 * 
 * @author Philipp Weinbrenner
 */

package de.phwbrnr.lina.main.strategies;
import java.util.LinkedList;

public class TrivialComputation<ResultType> extends Computation<ResultType> {
	private Strategy<ResultType>[] strategyList;
	private Strategy<ResultType> singleStrategy;
	
	public TrivialComputation(Strategy<ResultType> singleStrategy) {
		this.singleStrategy = singleStrategy;
		LinkedList<Strategy<ResultType>> templist = new LinkedList<Strategy<ResultType>>();
		templist.add(singleStrategy);
		@SuppressWarnings("unchecked")
		Strategy<ResultType>[] array = (Strategy<ResultType>[])templist.toArray();
		strategyList = array;
	}
	
	@Override
	public Strategy<ResultType>[] getApplicableStrategies(Object... instance) {
		if(!singleStrategy.appliesTo(instance))
			throw new IllegalArgumentException("The objects '" + instance.toString() + "' don't represent a valid input to the computation " + getDescription());
		
		return strategyList;
	}

	@Override
	public String getDescription() {
		return singleStrategy.getDescription();
	}

	@Override
	public Strategy<ResultType>[] getStrategies() {
		return strategyList;
	}

	@Override
	public void addStrategy(Strategy<ResultType> strategy) {
		assert false : "Read the docs. Can't add strategies to TrivialComputation";
	}
}
