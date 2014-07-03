package at.jku.paugujooksik.model;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.MOVE_MARK_ON_SWAP;
import static at.jku.paugujooksik.tools.Constants.MOVE_PIN_ON_SWAP;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardModel<T extends Comparable<T>> {
	private final Statistics stat = new Statistics();
	private final List<Card> cardList = new LinkedList<>();
	private final Configuration<T> config;
	private final List<T> values;
	private final List<T> sortedValues;
	private int curAction;
	private List<Action> expectedActions;
	private Action prevAction = null;

	public CardModel(Configuration<T> config) {
		this.config = config;
		this.values = config.values;
		this.sortedValues = getSortedCopy(values);
		reset(false);
	}

	public boolean allMarked() {
		for (Card card : cardList) {
			if (!card.marked)
				return false;
		}
		return true;
	}

	public Card getCard(int index) {
		return cardList.get(index);
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

	public int getSelectedIndex() {
		if (!oneSelected())
			throw new SelectionException("One button must be selected");
		return getSelection().get(0);
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

	public void incErrorCount() {
		stat.errorCount++;
	}

	public boolean isFinished() {
		return curAction == expectedActions.size();
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

	public boolean oneSelected() {
		return getSelection().size() == 1;
	}

	public void reset(boolean shuffleValues) {
		if (shuffleValues) {
			Collections.shuffle(values);
			DEBUGLOG.fine("Generated values: " + values);
		}
		cardList.clear();
		for (T value : values) {
			cardList.add(new Card(value));
		}
		expectedActions = config.getAlgorithm().getActions(values);
		curAction = 0;
		prevAction = null;
		stat.reset();
	}

	public Action select(int index) {
		Action action = Action.open(index);
		checkAction(action);

		if (twoSelected()) {
			// two cards already opened -> close unpinned ones!
			Card left = cardList.get(getFirstSelectedIndex());
			Card right = cardList.get(getSecondSelectedIndex());
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
			DEBUGLOG.severe("Invalid index: " + index);
		}
		return action;
	}

	public void selectAll() {
		assert allMarked();
		for (Card c : cardList) {
			c.selected = true;
		}
	}

	public Action swapSelection() {
		if (!twoSelected())
			throw new SelectionException("Two cards need to be open.");

		final Action action = Action.swap(getFirstSelectedIndex(), getSecondSelectedIndex());
		checkAction(action);

		final Card left = cardList.get(getFirstSelectedIndex());
		final Card right = cardList.get(getSecondSelectedIndex());
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

	public Action toggleMark(int index) {
		final Card card = cardList.get(index);
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

	public Action togglePin(int index) {
		return getCard(index).pinned ? unpin(index) : pin(index);
	}

	public boolean twoSelected() {
		return getSelection().size() == 2;
	}

	public boolean zeroSelected() {
		return getSelection().size() == 0;
	}

	private void checkAction(Action action) {
		if (curAction < expectedActions.size()) {
			final Action exp = expectedActions.get(curAction);
			
			DEBUGLOG.fine("Expected: '" + exp + "'; actual: '" + action + "' " + "<"
					+ (!exp.isCompatibleTo(action) ? "not " : "") + "compatible>" + "<"
					+ (!exp.equals(action) ? "not " : "") + "equal>");
			
			if (exp.isCompatibleTo(action)) {
				if (exp.equals(action)) {
					curAction++;
					prevAction = null;
				}
			} else {
				String message = "You should " + exp + " instead!";
				// don't count if the same wrong action is performed twice!
				if (prevAction == null || !prevAction.equals(action)) {
					prevAction = action;
					stat.errorCount++;
					throw new SelectionException(message);
				}
				throw new SelectionException(message, true);
			}
		} else if (!config.getAlgorithm().allowsMoreActions()) {
			throw new SelectionException("No more actions necessary!");
		}
	}

	private Card getPinnedCard() {
		for (Card card : cardList) {
			if (card.pinned)
				return card;
		}
		return null;
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

	private List<T> getSortedCopy(List<T> values) {
		final List<T> sorted = new LinkedList<>();
		for (T e : values) {
			sorted.add(e);
		}
		Collections.sort(sorted);
		return Collections.unmodifiableList(sorted);
	}

	private Action pin(int index) {
		final Action action = Action.pin(index);
		checkAction(action);

		final Card curPinned = getPinnedCard();
		final Card newPinned = cardList.get(index);

		if (curPinned == null) {
			// no card was pinned before -> pin card
			newPinned.pinned = true;
		} else if (curPinned.equals(newPinned)) {
			// same card is pinned again -> unpin card
			newPinned.pinned = false;
		} else {
			// other card is pinned -> toggle pins
			curPinned.pinned = false;
			newPinned.pinned = true;
		}
		return action;
	}

	private Action unpin(int index) {
		final Action action = Action.unpin(index);
		checkAction(action);
		cardList.get(index).pinned = false;
		return action;
	}
	
	public class Card implements Comparable<Card>, Serializable {
		private static final long serialVersionUID = 8151077928442901008L;

		public final T value;
		public boolean pinned = false;
		public boolean marked = false;
		public boolean selected = false;

		public Card(T value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value.toString();
		}

		@Override
		public int compareTo(CardModel<T>.Card o) {
			return value.compareTo(o.value);
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
