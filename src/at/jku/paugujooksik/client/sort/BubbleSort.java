package at.jku.paugujooksik.client.sort;

import java.util.List;

public class BubbleSort<T extends Comparable<T>> extends SortAlgorithm<T> {

	@Override
	public List<Action> getActions(List<T> values) {
		final List<T> a = cloneList(values);
		final int n = a.size();
		
		boolean swapped = true;
		int max = n - 1;
		while (swapped) {
			swapped = false;
			for (int i = 1; i <= max; i++) {
				T left = a.get(i - 1);
				T right = a.get(i);
				actions.add(Action.open(i - 1, i));
//				actions.add(Action.compare(i - 1, i));
				if (left.compareTo(right) > 0) {
					a.set(i, left);
					a.set(i - 1, right);
					swapped = true;
					actions.add(Action.swap(i - 1, i));
				}
				if (i == max)
					actions.add(Action.mark(i));
			}
			max--;
		}
		while (max >= 0) {
			actions.add(Action.mark(max--));
		}
		
		assert isListSorted(a);
		return actions;
	}
}
