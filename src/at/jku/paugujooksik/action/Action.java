package at.jku.paugujooksik.action;

import java.io.Serializable;

public abstract class Action implements Serializable {
	private static final long serialVersionUID = 9168826858554586354L;
	public ActionType type;

	public static UnaryAction open(int index) {
		return new UnaryAction(ActionType.OPEN, index);
	}

	public static BinaryAction open(int left, int right) {
		return new BinaryAction(ActionType.OPEN, left, right);
	}

	public static UnaryAction mark(int index) {
		return new UnaryAction(ActionType.MARK, index);
	}

	public static UnaryAction unmark(int index) {
		return new UnaryAction(ActionType.UNMARK, index);
	}

	public static UnaryAction pin(int index) {
		return new UnaryAction(ActionType.PIN, index);
	}

	public static BinaryAction swap(int left, int right) {
		return new BinaryAction(ActionType.SWAP, left, right);
	}

	public abstract boolean isCompatibleTo(Action other);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract String toString();

}