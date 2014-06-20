package at.jku.paugujooksik.action;

import static at.jku.paugujooksik.action.ActionType.MARK;
import static at.jku.paugujooksik.action.ActionType.OPEN;
import static at.jku.paugujooksik.action.ActionType.PIN;
import static at.jku.paugujooksik.action.ActionType.SWAP;
import static at.jku.paugujooksik.action.ActionType.UNPIN;

import java.io.Serializable;

public abstract class Action implements Serializable {
	private static final long serialVersionUID = 9168826858554586354L;

	public ActionType type;

	@Override
	public abstract boolean equals(Object obj);

	public abstract boolean isCompatibleTo(Action other);

	@Override
	public abstract String toString();

	public static UnaryAction mark(int index) {
		return new UnaryAction(MARK, index);
	}

	public static UnaryAction open(int index) {
		return new UnaryAction(OPEN, index);
	}

	public static BinaryAction open(int left, int right) {
		return new BinaryAction(OPEN, left, right);
	}

	public static UnaryAction pin(int index) {
		return new UnaryAction(PIN, index);
	}

	public static BinaryAction swap(int left, int right) {
		return new BinaryAction(SWAP, left, right);
	}

	public static UnaryAction unpin(int index) {
		return new UnaryAction(UNPIN, index);
	}

}