package at.jku.paugujooksik.sort;

import java.util.List;

import at.jku.paugujooksik.model.Action;

/**
 * Another algorithm that allows any action.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @param <T>
 *            can be any of {@link Comparable}.
 */
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
