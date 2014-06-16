package at.jku.paugujooksik.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ValueGenerator {
	private static final int MAX_CHAR = 26;

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

		@Override
		public String toString() {
			return name().toLowerCase();
		};
	}

	public enum Type {
		INTEGER, STRING;

		@Override
		public String toString() {
			assert name().length() > 1;
			final StringBuilder sb = new StringBuilder();
			sb.append(name().charAt(0));
			sb.append(name().substring(1).toLowerCase());
			sb.append("s");
			return sb.toString();
		};
	}
}
