package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.loadFonts;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.ValueGenerator;

public abstract class AbstractGUI {
	protected static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	protected static final boolean GROW_CARDS = false;
	protected static final int MIN_SIZE = 7;
	protected static final int MAX_SIZE = 15;
	protected static final int DEFAULT_SIZE = 7;
	protected static final Font DEFAULT_FONT;
	protected static final Font DEFAULT_FONT_BOLD;
	
	protected final JFrame frame = new JFrame();
	protected final ValueGenerator values = new ValueGenerator();
	protected final List<CardPanel> cardBtns = new LinkedList<>();
	
	protected int n = DEFAULT_SIZE;
	protected Cards<?> cards;

	static {
		if (loadFonts()) {
			DEFAULT_FONT = new Font(SANS_FONT, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(BOLD_SANS_FONT, Font.BOLD, 16);
		} else {
			DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
			DEFAULT_FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		}
	}
	
	protected void initCardPanel() {
		final JPanel pnlCards = new JPanel();
		final GridBagConstraints gbcPnLCards = new GridBagConstraints();
		{
			gbcPnLCards.gridwidth = 2;
			gbcPnLCards.insets = new Insets(0, 0, 5, 0);
			gbcPnLCards.fill = GridBagConstraints.BOTH;
			gbcPnLCards.gridx = 0;
			gbcPnLCards.gridy = 1;
		}
		frame.getContentPane().add(pnlCards, gbcPnLCards);

		final GridBagLayout gblPnlCards = new GridBagLayout();
		{
			gblPnlCards.columnWidths = new int[n + 1];
			gblPnlCards.rowHeights = new int[] { 0 };
			double[] weights = new double[n + 1];
			{
				Arrays.fill(weights, 1.0);
				weights[weights.length - 1] = Double.MIN_VALUE;
			}
			gblPnlCards.columnWeights = weights;
			gblPnlCards.rowWeights = new double[] { 1.0 };
		}
		pnlCards.setLayout(gblPnlCards);

		cardBtns.clear();
		for (int i = 0; i < n; i++) {
			final CardSlotPanel slot = new CardSlotPanel(i);
			final GridBagConstraints gbc = new GridBagConstraints();
			{
				gbc.fill = GridBagConstraints.BOTH;
				gbc.insets = new Insets(0, 2, 0, 2);
				gbc.gridx = i;
				gbc.gridy = 0;
			}
			pnlCards.add(slot, gbc);

			final CardPanel card = new CardPanel(i, this);
			slot.add(card);
			cardBtns.add(card);
		}
	}
	
	public void performPin(int index) {
		if (cards.getCard(index).selected) {
			try {
				cards.togglePin(index);
				clearErrors();
			} catch (SelectionException ex) {
				reportError(ex);
			}
			checkComponents();
		}
	}

	public void performMark(int index) {
		try {
			cards.toggleMark(index);
			clearErrors();
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	public void performSelect(int index) {
		try {
			cards.select(index);
			updateStats();
			clearErrors();
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	public void performSwap() {
		try {
			cards.swapSelection();
			clearErrors();
			updateStats();
		} catch (SelectionException ex) {
			reportError(ex);
		}
		checkComponents();
	}

	protected abstract void checkComponents();

	protected abstract void clearErrors();

	protected abstract void reportError(SelectionException ex);

	protected abstract void updateStats();
}
