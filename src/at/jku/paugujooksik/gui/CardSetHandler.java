package at.jku.paugujooksik.gui;

public interface CardSetHandler {
	public void performPin(String clientId, int index);

	public void performMark(String clientId, int index);

	public void performSelect(String clientId, int index);

	public void performSwap(String clientId);

	public void finishSwap(String clientId);
}
