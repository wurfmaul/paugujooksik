package at.jku.paugujooksik.action;

public final class UnaryAction extends Action {
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
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" of card ");
		sb.append(index + 1);
		return sb.toString();
	}
}
