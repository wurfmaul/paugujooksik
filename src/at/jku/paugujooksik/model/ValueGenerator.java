package at.jku.paugujooksik.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Generates new values by specified attributes.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class ValueGenerator {
	private static final int MAX_CHAR = 26;

	/**
	 * @param size
	 *            The number of values.
	 * @return the first integers.
	 */
	public static List<Integer> smallNumberValues(int size) {
		final List<Integer> values = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			values.add(i + 1);
		}
		return values;
	}

	/**
	 * @param size
	 *            The number of values.
	 * @return random integers.
	 */
	public static List<String> smallLetterValues(int size) {
		assert size < MAX_CHAR;
		final List<String> values = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			values.add(Character.toString((char) ('A' + i)));
		}
		return values;
	}

	/**
	 * @param size
	 *            The number of values.
	 * @return the first letters of the alphabet.
	 */
	public static List<Integer> randomNumberValues(int size) {
		final List<Integer> values = new LinkedList<>();
		final Random rand = new Random();
		for (int i = 0; i < size; i++) {
			values.add(rand.nextInt(100));
		}
		return values;
	}

	/**
	 * @param size
	 *            The number of values.
	 * @return random letters of the alphabet.
	 */
	public static List<String> randomLetterValues(int size) {
		assert size < MAX_CHAR;
		final List<String> values = new LinkedList<>();
		final Random rand = new Random();
		for (int i = 0; i < size; i++) {
			values.add(Character.toString((char) ('A' + rand.nextInt(MAX_CHAR))));
		}
		return values;
	}

	/**
	 * Specifies in which way the values are generatied.
	 */
	public enum ValueMode {
		SMALL, RANDOM;

		@Override
		public String toString() {
			return name().toLowerCase();
		};
	}

	/**
	 * Specifies the values' types.
	 */
	public enum ValueType {
		NUMBERS, LETTERS;

		@Override
		public String toString() {
			assert name().length() > 1;
			final StringBuilder sb = new StringBuilder();
			sb.append(name().charAt(0));
			sb.append(name().substring(1).toLowerCase());
			return sb.toString();
		};
	}
}
