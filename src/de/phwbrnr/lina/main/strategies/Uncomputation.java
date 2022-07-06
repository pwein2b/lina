/**
 * A computation object to represent the case that some computation has not yet been implemented or is impossible at all.
 * 
 * @author Philipp Weinbrener
 */

package de.phwbrnr.lina.main.strategies;

import java.util.ArrayList;

public class Uncomputation<ResultType> extends Computation<ResultType> {
	String notice;
	Strategy<ResultType>[] strategies;
	
	@SuppressWarnings("unchecked")
	public Uncomputation (String notice) {
		this.notice = notice;
		strategies = (Strategy<ResultType>[]) (new ArrayList<Strategy.WrapStrategy<ResultType>>()).toArray();
	}
	
	@Override
	public String getDescription() {
		return "Uncomputation " + notice;
	}

	@Override
	public Strategy<ResultType>[] getStrategies() {
		return strategies;
	}

	@Override
	public void addStrategy(Strategy<ResultType> strategy) {
		assert false : "Read the docs. Can't add strategies to Uncomputation";
	}

}
