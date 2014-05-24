package at.jku.paugujooksik.client.sort;

import java.util.List;

public class InsertionSort<T extends Comparable<T>> extends SortAlgorithm<T> {

	@Override
	public List<Action> getActions(List<T> values) {
		final List<T> a = cloneList(values);
		final int n = a.size();

		for (int i = 1; i < n; i++) {
			T x = a.get(i);
			int j = i;
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

		assert isListSorted(a);
		return actions;
	}

}