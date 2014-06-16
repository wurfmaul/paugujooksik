package at.jku.paugujooksik.action;

public enum ActionType {
	COMPARE("Comparing"), 
	SWAP("Swapping"), 
	OPEN("Opening"), 
	PIN("Pinning"), 
	MARK("Marking"), 
	UNMARK("Unmarking");
	
	private final String desc;

	private ActionType(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return desc;
	};
}
