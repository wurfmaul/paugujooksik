package at.jku.paugujooksik.client.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.client.sort.BubbleSort;
import at.jku.paugujooksik.client.sort.InsertionSort;
import at.jku.paugujooksik.client.sort.PlayMode;
import at.jku.paugujooksik.client.sort.SelectionSort;
import at.jku.paugujooksik.client.sort.SortAlgorithm;

public class SortConfig<T extends Comparable<T>> {
	private final List<SortAlgorithm<T>> algorithms = new LinkedList<>();
	private int curAlgo;

	SortConfig(int defaultIndex) {
		// TODO reflect ?
		algorithms.add(new PlayMode<T>());
		algorithms.add(new BubbleSort<T>());
		algorithms.add(new InsertionSort<T>());
		algorithms.add(new SelectionSort<T>());
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