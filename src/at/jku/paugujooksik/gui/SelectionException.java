package at.jku.paugujooksik.gui;

public class SelectionException extends RuntimeException {
	private static final long serialVersionUID = 4955230999966765025L;
	private boolean isDuplicate;

	public SelectionException(String message) {
		this(message, false);
	}

	public SelectionException(String message, boolean isDuplicate) {
		super(message);
		this.isDuplicate = isDuplicate;
	}

	public boolean isDuplicate() {
		return isDuplicate;
	}
}