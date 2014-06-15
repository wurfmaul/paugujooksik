package at.jku.paugujooksik.sort;

import java.util.List;

public class InsertionSort<T extends Comparable<T>> extends SortAlgorithm<T> {

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
//					actions.add(Action.mark(j)); // FIXME marking
				a.set(j, a.get(j - 1));
				j--;
				if (j > 0)
					actions.add(Action.open(j, j - 1));
			}
			a.set(j, x);
		}
		while (j>=0) {
			actions.add(Action.mark(j--));
		}

		DEBUGLOG.info("InsertionSort sorted the values " + values);
		assert isListSorted(a);
		return actions;
	}

}