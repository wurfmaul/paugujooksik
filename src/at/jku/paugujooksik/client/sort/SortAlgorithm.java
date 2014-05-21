package at.jku.paugujooksik.client.sort;

import java.util.LinkedList;
import java.util.List;

public abstract class SortAlgorithm<T extends Comparable<T>> {
	protected final List<Action> actions = new LinkedList<>();
	
	public abstract List<Action> getActions(List<T> values);
	
	protected BinaryAction compare(int left, int right) {
		return new BinaryAction(Type.COMPARE, left, right);
	}
	
	protected UnaryAction open(int index) {
		return new UnaryAction(Type.OPEN, index);
	}
	
	protected BinaryAction open(int left, int right) {
		return new BinaryAction(Type.OPEN, left, right);
	}
	
	protected UnaryAction mark(int index) {
		return new UnaryAction(Type.MARK, index);
	}
	
	protected UnaryAction pin(int index) {
		return new UnaryAction(Type.PIN, index);
	}
	
	protected UnaryAction unpin(int index) {
		return new UnaryAction(Type.UNPIN, index);
	}
	
	protected BinaryAction swap(int left, int right) {
		return new BinaryAction(Type.SWAP, left, right);
	}
	
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
}
