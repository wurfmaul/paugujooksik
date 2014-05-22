package at.jku.paugujooksik.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.paugujooksik.client.gui.ClientGUI;
import at.jku.paugujooksik.client.mode.IntMode;
import at.jku.paugujooksik.client.mode.Mode;
import at.jku.paugujooksik.client.sort.Action;
import at.jku.paugujooksik.client.sort.BubbleSort;
import at.jku.paugujooksik.client.sort.InsertionSort;
import at.jku.paugujooksik.client.sort.SelectionSort;
import at.jku.paugujooksik.client.sort.SortAlgorithm;

public class Client {
	private static final Logger DEBUG = Logger.getLogger("DEBUG");
	private static final int MAX_CARDS = 7;
	private static final List<Mode> modes = new LinkedList<>();

	static {
		modes.add(new IntMode());
	}

	public static void main(String[] args) {
		DEBUG.setLevel(Level.ALL);
		runIntCards();
	}

	private static void runIntCards() {
		List<Integer> values = new LinkedList<>();
		{
			for (int i = 0; i < MAX_CARDS; i++) {
				values.add(i + 1);
			}
			Collections.shuffle(values);
		}

		SortAlgorithm<Integer> algorithm;
		algorithm = new InsertionSort<>();
		algorithm = new BubbleSort<>();
		algorithm = new SelectionSort<>();

		final List<Action> actions = algorithm.getActions(values);
		ClientGUI.initAndRun(values, actions);
	}
}
