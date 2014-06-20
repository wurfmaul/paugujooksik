package at.jku.paugujooksik.action;

public enum ActionType {
	MARK, OPEN, PIN, SWAP, UNPIN;

	@Override
	public String toString() {
		return name().toLowerCase();
	};
}
