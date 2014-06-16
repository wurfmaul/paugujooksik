package at.jku.paugujooksik.gui;

import static at.jku.paugujooksik.logic.Toolkit.DEFAULT_FONT_BOLD;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Presenter extends Window implements CardSetHandler {
	private static final long serialVersionUID = 8299211278767397214L;

	private JFrame frame;
	private ServerGUI target;

	/**
	 * Create the application.
	 */
	public Presenter(Frame owner, ServerGUI target) {
		super(owner);
		this.target = target;
		initialize();
		setToFullScreen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 100, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblConfig = new JLabel("Bubblesort");
		lblConfig.setFont(DEFAULT_FONT_BOLD.deriveFont(36f));
		GridBagConstraints gbcLblConfig = new GridBagConstraints();
		gbcLblConfig.insets = new Insets(10, 10, 10, 10);
		gbcLblConfig.gridx = 0;
		gbcLblConfig.gridy = 0;
		frame.getContentPane().add(lblConfig, gbcLblConfig);

		// NEW LINE
		final String name = "Fritz";

		JPanel pnlRow = new JPanel();
		pnlRow.setBorder(new LineBorder(Color.LIGHT_GRAY, 3, true));
		GridBagConstraints gbcPnlRow = new GridBagConstraints();
		gbcPnlRow.insets = new Insets(10, 10, 10, 10);
		gbcPnlRow.fill = GridBagConstraints.BOTH;
		gbcPnlRow.gridx = 0;
		gbcPnlRow.gridy = 1;
		frame.getContentPane().add(pnlRow, gbcPnlRow);
		GridBagLayout gblPnlRow = new GridBagLayout();
		gblPnlRow.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gblPnlRow.rowHeights = new int[] { 0, 0, 0, 0 };
		gblPnlRow.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gblPnlRow.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		pnlRow.setLayout(gblPnlRow);

		JLabel lblName = new JLabel("Team '" + name + "'");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.gridheight = 2;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		pnlRow.add(lblName, gbc_lblName);
		lblName.setFont(DEFAULT_FONT_BOLD.deriveFont(28f));

		JLabel lblCompare = new JLabel("x ≶ y: ");
		GridBagConstraints gbc_lblCompare = new GridBagConstraints();
		gbc_lblCompare.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompare.gridx = 1;
		gbc_lblCompare.gridy = 0;
		pnlRow.add(lblCompare, gbc_lblCompare);
		lblCompare.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompare.setFont(DEFAULT_FONT_BOLD.deriveFont(28f));

		JLabel lblCompareCount = new JLabel("12");
		GridBagConstraints gbc_lblCompareCount = new GridBagConstraints();
		gbc_lblCompareCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompareCount.gridx = 2;
		gbc_lblCompareCount.gridy = 0;
		pnlRow.add(lblCompareCount, gbc_lblCompareCount);
		lblCompareCount.setFont(DEFAULT_FONT_BOLD.deriveFont(32f));

		JLabel lblError = new JLabel("≠: ");
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.gridheight = 2;
		gbc_lblError.insets = new Insets(0, 0, 5, 5);
		gbc_lblError.gridx = 3;
		gbc_lblError.gridy = 0;
		pnlRow.add(lblError, gbc_lblError);
		lblError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblError.setFont(DEFAULT_FONT_BOLD.deriveFont(48f));

		JLabel lblErrorCount = new JLabel("2");
		GridBagConstraints gbc_lblErrorCount = new GridBagConstraints();
		gbc_lblErrorCount.gridheight = 2;
		gbc_lblErrorCount.insets = new Insets(0, 0, 5, 0);
		gbc_lblErrorCount.gridx = 4;
		gbc_lblErrorCount.gridy = 0;
		pnlRow.add(lblErrorCount, gbc_lblErrorCount);
		lblErrorCount.setFont(DEFAULT_FONT_BOLD.deriveFont(48f));

		JLabel lblSwap = new JLabel("x ↔ y: ");
		GridBagConstraints gbc_lblSwap = new GridBagConstraints();
		gbc_lblSwap.insets = new Insets(0, 0, 5, 5);
		gbc_lblSwap.gridx = 1;
		gbc_lblSwap.gridy = 1;
		pnlRow.add(lblSwap, gbc_lblSwap);
		lblSwap.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSwap.setFont(DEFAULT_FONT_BOLD.deriveFont(28f));

		JLabel lblSwapCount = new JLabel("3");
		GridBagConstraints gbc_lblSwapCount = new GridBagConstraints();
		gbc_lblSwapCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblSwapCount.gridx = 2;
		gbc_lblSwapCount.gridy = 1;
		pnlRow.add(lblSwapCount, gbc_lblSwapCount);
		lblSwapCount.setFont(DEFAULT_FONT_BOLD.deriveFont(32f));

		JPanel pnlCardSet = new CardSet(target.config.size, this, name);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 5;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		pnlRow.add(pnlCardSet, gbc_panel);
	}

	private void setToFullScreen() {
		// use secondary monitor if available
		GraphicsDevice[] devices = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();
		GraphicsDevice monitor = devices.length > 1 ? devices[1] : devices[0];

		frame.dispose();
		frame.setUndecorated(true);
		frame.setVisible(true);
		monitor.setFullScreenWindow(frame);
	}

	@Override
	public void performPin(String originId, int index) {
		// XXX Auto-generated method stub
	}

	@Override
	public void performMark(String originId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performSelect(String originId, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performSwap(String originId) {
		// TODO Auto-generated method stub

	}
}