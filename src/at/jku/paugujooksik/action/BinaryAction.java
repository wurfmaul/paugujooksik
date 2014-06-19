package at.jku.paugujooksik.action;

public final class BinaryAction extends Action {
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
					&& (indexLeft == other.indexLeft
							&& indexRight == other.indexRight || indexLeft == other.indexRight
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
		return String.format("%s cards %d and %d", type, indexLeft + 1,
				indexRight + 1);
	}
}
