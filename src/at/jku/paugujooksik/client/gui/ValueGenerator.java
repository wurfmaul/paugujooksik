package at.jku.paugujooksik.client.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ValueGenerator {
	public static List<Integer> intValues(int size) {
		final List<Integer> values = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			values.add(i + 1);
		}
		return Collections.unmodifiableList(values);
	}
}
