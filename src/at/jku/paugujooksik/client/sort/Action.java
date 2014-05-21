package at.jku.paugujooksik.client.sort;

public abstract class Action {
	public Type type;

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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" of index ");
		sb.append(index + 1);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof UnaryAction && index == ((UnaryAction) obj).index;
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" of index ");
		sb.append(indexLeft + 1);
		sb.append(" and ");
		sb.append(indexRight + 1);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BinaryAction))
			return false;

		final BinaryAction other = (BinaryAction) obj;
		return indexLeft == other.indexLeft && indexRight == other.indexRight
				|| indexLeft == other.indexRight
				&& indexRight == other.indexLeft;
	}
}

enum Type {
	COMPARE("Comparing"), SWAP("Swapping"), OPEN("Opening"), PIN("Pinning"), UNPIN(
			"Unpinning"), MARK("Marking");
	private final String desc;

	private Type(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	};
}