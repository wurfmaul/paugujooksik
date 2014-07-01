package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.tools.Constants.DEBUGLOG;
import static at.jku.paugujooksik.tools.Constants.QUEUE_DELAY;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import at.jku.paugujooksik.action.Action;
import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.gui.CardSetContainerPanel;
import at.jku.paugujooksik.model.Cards;

public class Player {
	public final Cards<?> cards;
	public boolean animating;

	private final BlockingQueue<QueuedAction> actionQueue = new LinkedBlockingDeque<>();
	private final QueueWorker queueWorker = new QueueWorker();
	private final Presenter target;
	private CardSetContainerPanel panel;

	public Player(CardSetContainerPanel panel, Presenter target) {
		this.target = target;
		this.cards = new Cards<>(target.config);
		this.setPanel(panel);
		queueWorker.start();
	}

	public CardSetContainerPanel getPanel() {
		return panel;
	}

	public void queueAction(String clientId, Action action) {
		actionQueue.add(new QueuedAction(clientId, action));
	}

	public void setPanel(CardSetContainerPanel panel) {
		this.panel = panel;
	}

	public void updateComponents() {
		getPanel().cardSet.updateCards(cards);
		if (cards.allMarked() && cards.isFinished()) {
			cards.selectAll();
			getPanel().cardSet.updateCards(cards);
			getPanel().cardSet.finishCards(cards);
		}
		// in order to balance the animation trick (see CardSetContainerPanel.validateTree()), 
		// we have to validate manually here 
		panel.validate();
	}

	public void updateStats() {
		getPanel().setStats(cards.getCompareCount(), cards.getSwapCount(), cards.getErrorCount());
	}

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

		private void performAction(String clientId, UnaryAction action) {
			switch (action.type) {
			case OPEN:
				target.performSelect(clientId, action.index);
				break;
			case MARK:
				target.performMark(clientId, action.index);
				break;
			case PIN:
			case UNPIN:
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
				target.performSwapStart(clientId);
				break;
			default:
				DEBUGLOG.severe("Cannot perform action: '" + action + "'");
			}
		}
	}

	private class QueuedAction {
		public final Action action;
		public final String clientId;

		public QueuedAction(String clientId, Action action) {
			this.action = action;
			this.clientId = clientId;
		}
	}
}