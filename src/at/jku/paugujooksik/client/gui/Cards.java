package at.jku.paugujooksik.client.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.client.sort.Action;

public class Cards<T extends Comparable<T>> {
	private final List<Card<T>> cards;
	private final List<Action> expectedActions;
	private int curAction;

	public Cards(List<T> values, List<Action> expectedActions) {
		this.cards = new LinkedList<>();
		{
			for (T value : values) {
				cards.add(new Card<>(value));
			}
		}
		this.expectedActions = Collections.unmodifiableList(expectedActions);
		this.curAction = 0;
	}

	public void select(int index) {
		if (isSelected(index))
			return;

		if (!checkAction(Action.open(index)))
			return;
		
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
			if (checkAction(Action.open(index, getOne())))
				cards.get(index).selected = true;
			break;
		case 0:
			if (checkAction(Action.open(index)))
				cards.get(index).selected = true;
			break;

		default:
			throw new SelectionException("Invalid index: " + index);
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

	public void togglePin(int index) {
		final Card<T> card = cards.get(index);
		if (card.pinned) {
			if (checkAction(Action.unpin(index)))
				card.pinned = false;
		} else {
			if (checkAction(Action.unpin(index)))
				card.pinned = true;
		}
	}

	public void toggleMark(int index) {
		final Card<T> card = cards.get(index);
		if (card.marked) {
			if (checkAction(Action.unmark(index)))
				card.marked = false;
		} else {
			if (checkAction(Action.mark(index)))
				card.marked = true;
		}
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

	public int getOne() {
		if (!one())
			throw new SelectionException("One button must be selected");
		return getSelection().get(0);
	}

	public void reset() {
		// clear flags
		for (Card<T> c : cards) {
			c.reset();
		}
		Collections.shuffle(cards);
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

	public Card<T> get(int index) {
		return cards.get(index);
	}

	public void swapSelection() {
		if (!two())
			throw new SelectionException("Two cards need to be open.");

		if (checkAction(Action.swap(getLeft(), getRight()))) {
			final Card<T> left = cards.get(getLeft());
			final Card<T> right = cards.get(getRight());
			cards.set(getLeft(), right);
			cards.set(getRight(), left);
		}
	}

	public boolean success() {
		Object[] act = cards.toArray();
		Object[] exp = cards.toArray();
		Arrays.sort(exp);
		return Arrays.equals(act, exp);
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

	private boolean checkAction(Action action) {
		if (curAction < expectedActions.size()) {
			final Action exp = expectedActions.get(curAction);
			if (exp.isCompatibleTo(action)) {
				if (exp.equals(action))
					curAction++;
				return true;
			} else {
				System.out.println("Algorithm would do the following instead: " + exp);
				return false;
			}
		}
		System.out.println("No more actions necessary!");
		return false;
	}
}
