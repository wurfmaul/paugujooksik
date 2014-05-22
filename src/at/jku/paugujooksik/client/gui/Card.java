package at.jku.paugujooksik.client.gui;

public class Card<T extends Comparable<T>> implements Comparable<Card<T>> {
	public final T value;
	public boolean pinned = false;
	public boolean marked = false;
	public boolean selected = false;

	public Card(T value) {
		this.value = value;
	}

	public void reset() {
		pinned = false;
		marked = false;
		selected = false;
	}

	@Override
	public int compareTo(Card<T> o) {
		return value.compareTo(o.value);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}