package at.jku.paugujooksik.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.sort.BubbleSort;
import at.jku.paugujooksik.sort.InsertionSort;
import at.jku.paugujooksik.sort.PlayMode;
import at.jku.paugujooksik.sort.SelectionSort;
import at.jku.paugujooksik.sort.SortAlgorithm;

public class SortConfig<T extends Comparable<T>> {
	private final List<SortAlgorithm<T>> algorithms = new LinkedList<>();
	private int curAlgo;

	SortConfig(int defaultIndex) {
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