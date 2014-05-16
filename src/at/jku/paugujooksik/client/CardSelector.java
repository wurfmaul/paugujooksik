package at.jku.paugujooksik.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardSelector {
	private final List<Card> selection = new LinkedList<>();
	
	public boolean pin(int index) {
		int i = selection.indexOf(new Card(index));
		if (i < 0 || 1 < i)
			throw new SelectionException("Element can not be pinned: " + index);
		if (selection.get(i).pinned)
			return false;
		
		selection.get(i).pinned = true;
		return true;
	}
	
	public boolean select(int index) {
		if (isSelected(index))
			return true;
		
		switch (selection.size()) {
		case 2:
			if (!selection.get(1).pinned)
				selection.remove(1);
			if (!selection.get(0).pinned)
				selection.remove(0);
			if (two()) {
				System.err.println("Two cards are already pinned!");
				return false;
			}
		case 1:
		case 0:
			selection.add(new Card(index));
			break;

		default:
			System.err.println("Invalid index: " + index);
			return false;
		}
		Collections.sort(selection);
		return true;
	}

	public boolean isPinned(int index) {
		final int i = selection.indexOf(new Card(index));
		return i > -1 && selection.get(i).pinned;
	}
	
	public boolean isSelected(int index) {
		return selection.contains(new Card(index));
	}

	public void togglePin(int index) {
		final int i = selection.indexOf(new Card(index));
		selection.get(i).pinned = !selection.get(i).pinned;
	}

	public int getLeft() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return selection.get(0).index;
	}

	public int getRight() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return selection.get(1).index;
	}
	
	public int getOne() {
		if (!one())
			throw new SelectionException("One button must be selected");
		return selection.get(0).index;
	}

	public void reset() {
		selection.clear();
	}

	public boolean zero() {
		return selection.size() == 0;
	}

	public boolean one() {
		return selection.size() == 1;
	}

	public boolean two() {
		return selection.size() == 2;
	}
	
	private class SelectionException extends RuntimeException {
		private static final long serialVersionUID = 4955230999966765025L;
		public SelectionException(String message) {
			super(message);
		}
	}
	
	private class Card implements Comparable<Card> {
		public final int index;
		public boolean pinned = false;
		
		public Card(int index) {
			this.index = index;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof Card && compareTo((Card) obj) == 0;
		}

		@Override
		public int compareTo(Card o) {
			return index - o.index;
		}
	}
}
