package at.jku.paugujooksik.client.gui;

import java.util.LinkedList;
import java.util.List;

public class ValueGenerator {
	public static final Type DEFAULT_TYPE = Type.INT;
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

	public static List<Integer> defaultValues(int size) {
		return smallIntValues(size);
	}
	
	public enum Mode {
		SMALL, RANDOM;
	}
	
	public enum Type {
		INT, STRING;
	}

	public static List<String> smallStringValues(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Integer> randomIntValues(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<String> randomStringValues(int n) {
		// TODO Auto-generated method stub
		return null;
	}
}
