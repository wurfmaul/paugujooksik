package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.QUEUE_DELAY;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import at.jku.paugujooksik.gui.CardSetContainer;
import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Action.BinaryAction;
import at.jku.paugujooksik.model.Action.UnaryAction;
import at.jku.paugujooksik.model.CardModel;

/**
 * Represents one player.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class Player {
	/** The model of this player. */
	public final CardModel<?> cardModel;
	/** Indicates whether animation is ongoing. */
	public boolean animating;

	/**
	 * This queue collects all actions in order to process them properly
	 * sequential.
	 */
	private final BlockingQueue<QueuedAction> actionQueue = new LinkedBlockingDeque<>();
	/** This poor guy has to perform all the queued actions. */
	private final QueueWorker queueWorker = new QueueWorker();
	/** The presenter that needs to be notified about the action status. */
	private final Presenter presenter;
	/** The component that holds all the cards of this player. */
	private CardSetContainer panel;

	/**
	 * Create a new player.
	 * 
	 * @param panel
	 *            See {@link #panel}.
	 * @param presenter
	 *            See {@link #presenter}.
	 */
	public Player(CardSetContainer panel, Presenter presenter) {
		this.presenter = presenter;
		this.cardModel = new CardModel<>(presenter.config);
		this.panel = panel;
		queueWorker.start();
	}

	/**
	 * @return {@link #panel}.
	 */
	public CardSetContainer getPanel() {
		return panel;
	}

	/**
	 * Add another action to the queue.
	 * 
	 * @param clientId
	 *            The performer of the specified action.
	 * @param action
	 *            The action that shall be queued.
	 */
	public void queueAction(String clientId, Action action) {
		actionQueue.add(new QueuedAction(clientId, action));
	}

	/**
	 * Sets the {@link #panel}.
	 * 
	 * @param panel
	 *            The {@link #panel}.
	 */
	public void setPanel(CardSetContainer panel) {
		this.panel = panel;
	}

	/**
	 * Synchronize all components with the model.
	 */
	public void synchronizeComponents() {
		getPanel().cardSet.synchronize(cardModel);
		if (cardModel.allMarked() && cardModel.isFinished()) {
			cardModel.selectAll();
			getPanel().cardSet.synchronize(cardModel);
			getPanel().cardSet.finishCards(cardModel);
		}
	}

	/**
	 * Synchronize the statistics with the model.
	 */
	public void synchronizeStats() {
		getPanel().setStats(cardModel.getCompareCount(), cardModel.getSwapCount(), cardModel.getErrorCount());
	}

	/**
	 * This lad works permanently in order to keep the queue tidy.
	 */
	private class QueueWorker extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					QueuedAction qAction = actionQueue.take();
					if (qAction.action instanceof UnaryAction)
						performAction(qAction.clientId, (UnaryAction) qAction.action);
					else if (qAction.action instanceof BinaryAction)
						performAction(qAction.clientId, (BinaryAction) qAction.action);
					while (animating) {
						Thread.sleep(200);
					}
					Thread.sleep(QUEUE_DELAY);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Delegate unary actions to the presenter.
		 * 
		 * @param clientId
		 *            The client that produced this action.
		 * @param action
		 *            The produced action.
		 */
		private void performAction(String clientId, UnaryAction action) {
			switch (action.type) {
			case OPEN:
				presenter.performSelect(clientId, action.index);
				break;
			case MARK:
				presenter.performMark(clientId, action.index);
				break;
			case PIN:
			case UNPIN:
				presenter.performPin(clientId, action.index);
				break;
			default:
				DEBUGLOG.severe("Cannot perform action: '" + action + "'");
			}
		}

		/**
		 * Delegate binary actions to the presenter.
		 * 
		 * @param clientId
		 *            The client that produced this action.
		 * @param action
		 *            The produced action.
		 */
		private void performAction(String clientId, BinaryAction action) {
			switch (action.type) {
			case OPEN:
				if (cardModel.isSelected(action.indexLeft))
					presenter.performSelect(clientId, action.indexRight);
				else
					presenter.performSelect(clientId, action.indexLeft);
				break;
			case SWAP:
				presenter.performSwapStart(clientId);
				break;
			default:
				DEBUGLOG.severe("Cannot perform action: '" + action + "'");
			}
		}
	}

	/**
	 * A struct that matches one action to one client.
	 */
	private class QueuedAction {
		public final Action action;
		public final String clientId;

		public QueuedAction(String clientId, Action action) {
			this.action = action;
			this.clientId = clientId;
		}
	}
}