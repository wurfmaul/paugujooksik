package at.jku.paugujooksik.gui;

public interface PresentationView {
	public boolean isProcessing(String clientId);

	public void performMark(String clientId, int index);

	public void performPin(String clientId, int index);

	public void performSelect(String clientId, int index);

	public void performSwapStart(String clientId);

	public void performSwapStop(String clientId);
}