package at.jku.paugujooksik.action;

public enum ActionType {
	MARK, OPEN, PIN, SWAP, UNMARK;

	@Override
	public String toString() {
		return name().toLowerCase();
	};
}
