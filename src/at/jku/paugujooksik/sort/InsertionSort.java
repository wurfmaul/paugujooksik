package at.jku.paugujooksik.sort;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;

import java.util.List;

import at.jku.paugujooksik.action.Action;

public class InsertionSort<T extends Comparable<T>> extends SortAlgorithm<T> {
	private static final long serialVersionUID = 2190148165959505555L;

	@Override
	public List<Action> getActions(List<T> values) {
		setup(values);

		int j = 0;
		for (int i = 1; i < n; i++) {
			T x = a.get(i);
			j = i;
			actions.add(Action.open(j, j - 1));
			while (j > 0 && a.get(j - 1).compareTo(x) > 0) {
				actions.add(Action.swap(j, j - 1));
				a.set(j, a.get(j - 1));
				j--;
				if (j > 0)
					actions.add(Action.open(j, j - 1));
			}
			a.set(j, x);
		}

		DEBUGLOG.fine("InsertionSort sorted the values " + values);
		assert isListSorted(a);
		return actions;
	}

}