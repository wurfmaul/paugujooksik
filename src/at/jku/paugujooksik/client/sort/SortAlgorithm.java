package at.jku.paugujooksik.client.sort;

import java.util.LinkedList;
import java.util.List;

public abstract class SortAlgorithm<T extends Comparable<T>> {
	protected final List<Action> actions = new LinkedList<>();
	
	public abstract List<Action> getActions(List<T> values);
	
	protected List<T> cloneList(List<T> list) {
		List<T> copy = new LinkedList<>();
		for (T e : list) {
			copy.add(e);
		}
		return copy;
	}
	
	protected boolean isListSorted(List<T> list) {
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i-1).compareTo(list.get(i)) > 0)
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
