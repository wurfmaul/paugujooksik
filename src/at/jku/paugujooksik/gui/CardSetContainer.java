package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.tools.Constants.COMPARE_LABEL;
import static at.jku.paugujooksik.tools.Constants.ERROR_BACKGROUND_COLOR;
import static at.jku.paugujooksik.tools.Constants.ERROR_LABEL;
import static at.jku.paugujooksik.tools.Constants.ERROR_LINE_COLOR;
import static at.jku.paugujooksik.tools.Constants.PLAYER_BORDER_COLOR;
import static at.jku.paugujooksik.tools.Constants.PLAYER_BORDER_THICKNESS;
import static at.jku.paugujooksik.tools.Constants.STAT_BACKGROUND_COLOR;
import static at.jku.paugujooksik.tools.Constants.STAT_FONT;
import static at.jku.paugujooksik.tools.Constants.STAT_LINE_COLOR;
import static at.jku.paugujooksik.tools.Constants.SWAP_LABEL;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * The outer most container of one player's cards, including a label for
 * name/config and statistics. This contains other layers in order to provide a
 * proper layout.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 * @see Card details on the GUI architecture
 */
public class CardSetContainer extends JPanel {
	public static final long serialVersionUID = 979482902923838043L;

	/** The next layer, the panel that holds the cards. */
	public final CardSet cardSet;
	/** The representing component, responsible for this container. */
	public final PresentationView view;
	/** The player's unique name. */
	public final String name;
	/** Determines whether or not mouse actions are permitted. */
	public final boolean mouseActionsEnabled;

	private final JLabel lblTitle;
	private final JLabel lblCompareCount;
	private final JLabel lblErrorCount;
	private final JLabel lblSwapCount;

	/**
	 * Creates a new basic container.
	 * 
	 * @param view
	 *            See {@link #view}.
	 * @param size
	 *            The amount of cards.
	 * @param name
	 *            See {@link #name}.
	 * @param border
	 *            True if a border should be displayed.
	 * @param enableMouseActions
	 *            See {@link #mouseActionsEnabled}.
	 */
	public CardSetContainer(PresentationView view, int size, String name, boolean border, boolean enableMouseActions) {
		this.view = view;
		this.name = name;
		this.mouseActionsEnabled = enableMouseActions;

		if (border)
			setBorder(new LineBorder(PLAYER_BORDER_COLOR, PLAYER_BORDER_THICKNESS));

		GridBagLayout gblPnlRow = new GridBagLayout();
		gblPnlRow.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gblPnlRow.rowHeights = new int[] { 0, 0, 0 };
		gblPnlRow.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gblPnlRow.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gblPnlRow);

		lblTitle = new JLabel();
		GridBagConstraints gbcLblTitle = new GridBagConstraints();
		gbcLblTitle.insets = new Insets(5, 5, 5, 5);
		gbcLblTitle.gridx = 0;
		gbcLblTitle.gridy = 0;
		add(lblTitle, gbcLblTitle);
		lblTitle.setFont(STAT_FONT);

		JPanel pnlCompare = new JPanel(new FlowLayout());
		{
			pnlCompare.setBorder(new LineBorder(STAT_LINE_COLOR));
			pnlCompare.setBackground(STAT_BACKGROUND_COLOR);

			GridBagConstraints gbcPnlCompare = new GridBagConstraints();
			gbcPnlCompare.insets = new Insets(5, 5, 5, 5);
			gbcPnlCompare.gridx = 1;
			gbcPnlCompare.gridy = 0;
			add(pnlCompare, gbcPnlCompare);

			JLabel lblCompare = new JLabel(COMPARE_LABEL);
			lblCompare.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCompare.setFont(STAT_FONT);
			pnlCompare.add(lblCompare);

			lblCompareCount = new JLabel("0");
			lblCompareCount.setFont(STAT_FONT);
			pnlCompare.add(lblCompareCount);
		}

		JPanel pnlSwap = new JPanel(new FlowLayout());
		{
			pnlSwap.setBorder(new LineBorder(STAT_LINE_COLOR));
			pnlSwap.setBackground(STAT_BACKGROUND_COLOR);

			GridBagConstraints gbcPnlSwap = new GridBagConstraints();
			gbcPnlSwap.insets = new Insets(5, 5, 5, 5);
			gbcPnlSwap.gridx = 2;
			gbcPnlSwap.gridy = 0;
			add(pnlSwap, gbcPnlSwap);

			JLabel lblSwap = new JLabel(SWAP_LABEL);
			lblSwap.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSwap.setFont(STAT_FONT);
			pnlSwap.add(lblSwap);

			lblSwapCount = new JLabel("0");
			lblSwapCount.setFont(STAT_FONT);
			pnlSwap.add(lblSwapCount);
		}

		JPanel pnlError = new JPanel(new FlowLayout());
		{
			pnlError.setBorder(new LineBorder(ERROR_LINE_COLOR));
			pnlError.setBackground(ERROR_BACKGROUND_COLOR);

			GridBagConstraints gbcPnlError = new GridBagConstraints();
			gbcPnlError.insets = new Insets(5, 5, 5, 5);
			gbcPnlError.gridx = 3;
			gbcPnlError.gridy = 0;
			add(pnlError, gbcPnlError);

			JLabel lblError = new JLabel(ERROR_LABEL);
			lblError.setHorizontalAlignment(SwingConstants.RIGHT);
			lblError.setFont(STAT_FONT);
			pnlError.add(lblError);

			lblErrorCount = new JLabel("0");
			lblErrorCount.setFont(STAT_FONT);
			pnlError.add(lblErrorCount);
		}

		cardSet = new CardSet(size, this);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 5;
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(cardSet, gbc_panel);

		// disable resizing when multiple players change card contents
		setPreferredSize(getSize());
	}

	/**
	 * Sets the statistics.
	 * 
	 * @param compareCount
	 *            Number of performed comparisons.
	 * @param swapCount
	 *            Number of performed Swaps.
	 * @param errorCount
	 *            Number of made mistakes.
	 */
	public void setStats(int compareCount, int swapCount, int errorCount) {
		lblCompareCount.setText(Integer.toString(compareCount));
		lblErrorCount.setText(Integer.toString(errorCount));
		lblSwapCount.setText(Integer.toString(swapCount));
	}

	/**
	 * Sets the title if it should be other than the player's name.
	 * 
	 * @param title
	 *            The new title.
	 */
	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	@Override
	protected void validateTree() {
		/**
		 * The following is a very dirty trick to avoid repainting while
		 * animation is ongoing. The layout manager performs a layout operation
		 * of all children, once certain modifications are performed (e.g.
		 * repaint of a component). This would reset all panels to their
		 * original positions, including the moving cards. In order to keep the
		 * layout manager from doing this, the validation command is simply kept
		 * from being delegated any further than here. This hack needs manual
		 * validation after the animation has finished.
		 * 
		 * See {@link CardSet#synchronize(at.jku.paugujooksik.model.CardModel)}!
		 */
		if (!view.isProcessing(name))
			super.validateTree();
	}
}