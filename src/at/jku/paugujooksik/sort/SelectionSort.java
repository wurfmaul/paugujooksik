package at.jku.paugujooksik.sort;

import java.util.List;

public class SelectionSort<T extends Comparable<T>> extends SortAlgorithm<T> {
	int pinned;
	
	@Override
	public List<Action> getActions(List<T> values) {
		setup(values);
		pinned = -1;

		int iMin;
		for (int j = 0; j < n - 1; j++) {
			iMin = j;
			for (int i = j + 1; i < n; i++) {
				actionOpen(iMin, i);
				if (a.get(i).compareTo(a.get(iMin)) < 0) {
					iMin = i;
				}
				actionPin(iMin);
			}
			if (iMin != j) {
				T tmp = a.get(j);
				a.set(j, a.get(iMin));
				a.set(iMin, tmp);
				actionOpen(j, iMin);
				actionSwap(iMin, j);
			}
			actionMark(j);
		}
		actions.add(Action.mark(n - 1));
		
		DEBUGLOG.info("SelectionSort sorted the values " + values);
		assert isListSorted(a);
		return actions;
	}

	private void actionMark(int i) {
		if (pinned != -1) {
			actions.add(Action.mark(i));
		}
	}

	private void actionSwap(int i, int j) {
		actions.add(Action.swap(i, j));
	}

	private void actionPin(int i) {
		if (pinned != i) {
			pinned = i;
			actions.add(Action.pin(i));
		}
	}

	private void actionOpen(int i, int j) {
		final BinaryAction openAction = Action.open(i, j);
		if (actions.size() < 1 || !actions.get(actions.size() - 1).equals(openAction))
			actions.add(openAction);
	}
}
