package at.jku.paugujooksik.sort;

import java.util.List;

public class PlayMode<T extends Comparable<T>> extends SortAlgorithm<T> {

	@Override
	public List<Action> getActions(List<T> values) {
		setup(values);
		return actions;
	}
	
	@Override
	public boolean allowsMoreActions() {
		return true;
	}

}
