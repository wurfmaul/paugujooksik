package at.jku.paugujooksik.logic;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.jku.paugujooksik.gui.SelectionException;
import at.jku.paugujooksik.sort.Action;
import at.jku.paugujooksik.sort.SortConfig;

public class Cards<T extends Comparable<T>> {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final boolean MOVE_PIN = false;
	private static final boolean MOVE_MARK = false;

	private final Statistics stat = new Statistics();
	private final List<Card<T>> cards = new LinkedList<>();
	private final List<T> values;
	private final List<T> sortedValues;

	private List<Action> expectedActions;
	private int curAction;
	private int pinIndex = -1;

	public final SortConfig<T> sort;

	public Cards(List<T> values, int sortAlgoIndex) {
		this.values = values;
		this.sortedValues = getSortedCopy(values);
		this.sort = new SortConfig<>(sortAlgoIndex);
		reset();
	}

	public boolean allMarked() {
		for (Card<T> card : cards) {
			if (!card.marked)
				return false;
		}
		return true;
	}

	public Card<T> getCard(int index) {
		return cards.get(index);
	}

	public int getSelectedIndex() {
		if (!oneSelected())
			throw new SelectionException("One button must be selected");
		return getSelection().get(0);
	}

	public int getFirstSelectedIndex() {
		if (!twoSelected())
			throw new SelectionException("Two buttons must be selected!");
		return getSelection().get(0);
	}

	public int getSecondSelectedIndex() {
		if (!twoSelected())
			throw new SelectionException("Two buttons must be selected!");
		return getSelection().get(1);
	}

	public int getErrorCount() {
		return stat.errorCount;
	}

	public int getSwapCount() {
		return stat.swapCount;
	}

	public int getCompareCount() {
		return stat.compareCount;
	}

	public Action select(int index) {
		if (isSelected(index) && oneSelected()) {
			// click twice at same card
			cards.get(index).selected = false;
			return null; // TODO noAction ??
		}

		Action action = Action.open(index);
		checkAction(action);

		if (twoSelected()) {
			Card<T> left = cards.get(getFirstSelectedIndex());
			Card<T> right = cards.get(getSecondSelectedIndex());
			if (!left.pinned)
				left.selected = false;
			if (!right.pinned)
				right.selected = false;
		}

		switch (getSelection().size()) {
		case 1:
			action = Action.open(index, getSelectedIndex());
			checkAction(action);
			stat.compareCount++;
		case 0:
			cards.get(index).selected = true;
			break;

		default:
			throw new SelectionException("Invalid index: " + index);
		}
		return action;
	}

	public Action swapSelection() {
		if (!twoSelected())
			throw new SelectionException("Two cards need to be open.");

		final Action action = Action.swap(getFirstSelectedIndex(),
				getSecondSelectedIndex());
		checkAction(action);
		final Card<T> left = cards.get(getFirstSelectedIndex());
		final Card<T> right = cards.get(getSecondSelectedIndex());
		cards.set(getFirstSelectedIndex(), right);
		cards.set(getSecondSelectedIndex(), left);
		if (!MOVE_PIN) {
			boolean leftPinned = left.pinned;
			boolean rightPinned = right.pinned;
			left.pinned = rightPinned;
			right.pinned = leftPinned;
		}
		if (!MOVE_MARK) {
			boolean leftMarked = left.marked;
			boolean rightMarked = right.marked;
			left.marked = rightMarked;
			right.marked = leftMarked;
		}
		stat.swapCount++;
		return action;
	}

	public Action togglePin(int index) {
		if (pinIndex == -1 || pinIndex != index) {
			final Action action = Action.pin(index);
			checkAction(action);
			if (pinIndex != -1)
				cards.get(pinIndex).pinned = false;
			cards.get(index).pinned = true;
			pinIndex = index;
			return action;
		} else {
			cards.get(pinIndex).pinned = false;
			pinIndex = -1;
		}
		return null;
	}

	public Action toggleMark(int index) {
		final Card<T> card = cards.get(index);
		Action action;
		if (card.marked) {
			action = Action.unmark(index);
			checkAction(action);
			card.marked = false;
		} else {
			action = Action.mark(index);
			checkAction(action);
			card.marked = true;
		}
		return action;
	}

	public boolean isMarked(int index) {
		return cards.get(index).marked;
	}

	public boolean isOnRightPosition(int index) {
		return cards.get(index).value.equals(sortedValues.get(index));
	}

	public boolean isPinned(int index) {
		return cards.get(index).pinned;
	}

	public boolean isSelected(int index) {
		return cards.get(index).selected;
	}

	public boolean isSorted() {
		for (int i = 0; i < values.size(); i++) {
			if (!isOnRightPosition(i))
				return false;
		}
		return true;
	}

	public boolean zeroSelected() {
		return getSelection().size() == 0;
	}

	public boolean oneSelected() {
		return getSelection().size() == 1;
	}

	public boolean twoSelected() {
		return getSelection().size() == 2;
	}

	public void selectAll() {
		assert allMarked();

		for (Card<T> c : cards) {
			c.selected = true;
		}
	}

	public void reset() {
		Collections.shuffle(values);
		DEBUGLOG.fine("Generated values: " + values);
		cards.clear();
		for (T value : values) {
			cards.add(new Card<>(value));
		}
		expectedActions = sort.getCurrent().getActions(values);
		curAction = 0;
		stat.reset();
	}

	private List<T> getSortedCopy(List<T> values) {
		final List<T> sorted = new LinkedList<>();
		for (T e : values) {
			sorted.add(e);
		}
		Collections.sort(sorted);
		return Collections.unmodifiableList(sorted);
	}

	private List<Integer> getSelection() {
		final List<Integer> sel = new LinkedList<>();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).selected) {
				sel.add(i);
			}
		}
		return sel;
	}

	private void checkAction(Action action) {
		if (curAction < expectedActions.size()) {
			final Action exp = expectedActions.get(curAction);
			DEBUGLOG.fine("Expected: '" + exp + "'; actual: '" + action + "' "
					+ "<" + (!exp.isCompatibleTo(action) ? "not " : "")
					+ "compatible>" + "<" + (!exp.equals(action) ? "not " : "")
					+ "equal>");
			if (exp.isCompatibleTo(action)) {
				if (exp.equals(action)) {
					curAction++;
				}
			} else {
				stat.errorCount++;
				throw new SelectionException(
						"Algorithm would do the following instead: " + exp
								+ "!");
			}
		} else if (!sort.getCurrent().allowsMoreActions()) {
			throw new SelectionException("No more actions necessary!");
		}
	}

	private class Statistics implements Serializable {
		private static final long serialVersionUID = 1077686492906771786L;
		
		public int errorCount = 0;
		public int swapCount = 0;
		public int compareCount = 0;

		public void reset() {
			errorCount = 0;
			swapCount = 0;
			compareCount = 0;
		}
	}
}
