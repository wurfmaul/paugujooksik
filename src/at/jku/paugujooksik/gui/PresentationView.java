package at.jku.paugujooksik.gui;

/**
 * Every view that represents the game has to implement this interface.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public interface PresentationView {
	/**
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 * @return true if the view is still processing an action (e.g. animation).
	 */
	public boolean isProcessing(String clientId);

	/**
	 * Marks the specified card of the player's set.
	 * 
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 * @param index
	 *            The index of the card that should be marked.
	 */
	public void performMark(String clientId, int index);

	/**
	 * Pins the specified card of the player's set.
	 * 
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 * @param index
	 *            The index of the card that should be pinned.
	 */
	public void performPin(String clientId, int index);

	/**
	 * Selects the specified card of the player's set.
	 * 
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 * @param index
	 *            The index of the card that should be selected.
	 */
	public void performSelect(String clientId, int index);

	/**
	 * Initiates the swap of the two previously selected cards of the player's
	 * set.
	 * 
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 */
	public void performSwapStart(String clientId);

	/**
	 * Finalizes the swap of the two previously selected cards of the player's
	 * set.
	 * 
	 * @param clientId
	 *            If more than one player is presented on the view, the clientId
	 *            identifies the player.
	 */
	public void performSwapStop(String clientId);
}