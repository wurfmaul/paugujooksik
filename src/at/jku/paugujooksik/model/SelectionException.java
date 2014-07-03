package at.jku.paugujooksik.model;

/**
 * This exception represents a mistake, the player has produced.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class SelectionException extends RuntimeException {
	private static final long serialVersionUID = 4955230999966765025L;
	private boolean isDuplicate;

	public SelectionException(String message) {
		this(message, false);
	}

	/**
	 * In order to avoid reporting the same mistake over and over again,
	 * duplicates were introduced. A duplicate indicates that this mistake has
	 * been performed before.
	 * 
	 * @param message
	 *            The error message.
	 * @param isDuplicate
	 *            True, if the mistake has been performed immediately prior to
	 *            this mistake as well.
	 */
	public SelectionException(String message, boolean isDuplicate) {
		super(message);
		this.isDuplicate = isDuplicate;
	}

	/**
	 * @return true if the same mistake has been performed again.
	 */
	public boolean isDuplicate() {
		return isDuplicate;
	}
}