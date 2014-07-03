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

	private final CardSetContainer container;
	private final List<Card> cardList = new LinkedList<>();

	/**
	 * Creates a new set of cards.
	 * 
	 * @param size
	 *            The amount of cards.
	 * @param container
	 *            The component that contains this set.
	 */
	public CardSet(int size, CardSetContainer container) {
		this.container = container;
		setSize(size);
		setOpaque(false);
	}

	/**
	 * Returns the card at specified index.
	 * 
	 * @param index
	 *            The card's index.
	 * @return The card at specified position.
	 */
	public Card get(int index) {
		return cardList.get(index);
	}

	/**
	 * Prepare cards ford their final appearance according to the model.
	 * 
	 * @param model
	 *            The cardset's model.
	 */
	public void finishCards(CardModel<?> model) {
		for (int i = 0; i < cardList.size(); i++) {
			cardList.get(i).finish(!model.isOnRightPosition(i));
		}
	}

	/**
	 * Sets the amount of cards this set should contain and performs the
	 * necessary initializations.
	 * 
	 * @param size
	 *            The amount of cards.
	 */
	public void setSize(int size) {
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

		// remove old cards from the panel
		removeAll();
		cardList.clear();

		// setup the actual cards
		for (int i = 0; i < size; i++) {
			final Card card = new Card(i, container);
			final GridBagConstraints gbcCard = new GridBagConstraints();
			{
				gbcCard.fill = GridBagConstraints.BOTH;
				gbcCard.insets = new Insets(25, INSET + 2, INSET, INSET + 2);
				gbcCard.gridx = i;
				gbcCard.gridy = 0;
			}
			add(card, gbcCard);
			cardList.add(card);
		}

		// setup the borders around the cards -> slots
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

	/**
	 * Synchronizes the GUI elements with the underlying model.
	 * 
	 * @param model
	 *            The model, this card should match.
	 */
	public void synchronize(CardModel<?> model) {
		for (int i = 0; i < cardList.size(); i++) {
			cardList.get(i).synchronize(model.getCard(i));
		}
		/**
		 * In order to complete the layout trick, a validate command has to be
		 * delegated manually here. See {@link CardSetContainer#validateTree()}
		 * for further details.
		 */
		validate();
	}
}
