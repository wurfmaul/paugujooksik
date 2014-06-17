package at.jku.paugujooksik.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import at.jku.paugujooksik.logic.Cards;

public class CardSet extends JPanel {
	private static final long serialVersionUID = 8943110545308400878L;

	private final List<CardPanel> cardBtns = new LinkedList<>();

	public CardSet(int size, CardSetHandler target, String originId,
			boolean enableMouseActions) {
		final GridBagLayout gblPnlCards = new GridBagLayout();
		{
			gblPnlCards.columnWidths = new int[size + 1];
			gblPnlCards.rowHeights = new int[] { 0 };
			double[] weights = new double[size + 1];
			{
				Arrays.fill(weights, 1.0);
				weights[weights.length - 1] = Double.MIN_VALUE;
			}
			gblPnlCards.columnWeights = weights;
			gblPnlCards.rowWeights = new double[] { 1.0 };
		}
		setLayout(gblPnlCards);

		cardBtns.clear();
		for (int i = 0; i < size; i++) {
			final CardSlotPanel slot = new CardSlotPanel(i);
			final GridBagConstraints gbc = new GridBagConstraints();
			{
				gbc.fill = GridBagConstraints.BOTH;
				gbc.insets = new Insets(0, 2, 0, 2);
				gbc.gridx = i;
				gbc.gridy = 0;
			}
			add(slot, gbc);

			final CardPanel card = new CardPanel(i, target, originId,
					enableMouseActions);
			slot.add(card);
			cardBtns.add(card);
		}
	}

	public CardPanel get(int index) {
		return cardBtns.get(index);
	}

	public void updateCards(Cards<?> cards) {
		for (int i = 0; i < cardBtns.size(); i++) {
			cardBtns.get(i).updateCard(cards.getCard(i));
		}
	}

	public void finishCards(Cards<?> cards) {
		for (int i = 0; i < cardBtns.size(); i++) {
			cardBtns.get(i).finish(!cards.isOnRightPosition(i));
		}
	}
}
