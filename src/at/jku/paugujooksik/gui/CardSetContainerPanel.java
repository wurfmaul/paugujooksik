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

public class CardSetContainerPanel extends JPanel {
	public static final long serialVersionUID = 979482902923838043L;

	public final CardSetPanel cardSet;

	private final JLabel lblCompareCount;
	private final JLabel lblErrorCount;
	private final JLabel lblSwapCount;
	private JLabel lblTitle;

	public CardSetContainerPanel(CardSetHandler target, int size, String name, boolean border,
			boolean enableMouseActions) {

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

		cardSet = new CardSetPanel(size, target, name, enableMouseActions);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 5;
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(cardSet, gbc_panel);
	}

	public void setStats(int compareCount, int swapCount, int errorCount) {
		lblCompareCount.setText(Integer.toString(compareCount));
		lblErrorCount.setText(Integer.toString(errorCount));
		lblSwapCount.setText(Integer.toString(swapCount));
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}
}