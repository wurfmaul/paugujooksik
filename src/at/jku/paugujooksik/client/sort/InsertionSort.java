package at.jku.paugujooksik.client.sort;

import java.util.List;

public class InsertionSort<T extends Comparable<T>> extends SortAlgorithm<T> {

	@Override
	public List<Action> getActions(List<T> values) {
		final List<T> a = cloneList(values);
		final int n = a.size();
		
		//TODO insertion sort
		
		return actions;
	}

}