package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.INSET;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import at.jku.paugujooksik.model.CardModel;

/**
 * Represents a set of cards.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @see Card details on the GUI architecture
 */
public class CardSet extends JPanel {
	private static final long serialVersionUID = 8943110545308400878L;

	private final List<Card> cardBtns = new LinkedList<>();

	public CardSet(int size, PresentationView target, String clientId, boolean enableMouseActions) {
		set(size, target, clientId, enableMouseActions);
		setOpaque(false);
	}

	public Card get(int index) {
		return cardBtns.get(index);
	}

	public void finishCards(CardModel<?> cards) {
		for (int i = 0; i < cardBtns.size(); i++) {
			cardBtns.get(i).finish(!cards.isOnRightPosition(i));
		}
	}

	public void set(int size, PresentationView target, String clientId, boolean enableMouseActions) {
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

		removeAll();
		cardBtns.clear();

		for (int i = 0; i < size; i++) {
			final Card card = new Card(i, target, clientId, enableMouseActions);
			final GridBagConstraints gbcCard = new GridBagConstraints();
			{
				gbcCard.fill = GridBagConstraints.BOTH;
				gbcCard.insets = new Insets(25, INSET + 2, INSET, INSET + 2);
				gbcCard.gridx = i;
				gbcCard.gridy = 0;
			}
			add(card, gbcCard);
			cardBtns.add(card);
		}

		for (int i = 0; i < size; i++) {
			final CardSlot slot = new CardSlot(i);
			final GridBagConstraints gbcSlot = new GridBagConstraints();
			{
				gbcSlot.fill = GridBagConstraints.BOTH;
				gbcSlot.insets = new Insets(0, 2, 0, 2);
				gbcSlot.gridx = i;
				gbcSlot.gridy = 0;
			}
			add(slot, gbcSlot);
		}

	}

	public void updateCards(CardModel<?> cards) {
		for (int i = 0; i < cardBtns.size(); i++) {
			cardBtns.get(i).updateCard(cards.getCard(i));
		}
		validate();
	}
}
