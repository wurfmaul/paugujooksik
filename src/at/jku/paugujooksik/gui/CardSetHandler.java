package at.jku.paugujooksik.gui;

public interface CardSetHandler {
	public void performPin(String originId, int index);

	public void performMark(String originId, int index);

	public void performSelect(String originId, int index);

	public void performSwap(String originId);
}
