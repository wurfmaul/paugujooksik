package at.jku.paugujooksik.sort;

import java.util.List;

import at.jku.paugujooksik.action.Action;

public class PlayMode<T extends Comparable<T>> extends SortAlgorithm<T> {
	private static final long serialVersionUID = 8007175329327173679L;

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
