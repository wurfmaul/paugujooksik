package at.jku.paugujooksik.client.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ValueGenerator {
	private static final int MAX_CHAR = 26;
	public static final Type DEFAULT_TYPE = Type.INTEGER;
	public static final Mode DEFAULT_MODE = Mode.SMALL;

	public Type type;
	public Mode mode;
	
	public ValueGenerator() {
		this.type = DEFAULT_TYPE;
		this.mode = DEFAULT_MODE;
	}

	public static List<Integer> smallIntValues(int size) {
		final List<Integer> values = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			values.add(i + 1);
		}
		return values;
	}
	
	public static List<String> smallStringValues(int size) {
		assert size < MAX_CHAR;
		final List<String> values = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			values.add(Character.toString((char) ('A' + i)));
		}
		return values;
	}

	public static List<Integer> randomIntValues(int size) {
		final List<Integer> values = new LinkedList<>();
		final Random rand = new Random();
		for (int i = 0; i < size; i++) {
			values.add(rand.nextInt(100));
		}
		return values;
	}

	public static List<String> randomStringValues(int size) {
		assert size < MAX_CHAR;
		final List<String> values = new LinkedList<>();
		final Random rand = new Random();
		for (int i = 0; i < size; i++) {
			values.add(Character.toString((char) ('A' + rand.nextInt(MAX_CHAR))));
		}
		return values;
	}

	public enum Mode {
		SMALL, RANDOM;
	}
	
	public enum Type {
		INTEGER, STRING;
	}
}
