package at.jku.paugujooksik.client.sort;

import java.util.List;

public class SelectionSort<T extends Comparable<T>> extends SortAlgorithm<T> {

	@Override
	public List<Action> getActions(List<T> values) {
		final List<T> a = cloneList(values);
		final int n = a.size();
		int pinned = -1;

		int iMin;
		for (int j = 0; j < n - 1; j++) {
			iMin = j;
			for (int i = j + 1; i < n; i++) {
				final BinaryAction openAction = Action.open(i, iMin);
				if (actions.size() < 1 || !actions.get(actions.size() - 1).equals(openAction))
					actions.add(openAction);
				
				if (a.get(i).compareTo(a.get(iMin)) < 0) {
					if (pinned != -1) actions.add(Action.unpin(pinned));
					iMin = i;
					pinned = iMin;
					actions.add(Action.pin(iMin));
				} else {
					if (pinned == -1){
						pinned = iMin;
						actions.add(Action.pin(pinned));
					}
				}
			}
			if (iMin != j) {
				T tmp = a.get(j);
				a.set(j, a.get(iMin));
				a.set(iMin, tmp);
				actions.add(Action.open(j, iMin));
				actions.add(Action.swap(j, iMin));
			}
			if (pinned != -1) {
				actions.add(Action.unpin(pinned));
				actions.add(Action.mark(j));
			}
			pinned = -1;
		}
		actions.add(Action.mark(n - 1));
		assert isListSorted(a);
		return actions;
	}
}
