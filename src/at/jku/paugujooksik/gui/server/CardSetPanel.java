package at.jku.paugujooksik.gui.server;

import static at.jku.paugujooksik.logic.Toolkit.DEFAULT_FONT_BOLD;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import at.jku.paugujooksik.gui.CardSet;
import at.jku.paugujooksik.gui.CardSetHandler;

public class CardSetPanel extends JPanel {
	private static final String ERROR_LABEL = "≠ ";
	private static final String SWAP_LABEL = "↔ ";
	private static final String COMPARE_LABEL = "≶ ";
	private static final long serialVersionUID = 979482902923838043L;
	private static final Font STAT_FONT = DEFAULT_FONT_BOLD.deriveFont(28f);
	private static final Color STAT_LINECOLOR = new Color(0, 128, 0);
	private static final Color STAT_COLOR = new Color(204, 255, 153);
	private static final Color ERROR_LINECOLOR = new Color(128, 0, 0);
	private static final Color ERROR_COLOR = new Color(255, 153, 153);

	public final CardSet cardSet;

	private final JLabel lblCompareCount;
	private final JLabel lblErrorCount;
	private final JLabel lblSwapCount;

	public CardSetPanel(CardSetHandler target, int size, String name,
			boolean enableMouseActions) {
		
		setBorder(new LineBorder(Color.LIGHT_GRAY, 3, true));

		GridBagLayout gblPnlRow = new GridBagLayout();
		gblPnlRow.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gblPnlRow.rowHeights = new int[] { 0, 0, 0 };
		gblPnlRow.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gblPnlRow.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gblPnlRow);

		JLabel lblName = new JLabel("Team '" + name + "'");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(5, 5, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblName, gbc_lblName);
		lblName.setFont(STAT_FONT);

		JPanel pnlCompare = new JPanel(new FlowLayout());
		{
			pnlCompare.setBorder(new LineBorder(STAT_LINECOLOR));
			pnlCompare.setBackground(STAT_COLOR);
			
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
			pnlSwap.setBorder(new LineBorder(STAT_LINECOLOR));
			pnlSwap.setBackground(STAT_COLOR);
			
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
			pnlError.setBorder(new LineBorder(ERROR_LINECOLOR));
			pnlError.setBackground(ERROR_COLOR);
			
			GridBagConstraints gbcPnlError = new GridBagConstraints();
			gbcPnlError.insets = new Insets(5, 5, 5, 5);
			gbcPnlError.gridx = 3;
			gbcPnlError.gridy = 0;
			add(pnlError, gbcPnlError);
			
			JLabel lblError = new JLabel(ERROR_LABEL);
			lblError.setHorizontalAlignment(SwingConstants.RIGHT);
			lblError.setFont(STAT_FONT);
			pnlError.add(lblError);
			
			lblErrorCount = new JLabel("0"); // FIXME errorcount does not work
			lblErrorCount.setFont(STAT_FONT);
			pnlError.add(lblErrorCount);
		}
		
		cardSet = new CardSet(size, target, name, enableMouseActions);
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
}
