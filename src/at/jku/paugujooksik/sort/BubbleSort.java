package at.jku.paugujooksik.sort;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;

import java.util.List;

import at.jku.paugujooksik.model.Action;

/**
 * An implementation of the "Bubble Sort" algorithm.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @param <T>
 *            can be any of {@link Comparable}.
 */
public class BubbleSort<T extends Comparable<T>> extends SortAlgorithm<T> {
	private static final long serialVersionUID = -7956968455697337445L;

	@Override
	public List<Action> getActions(List<T> values) {
		setup(values);
		boolean swapped = true;
		int max = n - 1;

		while (swapped) {
			swapped = false;
			for (int i = 1; i <= max; i++) {
				T left = a.get(i - 1);
				T right = a.get(i);
				actions.add(Action.open(i - 1, i));
				if (left.compareTo(right) > 0) {
					a.set(i, left);
					a.set(i - 1, right);
					swapped = true;
					actions.add(Action.swap(i - 1, i));
				}
			}
			max--;
		}

		DEBUGLOG.fine("BubbleSort sorted the values " + values);
		assert isListSorted(a);
		return actions;
	}
}
