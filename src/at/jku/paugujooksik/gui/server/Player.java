package at.jku.paugujooksik.gui.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.gui.CardSetContainerPanel;
import at.jku.paugujooksik.model.Cards;

public class Player {
	private static final Logger DEBUGLOG = Logger.getLogger("DEBUG");

	public final Cards<?> cards;
	public final CardSetContainerPanel panel;
	public boolean animating;

	private final BlockingQueue<QueuedAction> actionQueue = new LinkedBlockingDeque<>();
	private final QueueWorker queueWorker = new QueueWorker();
	private final Presenter target;

	public Player(CardSetContainerPanel panel, Presenter target) {
		this.target = target;
		this.cards = new Cards<>(target.getConfig());
		this.panel = panel;
		queueWorker.start();
	}

	public void queueAction(String clientId, Action action) {
		actionQueue.add(new QueuedAction(clientId, action));
	}

	public void updateComponents() {
		panel.cardSet.updateCards(cards);
		if (cards.allMarked()) {
			cards.selectAll();
			panel.cardSet.updateCards(cards);
			panel.cardSet.finishCards(cards);
		}
	}

	public void updateStats() {
		panel.setStats(cards.getCompareCount(), cards.getSwapCount(),
				cards.getErrorCount());
	}

	private class QueueWorker extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					QueuedAction qAction = actionQueue.take();
					if (qAction.action instanceof UnaryAction)
						performAction(qAction.clientId,
								(UnaryAction) qAction.action);
					else if (qAction.action instanceof BinaryAction)
						performAction(qAction.clientId,
								(BinaryAction) qAction.action);
					while (animating) {
						Thread.sleep(200);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void performAction(String clientId, UnaryAction action) {
			switch (action.type) {
			case OPEN:
				target.performSelect(clientId, action.index);
				break;
			case MARK:
			case UNMARK:
				target.performMark(clientId, action.index);
				break;
			case PIN:
				target.performPin(clientId, action.index);
				break;
			default:
				DEBUGLOG.severe("Cannot perform action: '" + action + "'");
			}
		}

		private void performAction(String clientId, BinaryAction action) {
			switch (action.type) {
			case OPEN:
				if (cards.isSelected(action.indexLeft))
					target.performSelect(clientId, action.indexRight);
				else
					target.performSelect(clientId, action.indexLeft);
				break;
			case SWAP:
				target.performSwap(clientId);
				break;
			default:
				DEBUGLOG.severe("Cannot perform action: '" + action + "'");
			}
		}
	}

	private class QueuedAction {
		public final String clientId;
		public final Action action;

		public QueuedAction(String clientId, Action action) {
			this.clientId = clientId;
			this.action = action;
		}
	}
}