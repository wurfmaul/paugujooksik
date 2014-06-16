package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.gui.ResourceLoader.BOLD_SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.SANS_FONT;
import static at.jku.paugujooksik.gui.ResourceLoader.loadFonts;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import at.jku.paugujooksik.logic.Cards;
import at.jku.paugujooksik.logic.Configuration;
import at.jku.paugujooksik.logic.ValueGenerator;

public abstract class AbstractGUI implements Serializable {
	private static final long serialVersionUID = -4330835763782334124L;
	
	protected static final Logger DEBUGLOG = Logger.getLogger("DEBUG");
	protected static final boolean GROW_CARDS = false;
	protected static final Font DEFAULT_FONT;
	protected static final Font DEFAULT_FONT_BOLD;
	
	protected final JFrame frame = new JFrame();
	protected final ValueGenerator values = new ValueGenerator();
	protected final List<CardPanel> cardBtns = new LinkedList<>();
	
	protected Configuration<?> config;
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
	
	protected AbstractGUI() {
		config = Configuration.generateDefault();
		cards = new Cards<>(config.values, config.algorithmIndex);
	}

	protected JPanel initAndGetCardPanel(int size) {
		final JPanel pnlCards = new JPanel();
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
		pnlCards.setLayout(gblPnlCards);

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
			pnlCards.add(slot, gbc);

			final CardPanel card = new CardPanel(i, this);
			slot.add(card);
			cardBtns.add(card);
		}
		return pnlCards;
	}
	
	public abstract void performPin(int index);

	public abstract void performMark(int index);

	public abstract void performSelect(int index);

	public abstract void performSwap();
}
