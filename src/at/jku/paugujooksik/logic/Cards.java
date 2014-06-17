package at.jku.paugujooksik.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.gui.SelectionException;

public class Cards<T extends Comparable<T>> {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final boolean MOVE_PIN = false;
	private static final boolean MOVE_MARK = false;

	private final Statistics stat = new Statistics();
	private final List<Card<T>> cardList = new LinkedList<>();
	private final Configuration<T> config;
	private final List<T> values;
	private final List<T> sortedValues;

	private List<Action> expectedActions;
	private int curAction;
	private int pinIndex = -1;

	public Cards(Configuration<T> config) {
		this.config = config;
		this.values = config.values;
		this.sortedValues = getSortedCopy(values);
		reset(false);
	}

	public boolean allMarked() {
		for (Card<T> card : cardList) {
			if (!card.marked)
				return false;
		}
		return true;
	}

	public Card<T> getCard(int index) {
		return cardList.get(index);
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

	public void incErrorCount() {
		stat.errorCount++;
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
			cardList.get(index).selected = false;
			return null;
		}

		Action action = Action.open(index);
		checkAction(action);

		if (twoSelected()) {
			Card<T> left = cardList.get(getFirstSelectedIndex());
			Card<T> right = cardList.get(getSecondSelectedIndex());
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
			cardList.get(index).selected = true;
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
		final Card<T> left = cardList.get(getFirstSelectedIndex());
		final Card<T> right = cardList.get(getSecondSelectedIndex());
		cardList.set(getFirstSelectedIndex(), right);
		cardList.set(getSecondSelectedIndex(), left);
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
				cardList.get(pinIndex).pinned = false;
			cardList.get(index).pinned = true;
			pinIndex = index;
			return action;
		} else {
			cardList.get(pinIndex).pinned = false;
			pinIndex = -1;
		}
		return null;
	}

	public Action toggleMark(int index) {
		final Card<T> card = cardList.get(index);
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
		return cardList.get(index).marked;
	}

	public boolean isOnRightPosition(int index) {
		return cardList.get(index).value.equals(sortedValues.get(index));
	}

	public boolean isPinned(int index) {
		return cardList.get(index).pinned;
	}

	public boolean isSelected(int index) {
		return cardList.get(index).selected;
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
		for (Card<T> c : cardList) {
			c.selected = true;
		}
	}

	public void reset(boolean shuffleValues) {
		if (shuffleValues) {
			Collections.shuffle(values);
			DEBUGLOG.fine("Generated values: " + values);
		}
		cardList.clear();
		for (T value : values) {
			cardList.add(new Card<>(value));
		}
		expectedActions = config.getAlgorithm().getActions(values);
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
		for (int i = 0; i < cardList.size(); i++) {
			if (cardList.get(i).selected) {
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
				throw new SelectionException("You should " + exp + " instead!");
			}
		} else if (!config.getAlgorithm().allowsMoreActions()) {
			throw new SelectionException("No more actions necessary!");
		}
	}

	private class Statistics {
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
