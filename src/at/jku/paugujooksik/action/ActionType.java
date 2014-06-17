package at.jku.paugujooksik.action;

public enum ActionType {
	SWAP, 
	OPEN, 
	PIN, 
	MARK, 
	UNMARK;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	};
}
