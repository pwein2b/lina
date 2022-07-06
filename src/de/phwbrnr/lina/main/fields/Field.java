/**
 * Represent a field.
 * 
 * @author Philipp Weinbrenner
 * @version 2022-05-10
 */
package de.phwbrnr.lina.main.fields;

import java.util.ArrayList;

import de.phwbrnr.lina.main.strategies.Computation;
import de.phwbrnr.lina.main.strategies.Strategy;
import de.phwbrnr.lina.main.strategies.TrivialComputation;

public abstract class Field implements Ring {

	@Override
	public final boolean isCommutative() {
		return true;
	}

	@Override
	public final boolean isIntegralDomain () {
		return true;
	}
	
	@Override
	public final boolean irreducible (RingElement el) {
		return true;
	}

	@Override
	public final Computation<ArrayList<RingElement>> factor () {
		Field f = this;
		return new TrivialComputation<ArrayList<RingElement>>(new Strategy<ArrayList<RingElement>>() {
			@Override
			public boolean appliesTo(Object... problem) {
				if(problem.length != 1)
					return false;
				try {
					((RingElement)problem[0]).interpret(f);
					return true;
				} catch (Exception e) {
					return false;
				}
			}

			@Override
			public int expectedCost(Object... problem) {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public ArrayList<RingElement> execute(Object... problem) {
				ArrayList<RingElement> val = new ArrayList<RingElement> ();
				val.add((RingElement)problem[0]);
				return val;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Factor a field element";
			}
		});
	}
}
