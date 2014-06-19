package at.jku.paugujooksik.model;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.MOVE_MARK_ON_SWAP;
import static at.jku.paugujooksik.tools.Constants.MOVE_PIN_ON_SWAP;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.gui.SelectionException;

public class Cards<T extends Comparable<T>> {
	private final Statistics stat = new Statistics();
	private final List<Card<T>> cardList = new LinkedList<>();
	private final Configuration<T> config;
	private final List<T> values;
	private final List<T> sortedValues;
	private int curAction;
	private int pinIndex = -1;
	private List<Action> expectedActions;
	private Action lastAction = null;

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
		Action action = Action.open(index);
		checkAction(action);

		if (twoSelected()) {
			// two cards already opened -> close unpinned ones!
			Card<T> left = cardList.get(getFirstSelectedIndex());
			Card<T> right = cardList.get(getSecondSelectedIndex());
			if (!left.pinned)
				left.selected = false;
			if (!right.pinned)
				right.selected = false;
		}

		switch (getSelection().size()) {
		case 1:
			if (isSelected(index)) {
				// click twice at same card -> if not pinned, close it!
				cardList.get(index).selected = isPinned(index);
			} else {
				// click at another card -> open second card!
				action = Action.open(index, getSelectedIndex());
				checkAction(action);
				cardList.get(index).selected = true;
				stat.compareCount++;
			}
			break;
		case 0:
			// click at first card -> open it!
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

		// FIXME following 4 lines are evil!
		final Card<T> left = cardList.get(getFirstSelectedIndex());
		final Card<T> right = cardList.get(getSecondSelectedIndex());
		cardList.set(getFirstSelectedIndex(), right);
		cardList.set(getSecondSelectedIndex(), left);
		if (!MOVE_PIN_ON_SWAP) {
			boolean leftPinned = left.pinned;
			boolean rightPinned = right.pinned;
			left.pinned = rightPinned;
			right.pinned = leftPinned;
		}
		if (!MOVE_MARK_ON_SWAP) {
			boolean leftMarked = left.marked;
			boolean rightMarked = right.marked;
			left.marked = rightMarked;
			right.marked = leftMarked;
		}
		stat.swapCount++;
		return action;
	}

	public Action togglePin(int index) {
		final Action action = Action.pin(index);
		if (pinIndex == -1 || pinIndex != index) {
			checkAction(action);
			if (pinIndex != -1)
				cardList.get(pinIndex).pinned = false;
			cardList.get(index).pinned = true;
			pinIndex = index;
		} else {
			cardList.get(pinIndex).pinned = false;
			pinIndex = -1;
		}
		return action;
	}

	public Action toggleMark(int index) {
		final Card<T> card = cardList.get(index);
		Action action;
		if (card.marked) {
			action = Action.mark(index);
			card.marked = false;
		} else {
			action = Action.mark(index);
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
		lastAction = null;
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
				String message = "You should " + exp + " instead!";
				// don't count if the same wrong action is performed twice!
				if (lastAction == null || !lastAction.equals(action)) {
					lastAction = action;
					stat.errorCount++;
					throw new SelectionException(message);
				}
				throw new SelectionException(message, true);
			}
		} else if (!config.getAlgorithm().allowsMoreActions()) {
			throw new SelectionException("No more actions necessary!");
		}
	}

	private class Statistics {
		public int compareCount;
		public int errorCount;
		public int swapCount;

		public Statistics() {
			reset();
		}

		public void reset() {
			compareCount = 0;
			errorCount = 0;
			swapCount = 0;
		}
	}
}
