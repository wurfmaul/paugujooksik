package at.jku.paugujooksik.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.client.gui.ClientGUI;
import at.jku.paugujooksik.client.sort.Action;
import at.jku.paugujooksik.client.sort.BubbleSort;
import at.jku.paugujooksik.client.sort.InsertionSort;
import at.jku.paugujooksik.client.sort.SelectionSort;
import at.jku.paugujooksik.client.sort.SortAlgorithm;

public class Client {
	private static final int MAX_CARDS = 7;
	
	public static void main(String[] args) {
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
		algorithm = new SelectionSort<>();
		algorithm = new BubbleSort<>();
		
		final List<Action> actions = algorithm.getActions(values);
		ClientGUI.initAndRun(values, actions);
	}
}
