package at.jku.paugujooksik.logic;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import at.jku.paugujooksik.logic.ValueGenerator.Mode;
import at.jku.paugujooksik.logic.ValueGenerator.Type;

public class Configuration<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = 6147443577596760181L;
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	
	public static final int MIN_SIZE = 7;
	public static final int MAX_SIZE = 15;
	public static final int DEFAULT_SIZE = 7;
	public static final Type DEFAULT_TYPE = Type.INTEGER;
	public static final Mode DEFAULT_MODE = Mode.SMALL;

	public final int algorithmIndex;
	public final Mode mode;
	public final int size;
	public final Type type;
	public final List<T> values;

	private Configuration(int algorithmIndex, Mode mode, int size, Type type,
			List<T> values) {
		this.algorithmIndex = algorithmIndex;
		this.mode = mode;
		this.size = size;
		this.type = type;
		this.values = values;
	}

	public static Configuration<?> generate(Mode mode, Type type, int sortIdx, int size) {
		switch (mode) {
		case SMALL:
			if (type == Type.INTEGER) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.smallIntValues(size));
			} else if (type == Type.STRING) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.smallStringValues(size));
			}
			break;
		case RANDOM:
			if (type == Type.INTEGER) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.randomIntValues(size));
			} else if (type == Type.STRING) {
				return new Configuration<>(sortIdx, mode, size, type, ValueGenerator.randomStringValues(size));
			}
			break;
		}
		DEBUGLOG.severe("Unknown mode: '" + mode + "' or type: '" + type + "'");
		return null; // TODO exception system?
	}
	
	public static Configuration<?> generateDefault() {
		return generate(DEFAULT_MODE, DEFAULT_TYPE, 0, DEFAULT_SIZE);
	}
	
	public static Configuration<?> deriveWithNewSortIdx(Configuration<?> proto, int sortIdx) {
		return generate(proto.mode, proto.type, sortIdx, proto.size);
	}
	
	public static Configuration<?> deriveWithNewMode(Configuration<?> proto, Mode mode) {
		return generate(mode, proto.type, proto.algorithmIndex, proto.size);
	}
	
	public static Configuration<?> deriveWithNewType(Configuration<?> proto, Type type) {
		return generate(proto.mode, type, proto.algorithmIndex, proto.size);
	}
	
	public static Configuration<?> deriveWithNewSize(Configuration<?> proto, int size) {
		return generate(proto.mode, proto.type, proto.algorithmIndex, size);
	}
}
