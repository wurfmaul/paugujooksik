package at.jku.paugujooksik.gui;

public interface CardSetHandler {
	public void performPin(int index);

	public void performMark(int index);

	public void performSelect(int index);

	public void performSwap();
}
