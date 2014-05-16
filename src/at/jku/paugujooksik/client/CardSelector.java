package at.jku.paugujooksik.client;

import java.util.LinkedList;
import java.util.List;

public class CardSelector {
	private final List<Integer> selection = new LinkedList<>();

	public void select(int index) {
		if (selection.contains(index))
			return;
		
		switch (selection.size()) {
		case 2:
			selection.clear();
		case 1:
		case 0:
			selection.add(index);
			break;

		default:
			System.err.println("Invalid index: " + index);
		}
	}

	public boolean isSelected(int index) {
		return selection.contains(index);
	}

	public int getLeft() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return Math.min(selection.get(0), selection.get(1));
	}

	public int getRight() {
		if (!two())
			throw new SelectionException("Two buttons must be selected!");
		return Math.max(selection.get(0), selection.get(1));
	}
	
	public int getOne() {
		if (!one())
			throw new SelectionException("One button must be selected");
		return selection.get(0);
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
	
	public class SelectionException extends RuntimeException {
		private static final long serialVersionUID = 4955230999966765025L;
		public SelectionException(String message) {
			super(message);
		}
	}
}
