package at.jku.paugujooksik.client.sort;

public abstract class Action {
	public Type type;

	public static UnaryAction open(int index) {
		return new UnaryAction(Type.OPEN, index);
	}

	public static BinaryAction open(int left, int right) {
		return new BinaryAction(Type.OPEN, left, right);
	}

	public static UnaryAction mark(int index) {
		return new UnaryAction(Type.MARK, index);
	}

	public static UnaryAction unmark(int index) {
		return new UnaryAction(Type.UNMARK, index);
	}

	public static UnaryAction pin(int index) {
		return new UnaryAction(Type.PIN, index);
	}

	public static BinaryAction swap(int left, int right) {
		return new BinaryAction(Type.SWAP, left, right);
	}

	public abstract boolean isCompatibleTo(Action other);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract String toString();

}

final class UnaryAction extends Action {
	public final int index;

	public UnaryAction(Type type, int index) {
		this.type = type;
		this.index = index;
	}

	@Override
	public boolean isCompatibleTo(Action other) {
		return equals(other);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" of card ");
		sb.append(index + 1);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UnaryAction) {
			final UnaryAction other = (UnaryAction) obj;
			return type.equals(other.type) && index == other.index;
		}
		return false;
	}
}

final class BinaryAction extends Action {
	public final int indexLeft;
	public final int indexRight;

	public BinaryAction(Type type, int indexLeft, int indexRight) {
		this.type = type;
		this.indexLeft = indexLeft;
		this.indexRight = indexRight;
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
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" of cards ");
		sb.append(indexLeft + 1);
		sb.append(" and ");
		sb.append(indexRight + 1);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BinaryAction) {
			final BinaryAction other = (BinaryAction) obj;
			return type.equals(other.type) && 
					(indexLeft == other.indexLeft && indexRight == other.indexRight 
					|| indexLeft == other.indexRight && indexRight == other.indexLeft);
		}
		return false;
	}
}

enum Type {
	COMPARE("Comparing"), SWAP("Swapping"), OPEN("Opening"), PIN("Pinning"), UNPIN(
			"Unpinning"), MARK("Marking"), UNMARK("Unmarking");
	private final String desc;

	private Type(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	};
}