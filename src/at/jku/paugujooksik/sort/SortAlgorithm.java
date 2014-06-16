package at.jku.paugujooksik.sort;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.jku.paugujooksik.action.Action;

public abstract class SortAlgorithm<T extends Comparable<T>> implements
		Serializable {
	private static final long serialVersionUID = -7013695126248089403L;

	protected static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	protected final List<Action> actions = new LinkedList<>();

	protected List<T> a;
	protected int n;

	public abstract List<Action> getActions(List<T> values);

	private List<T> cloneList(List<T> list) {
		List<T> copy = new LinkedList<>();
		for (T e : list) {
			copy.add(e);
		}
		assert list.equals(copy);
		return copy;
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

	public boolean allowsMoreActions() {
		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
