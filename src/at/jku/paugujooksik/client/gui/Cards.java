package at.jku.paugujooksik.client.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import at.jku.paugujooksik.client.sort.Action;
import at.jku.paugujooksik.client.sort.BubbleSort;
import at.jku.paugujooksik.client.sort.InsertionSort;
import at.jku.paugujooksik.client.sort.PlayMode;
import at.jku.paugujooksik.client.sort.SelectionSort;
import at.jku.paugujooksik.client.sort.SortAlgorithm;

public class Cards<T extends Comparable<T>> {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	private static final boolean MOVE_PIN = false;

	private final List<T> values;
	private final List<Card<T>> cards;

	private List<Action> expectedActions;
	private int curAction;

	public final Sort sort;

	public Cards(List<T> values) {
		this.values = values;
		this.cards = new LinkedList<>();
		this.sort = new Sort();
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

	public int getOne() {
		if (!one())
			throw new SelectionException("One button must be selected");
		return getSelection().get(0);
	}

	public int getLeft() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return getSelection().get(0);
	}

	public int getRight() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return getSelection().get(1);
	}

	public void select(int index) {
		if (isSelected(index))
			return;

		checkAction(Action.open(index));

		if (two()) {
			Card<T> left = cards.get(getLeft());
			Card<T> right = cards.get(getRight());
			if (!left.pinned)
				left.selected = false;
			if (!right.pinned)
				right.selected = false;
		}

		switch (getSelection().size()) {
		case 2:
			throw new SelectionException("Two cards are already pinned!");
		case 1:
			checkAction(Action.open(index, getOne()));
			cards.get(index).selected = true;
			break;
		case 0:
			cards.get(index).selected = true;
			break;

		default:
			throw new SelectionException("Invalid index: " + index);
		}
	}

	public void swapSelection() {
		if (!two())
			throw new SelectionException("Two cards need to be open.");

		checkAction(Action.swap(getLeft(), getRight()));
		final Card<T> left = cards.get(getLeft());
		final Card<T> right = cards.get(getRight());
		cards.set(getLeft(), right);
		cards.set(getRight(), left);
		if (!MOVE_PIN) {
			boolean leftPinned = left.pinned;
			boolean rightPinned = right.pinned;
			left.pinned = rightPinned;
			right.pinned = leftPinned;
		}
	}

	public void togglePin(int index) {
		final Card<T> card = cards.get(index);
		if (card.pinned) {
			checkAction(Action.unpin(index));
			card.pinned = false;
		} else {
			checkAction(Action.pin(index));
			card.pinned = true;
		}
	}

	public void toggleMark(int index) {
		final Card<T> card = cards.get(index);
		if (card.marked) {
			checkAction(Action.unmark(index));
			card.marked = false;
		} else {
			checkAction(Action.mark(index));
			card.marked = true;
		}
	}

	public boolean isMarked(int index) {
		return cards.get(index).marked;
	}

	public boolean isPinned(int index) {
		return cards.get(index).pinned;
	}

	public boolean isSelected(int index) {
		return cards.get(index).selected;
	}

	public boolean isSorted() {
		Object[] act = cards.toArray();
		Object[] exp = cards.toArray();
		Arrays.sort(exp);
		return Arrays.equals(act, exp);
	}

	public boolean zero() {
		return getSelection().size() == 0;
	}

	public boolean one() {
		return getSelection().size() == 1;
	}

	public boolean two() {
		return getSelection().size() == 2;
	}

	public void reset() {
		Collections.shuffle(values);
		DEBUGLOG.info("Generated values: " + values);
		cards.clear();
		for (T value : values) {
			cards.add(new Card<>(value));
		}
		expectedActions = sort.getCurrent().getActions(values);
		curAction = 0;
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
				throw new SelectionException(
						"Algorithm would do the following instead: " + exp);
			}
		} else if (!sort.curAlgo.allowsMoreActions()) {
			throw new SelectionException("No more actions necessary!");
		}
	}

	public class Sort {
		private List<SortAlgorithm<T>> algorithms;
		private SortAlgorithm<T> curAlgo;

		public Sort() {
			algorithms = new LinkedList<>();
			{
				algorithms.add(new PlayMode<T>());
				algorithms.add(new BubbleSort<T>());
				algorithms.add(new InsertionSort<T>());
				algorithms.add(new SelectionSort<T>());
			}
			curAlgo = algorithms.get(0);
		}

		public List<SortAlgorithm<T>> getAll() {
			return Collections.unmodifiableList(algorithms);
		}

		public SortAlgorithm<T> getCurrent() {
			return curAlgo;
		}

		public void setCurrent(int index) {
			this.curAlgo = algorithms.get(index);
		}
	}
}
