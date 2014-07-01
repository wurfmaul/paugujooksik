package at.jku.paugujooksik.sort;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.action.Action;

public abstract class SortAlgorithm<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = -7013695126248089403L;

	protected final List<Action> actions = new LinkedList<>();
	protected List<T> a;
	protected int n;

	public boolean allowsMoreActions() {
		return false;
	}

	public abstract List<Action> getActions(List<T> values);

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	protected boolean isListSorted(List<T> values) {
		for (int i = 1; i < values.size(); i++) {
			if (values.get(i - 1).compareTo(values.get(i)) > 0)
				return false;
		}
		return true;
	}

	protected void setup(List<T> values) {
		a = cloneList(values);
		n = a.size();
		actions.clear();
	}

	private List<T> cloneList(List<T> list) {
		List<T> copy = new LinkedList<>();
		for (T e : list) {
			copy.add(e);
		}
		assert list.equals(copy);
		return copy;
	}
}
