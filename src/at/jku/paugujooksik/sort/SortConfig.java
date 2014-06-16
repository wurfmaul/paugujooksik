package at.jku.paugujooksik.sort;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SortConfig<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = -3009123587215411268L;
	
	private final List<SortAlgorithm<T>> algorithms = new LinkedList<>();
	private int curAlgo;

	public SortConfig(int defaultIndex) {
		// TODO reflect?
		algorithms.add(new BubbleSort<T>());
		algorithms.add(new InsertionSort<T>());
		algorithms.add(new SelectionSort<T>());
		algorithms.add(new PlayMode<T>());
		curAlgo = defaultIndex;
	}

	public List<SortAlgorithm<T>> getAll() {
		return Collections.unmodifiableList(algorithms);
	}

	public SortAlgorithm<T> getCurrent() {
		return algorithms.get(curAlgo);
	}

	public int getCurrentIndex() {
		return curAlgo;
	}

	public void setCurrent(int index) {
		this.curAlgo = index;
	}
}