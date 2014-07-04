package at.jku.paugujooksik.sort;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.model.Action;

/**
 * Super class of all sorting algorithms. Provides helper methods and common
 * structures.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @param <T>
 *            can be any of {@link Comparable}.
 */
public abstract class SortAlgorithm<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = -7013695126248089403L;

	/**
	 * The list of actions that need to be performed in order to act like the
	 * algorithm.
	 */
	protected final List<Action> actions = new LinkedList<>();
	/** The input of the algorithm, e.g. the shuffled values. */
	protected List<T> a;
	/** Contains the length of the list of shuffled values. */
	protected int n;

	/**
	 * @return true if further actions are allowed despite the fact that the
	 *         algorithm is done.
	 */
	public boolean allowsMoreActions() {
		return false;
	}

	/**
	 * Compute the actions that the algorithm needs to perform in order to sort
	 * the cards.
	 * 
	 * @param values
	 *            The shuffled cards.
	 * @return a list of actions.
	 */
	public abstract List<Action> getActions(List<T> values);

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	/**
	 * @param values
	 *            The list of values.
	 * @return true if the list of values is sorted.
	 */
	protected boolean isListSorted(List<T> values) {
		for (int i = 1; i < values.size(); i++) {
			if (values.get(i - 1).compareTo(values.get(i)) > 0)
				return false;
		}
		return true;
	}

	/**
	 * Does basic setup.
	 * 
	 * @param values
	 *            The list of values.
	 */
	protected void setup(List<T> values) {
		a = cloneList(values);
		n = a.size();
		actions.clear();
	}

	/**
	 * @param list
	 *            The list that should be copied.
	 * @return a copy of the list.
	 */
	private List<T> cloneList(List<T> list) {
		List<T> copy = new LinkedList<>();
		for (T e : list) {
			copy.add(e);
		}
		assert list.equals(copy);
		return copy;
	}
}
