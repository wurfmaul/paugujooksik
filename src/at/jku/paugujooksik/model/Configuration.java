package at.jku.paugujooksik.model;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_MODE;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_SIZE;
import static at.jku.paugujooksik.tools.Constants.DEFAULT_TYPE;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.jku.paugujooksik.model.ValueGenerator.ValueMode;
import at.jku.paugujooksik.model.ValueGenerator.ValueType;
import at.jku.paugujooksik.sort.BubbleSort;
import at.jku.paugujooksik.sort.InsertionSort;
import at.jku.paugujooksik.sort.PlayMode;
import at.jku.paugujooksik.sort.SelectionSort;
import at.jku.paugujooksik.sort.SortAlgorithm;

/**
 * Contains the basic configuration of a game.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @param <T>
 *            can be any of {@link Comparable}.
 */
public class Configuration<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = 6147443577596760181L;
	private static final boolean PRINT_VALUES = false;

	/** Specifies in which way the values are computed. */
	public final ValueMode mode;
	/** The amount of cards. */
	public final int size;
	/** Specifies the values' types. */
	public final ValueType type;
	/** Contains the generated values. */
	public final List<T> values;

	/** Contains a list of available algorithms. */
	private final List<SortAlgorithm<T>> algorithms = new LinkedList<>();
	/** Specifies which algorithm of the list to use. */
	private int algorithmIndex;

	private Configuration(int algorithmIndex, ValueMode mode, int size, ValueType type, List<T> values) {
		this.algorithmIndex = algorithmIndex;
		this.mode = mode;
		this.size = size;
		this.type = type;
		this.values = values;

		Collections.shuffle(this.values);

		algorithms.add(new PlayMode<T>());
		algorithms.add(new BubbleSort<T>());
		algorithms.add(new InsertionSort<T>());
		algorithms.add(new SelectionSort<T>());
	}

	/**
	 * @param index
	 *            The index of the algorithm in the list.
	 * @return The algorithm at the specified index.
	 */
	public SortAlgorithm<T> get(int index) {
		return algorithms.get(index);
	}

	/**
	 * @return A list of all available algorithms.
	 */
	public List<SortAlgorithm<T>> getAllAlgorithms() {
		return Collections.unmodifiableList(algorithms);
	}

	/**
	 * @return the currently active algorithm.
	 */
	public SortAlgorithm<T> getAlgorithm() {
		return algorithms.get(getAlgorithmIndex());
	}

	/**
	 * @return the index of the currently active algorithm.
	 */
	public int getAlgorithmIndex() {
		return algorithmIndex;
	}

	@Override
	public String toString() {
		if (PRINT_VALUES)
			return String.format("%s (%d %s %s) %s", getAlgorithm(), size, mode, type, values);
		else
			return String.format("%s (%d %s %s)", getAlgorithm(), size, mode, type);
	}

	/**
	 * Generates a new configuration from the specified
	 * 
	 * @param mode
	 *            See {@link #mode}.
	 * @param type
	 *            See {@link #type}.
	 * @param sortIdx
	 *            See {@link #algorithmIndex}.
	 * @param size
	 *            See {@link #size}.
	 * @return a new configuration.
	 */
	public static Configuration<?> generate(ValueMode mode, ValueType type, int sortIdx, int size) {
		switch (mode) {
		case SMALL:
			if (type == ValueType.NUMBERS) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.smallNumberValues(size));
			} else if (type == ValueType.LETTERS) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.smallLetterValues(size));
			}
			break;
		case RANDOM:
			if (type == ValueType.NUMBERS) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.randomNumberValues(size));
			} else if (type == ValueType.LETTERS) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.randomLetterValues(size));
			}
			break;
		}
		DEBUGLOG.severe("Unknown mode: '" + mode + "' or type: '" + type + "'");
		return null;
	}

	/**
	 * @return a new configuration based on the default values.
	 */
	public static Configuration<?> generateDefault() {
		return generate(DEFAULT_MODE, DEFAULT_TYPE, 0, DEFAULT_SIZE);
	}

	/**
	 * @return an array of all the algorithms.
	 */
	public static Object[] getAlgorithmList() {
		return generateDefault().getAllAlgorithms().toArray();
	}

	/**
	 * @param proto
	 *            Take all options from this prototype but the algorithm index.
	 * @param sortIdx
	 *            The new algorithm's index.
	 * @return a new configuration based on a prototype.
	 */
	public static Configuration<?> deriveWithNewSortIdx(Configuration<?> proto, int sortIdx) {
		return generate(proto.mode, proto.type, sortIdx, proto.size);
	}

	/**
	 * @param proto
	 *            Take all options from this prototype but the value generation
	 *            mode.
	 * @param sortIdx
	 *            The new value generation mode.
	 * @return a new configuration based on a prototype.
	 */
	public static Configuration<?> deriveWithNewMode(Configuration<?> proto, ValueMode mode) {
		return generate(mode, proto.type, proto.getAlgorithmIndex(), proto.size);
	}

	/**
	 * @param proto
	 *            Take all options from this prototype but the types of the
	 *            values.
	 * @param sortIdx
	 *            The values' types.
	 * @return a new configuration based on a prototype.
	 */
	public static Configuration<?> deriveWithNewType(Configuration<?> proto, ValueType type) {
		return generate(proto.mode, type, proto.getAlgorithmIndex(), proto.size);
	}

	/**
	 * @param proto
	 *            Take all options from this prototype but the number of cards.
	 * @param sortIdx
	 *            The new number of cards.
	 * @return a new configuration based on a prototype.
	 */
	public static Configuration<?> deriveWithNewSize(Configuration<?> proto, int size) {
		return generate(proto.mode, proto.type, proto.getAlgorithmIndex(), size);
	}
}
