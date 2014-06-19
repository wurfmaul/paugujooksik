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

public class Configuration<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = 6147443577596760181L;
	private static final boolean PRINT_VALUES = false;

	public final ValueMode mode;
	public final int size;
	public final ValueType type;
	public final List<T> values;

	private final List<SortAlgorithm<T>> algorithms = new LinkedList<>();
	private int algorithmIndex;

	private Configuration(int algorithmIndex, ValueMode mode, int size,
			ValueType type, List<T> values) {
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

	public SortAlgorithm<T> get(int index) {
		return algorithms.get(index);
	}

	public List<SortAlgorithm<T>> getAllAlgorithms() {
		return Collections.unmodifiableList(algorithms);
	}

	public SortAlgorithm<T> getAlgorithm() {
		return algorithms.get(getAlgorithmIndex());
	}

	public int getAlgorithmIndex() {
		return algorithmIndex;
	}

	@Override
	public String toString() {
		if (PRINT_VALUES)
			return String.format("%s (%d %s %s) %s", getAlgorithm(), size,
					mode, type, values);
		else
			return String.format("%s (%d %s %s)", getAlgorithm(), size, mode,
					type);
	}

	public static Configuration<?> generate(ValueMode mode, ValueType type,
			int sortIdx, int size) {
		switch (mode) {
		case SMALL:
			if (type == ValueType.NUMBERS) {
				return new Configuration<>(sortIdx, mode, size, type,
						ValueGenerator.smallIntValues(size));
			} else if (type == ValueType.LETTERS) {
				return new Configuration<>(sortIdx, mode, size, type,
						ValueGenerator.smallStringValues(size));
			}
			break;
		case RANDOM:
			if (type == ValueType.NUMBERS) {
				return new Configuration<>(sortIdx, mode, size, type,
						ValueGenerator.randomIntValues(size));
			} else if (type == ValueType.LETTERS) {
				return new Configuration<>(sortIdx, mode, size, type,
						ValueGenerator.randomStringValues(size));
			}
			break;
		}
		DEBUGLOG.severe("Unknown mode: '" + mode + "' or type: '" + type + "'");
		return null;
	}

	public static Configuration<?> generateDefault() {
		return generate(DEFAULT_MODE, DEFAULT_TYPE, 0, DEFAULT_SIZE);
	}

	public static Object[] getAlgorithmList() {
		return generateDefault().getAllAlgorithms().toArray();
	}

	public static Configuration<?> deriveWithNewSortIdx(Configuration<?> proto,
			int sortIdx) {
		return generate(proto.mode, proto.type, sortIdx, proto.size);
	}

	public static Configuration<?> deriveWithNewMode(Configuration<?> proto,
			ValueMode mode) {
		return generate(mode, proto.type, proto.getAlgorithmIndex(), proto.size);
	}

	public static Configuration<?> deriveWithNewType(Configuration<?> proto,
			ValueType type) {
		return generate(proto.mode, type, proto.getAlgorithmIndex(), proto.size);
	}

	public static Configuration<?> deriveWithNewSize(Configuration<?> proto,
			int size) {
		return generate(proto.mode, proto.type, proto.getAlgorithmIndex(), size);
	}
}
