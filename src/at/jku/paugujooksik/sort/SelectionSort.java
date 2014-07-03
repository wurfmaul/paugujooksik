package at.jku.paugujooksik.sort;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;

import java.util.BitSet;
import java.util.List;

import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Action.BinaryAction;

public class SelectionSort<T extends Comparable<T>> extends SortAlgorithm<T> {
	private static final long serialVersionUID = -7852418811785446942L;
	private int pinned;
	private BitSet opened;

	@Override
	public List<Action> getActions(List<T> values) {
		setup(values);
		pinned = -1;
		opened = new BitSet(values.size());

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
			actionUnpin();
		}

		DEBUGLOG.fine("SelectionSort sorted the values " + values);
		assert isListSorted(a);
		return actions;
	}

	private void actionUnpin() {
		if (pinned >= 0) {
			actions.add(Action.unpin(pinned));
			pinned = -1;
		}
	}

	private void actionSwap(int i, int j) {
		pinned = (pinned == i) ? j : i;
		actions.add(Action.swap(i, j));
	}

	private void actionPin(int i) {
		if (pinned != i) {
			pinned = i;
			actions.add(Action.pin(i));
		}
	}

	private void actionOpen(int i, int j) {
		if (!opened.get(i) || !opened.get(j)) {
			final BinaryAction openAction = Action.open(i, j);
			opened.clear();
			opened.set(i);
			opened.set(j);
			actions.add(openAction);
		}
	}
}
