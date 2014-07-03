package at.jku.paugujooksik.model;

import java.io.Serializable;

/**
 * Represents one user action. Actions can either be unary (e.g. selection) or
 * binary (e.g. swap).
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public abstract class Action implements Serializable {
	private static final long serialVersionUID = 9168826858554586354L;

	public ActionType type;

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * A unary action can be part of a binary action (e.g. selecting one card
	 * should be part of a selection of the same plus another card). In order to
	 * accomplish this task, we call an action compatible to another one, if the
	 * other one is part of this action.
	 * 
	 * @param other
	 *            The other action.
	 * @return True if the other action is part of this action.
	 */
	public abstract boolean isCompatibleTo(Action other);

	@Override
	public abstract String toString();

	/**
	 * Create a new mark-action
	 * 
	 * @param index
	 *            The card index that is about to be marked.
	 * @return A new Mark-Action
	 */
	public static UnaryAction mark(int index) {
		return new UnaryAction(ActionType.MARK, index);
	}

	/**
	 * Create a new select-action (e.g. turning one card)
	 * 
	 * @param index
	 *            The card index that is about to be selected.
	 * @return A new Open-Action.
	 */
	public static UnaryAction open(int index) {
		return new UnaryAction(ActionType.OPEN, index);
	}

	/**
	 * Create a new select-action (e.g. turning a second card)
	 * 
	 * @param left
	 *            The first card index that is about to be selected.
	 * @param right
	 *            The second card index that is about to be selected.
	 * @return A new Open-Action.
	 */
	public static BinaryAction open(int left, int right) {
		return new BinaryAction(ActionType.OPEN, left, right);
	}

	/**
	 * Create a new pin-action
	 * 
	 * @param index
	 *            The card index that is about to be pinned.
	 * @return A new Pin-Action.
	 */
	public static UnaryAction pin(int index) {
		return new UnaryAction(ActionType.PIN, index);
	}

	/**
	 * Create a new swap-action (e.g. exchanging two cards).
	 * 
	 * @param left
	 *            The first card index that is about to be swapped.
	 * @param right
	 *            The second card index that is about to be swapped.
	 * @return A new Swap-Action.
	 */
	public static BinaryAction swap(int left, int right) {
		return new BinaryAction(ActionType.SWAP, left, right);
	}

	/**
	 * Create a new unpin-action
	 * 
	 * @param index
	 *            The card index that is about to be unpinned.
	 * @return A new Unpin-Action.
	 */
	public static UnaryAction unpin(int index) {
		return new UnaryAction(ActionType.UNPIN, index);
	}

	/**
	 * Represents an action that requires only one card.
	 */
	public static final class UnaryAction extends Action {
		private static final long serialVersionUID = 2482948068529637542L;

		public final int index;

		public UnaryAction(ActionType type, int index) {
			this.type = type;
			this.index = index;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof UnaryAction) {
				final UnaryAction other = (UnaryAction) obj;
				return type.equals(other.type) && index == other.index;
			}
			return false;
		}

		@Override
		public boolean isCompatibleTo(Action other) {
			return equals(other);
		}

		@Override
		public String toString() {
			return String.format("%s card %d", type, index + 1);
		}
	}

	/**
	 * Represents an action that requires two cards.
	 */
	public static final class BinaryAction extends Action {
		private static final long serialVersionUID = -4425358663271600895L;

		public final int indexLeft;
		public final int indexRight;

		public BinaryAction(ActionType type, int indexLeft, int indexRight) {
			this.type = type;
			this.indexLeft = indexLeft;
			this.indexRight = indexRight;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof BinaryAction) {
				final BinaryAction other = (BinaryAction) obj;
				return type.equals(other.type)
						&& (indexLeft == other.indexLeft && indexRight == other.indexRight || indexLeft == other.indexRight
								&& indexRight == other.indexLeft);
			}
			return false;
		}

		@Override
		public boolean isCompatibleTo(Action other) {
			if (type.equals(other.type) && other instanceof UnaryAction) {
				final int index = ((UnaryAction) other).index;
				if (index == indexLeft || index == indexRight)
					return true;
			}
			return equals(other);
		}

		@Override
		public String toString() {
			return String.format("%s cards %d and %d", type, indexLeft + 1, indexRight + 1);
		}
	}

	/**
	 * Collects the possible types of {@link Action}s.
	 */
	public static enum ActionType {
		MARK, OPEN, PIN, SWAP, UNPIN;

		@Override
		public String toString() {
			return name().toLowerCase();
		};
	}

}